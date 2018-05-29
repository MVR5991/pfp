import java.util.concurrent.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedThreadCompletionService<V> implements SimpleCompletionService<V> {
    private Worker[] workers;
    private AtomicBoolean shutdown = new AtomicBoolean(false);
    private volatile boolean terminated = false; // Only changed by terminationHandler()
    private AtomicInteger runCounter = new AtomicInteger();
    private FutureTask<V> poisonpill = new FutureTask<V>(new DummyCallable<V>());
    private BlockingQueue<FutureTask<V>> arbeitsAuftraege = new LinkedBlockingQueue();
    private BlockingQueue<FutureTask<V>> fertigeArbeitsAuftraege = new LinkedBlockingQueue();
    private AtomicInteger currentlyInMethod = new AtomicInteger(0);

    private class DummyCallable<V> implements Callable<V> {
        public V call() {
            return null;
        }

    }

    private class Worker extends Thread {
        private int threads;


        public Worker(int threads) {
            this.threads = threads;
        }

        private void doWork() {

            while (!arbeitsAuftraege.isEmpty() || !isShutdown()) {
                FutureTask<V> currentTask;
                try {
                    currentTask = arbeitsAuftraege.poll();
                    if (currentTask != null) {
                        currentTask.run();
                        fertigeArbeitsAuftraege.put(currentTask);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void terminationHandler() {
            int y = currentlyInMethod.get();
            for (int x = 0; x < y; x++) {
                try {
                    fertigeArbeitsAuftraege.put(poisonpill);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            terminated = true;
        }

        public void run() {
            doWork();
            int c = runCounter.incrementAndGet();
            if (c == threads) {
                terminationHandler();
            }
            interrupt();
        }

    }

    public FixedThreadCompletionService(int threads) {
        workers = new FixedThreadCompletionService.Worker[threads];
        for (int x = 0; x < threads; x++) {
            Worker work = new Worker(threads);
            workers[x] = work;
            work.start();
        }
    }

    @Override
    public boolean isShutdown() {
        return shutdown.get();
    }

    @Override
    public void shutdown() {
        shutdown.getAndSet(true);
    }

    @Override
    public Future<V> submit(Callable<V> task) throws RejectedExecutionException {
        if (!isShutdown()) {
            FutureTask<V> taskFut = new FutureTask(task);
            try {
                arbeitsAuftraege.put(taskFut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return taskFut;
        } else {
            throw new RejectedExecutionException();
        }
    }

    @Override
    public Future<V> poll() {
        FutureTask result = fertigeArbeitsAuftraege.poll();
        if (result.equals(poisonpill)) {
            try {
                fertigeArbeitsAuftraege.put(poisonpill);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        return result;
    }

    @Override
    public Future<V> take() throws InterruptedException {
        currentlyInMethod.getAndIncrement();
        if (terminated && fertigeArbeitsAuftraege.isEmpty()) {
            currentlyInMethod.getAndDecrement();
            return null;
        }
        FutureTask doneTask = fertigeArbeitsAuftraege.take();
        if (doneTask.equals(poisonpill)) {
            currentlyInMethod.getAndDecrement();
            return null;
        }
        currentlyInMethod.getAndDecrement();
        return doneTask;
    }
}
