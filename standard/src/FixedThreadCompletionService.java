import java.util.Queue;
import java.util.concurrent.*;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class FixedThreadCompletionService<V> implements SimpleCompletionService<V> {
    private Worker[] workers;
    private volatile boolean shutdown = false;
    private volatile boolean terminated = false; // Only changed by terminationHandler()
    private AtomicInteger runCounter = new AtomicInteger();
    private FutureTask<V> poisonpill = new FutureTask<V>(new DummyCallable<V>());
    private BlockingQueue<FutureTask<V>> arbeitsAuftraege = new LinkedBlockingQueue();
    private BlockingQueue<FutureTask<V>> fertigeArbeitsAuftraege = new LinkedBlockingQueue();

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

            while (true) {
                if (!arbeitsAuftraege.isEmpty()) {
                    FutureTask<V> currentTask = arbeitsAuftraege.poll();
                    if (currentTask != null) {
                        currentTask.run();
                        fertigeArbeitsAuftraege.add(currentTask);
                    }
                } else if (shutdown) {
                    break;
                } else {
                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        private void terminationHandler() {
            terminated = true;
            for (int x = 0; x < currentlyInMethod.get(); x++) {
                fertigeArbeitsAuftraege.add(poisonpill);
            }
        }

        public void run() {
            doWork();
            // If it is the last thread that has finished.
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
            Worker work = new Worker(x + 1);
            workers[x] = work;
            work.start();

        }
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void shutdown() {
        shutdown = true;
//        while (!arbeitsAuftraege.isEmpty()) {
//        }
//        for (Worker w : workers) {
//            try {
//                w.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }

    @Override
    public Future<V> poll() {
        FutureTask result = fertigeArbeitsAuftraege.poll();
        if (result == poisonpill) {
            return null;
        }
        return result;
    }

    @Override
    public Future<V> submit(Callable<V> task) throws RejectedExecutionException {
        if (!shutdown) {
            FutureTask<V> taskFut = new FutureTask(task);
            arbeitsAuftraege.add(taskFut);
            return taskFut;
        } else {
            throw new RejectedExecutionException();
        }

    }

    AtomicInteger currentlyInMethod = new AtomicInteger(0);

    @Override
    public Future<V> take() throws InterruptedException {
        currentlyInMethod.getAndIncrement();
        if (terminated) {
            currentlyInMethod.getAndDecrement();
            return null;
        }
        System.out.println("SO Many People here:" + currentlyInMethod);
        FutureTask doneTask = fertigeArbeitsAuftraege.take();
        if (doneTask == poisonpill) {
            currentlyInMethod.getAndDecrement();
            fertigeArbeitsAuftraege.add(poisonpill);
            return null;
        }
            currentlyInMethod.getAndDecrement();
            return doneTask;
    }
}
