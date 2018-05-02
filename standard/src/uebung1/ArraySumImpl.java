import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;

public class ArraySumImpl implements ArraySum {
    private ExecutorService executorService;
    private int numberOfTasksPerThread;
    private Collection<Future> futureList;
    private int lengthOfArray;

    @Override
    public long sum(long[] array, int threads) {
        validateParameters(array, threads);
        long result = 0;
        lengthOfArray = array.length;
        numberOfTasksPerThread = lengthOfArray / threads;
        executorService = Executors.newFixedThreadPool(threads);
        futureList = new ArrayList();

        initialiseThreads(array, threads);
        try {
            result = calculateSumOfThreads();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        return result;
    }

    private long calculateSumOfThreads() throws ExecutionException, InterruptedException {
        long sum = 0;
        while (!futureList.isEmpty()) {
            final Iterator<Future> iter = futureList.iterator();
            while (iter.hasNext()) {
                Future fut = iter.next();
                if (fut.isDone()) {
                    sum = sum + (long) fut.get();
                    iter.remove();
                }
            }
        }
        return sum;
    }

    private void initialiseThreads(long[] array, int threads) {
        int additionalTasks = lengthOfArray % threads;
        int lowerLimitOfArray = 0;
        for (int i = 0; i < threads; i++) {
            long[] tempArray = new long[numberOfTasksPerThread + additionalTasks];
            if (additionalTasks != 0) {
                System.arraycopy(array, lowerLimitOfArray, tempArray, 0, numberOfTasksPerThread + 1);
                additionalTasks--;
                lowerLimitOfArray = lowerLimitOfArray + numberOfTasksPerThread + 1;
            } else {
                System.arraycopy(array, lowerLimitOfArray, tempArray, 0, numberOfTasksPerThread);
                lowerLimitOfArray = lowerLimitOfArray + numberOfTasksPerThread;
            }
            futureList.add(executorService.submit(new AdditionCalculatorTask(tempArray)));
        }
    }

    private void validateParameters(long[] array, int threads) {
        if (array == null || threads == 0) {
            throw new InvalidParameterException("Error: Calculations with given Parameters not possible");
        }
    }

    protected static class AdditionCalculatorTask implements Callable {
        final long[] arrayToProcess;

        public AdditionCalculatorTask(long[] arrayToProcess) {
            this.arrayToProcess = arrayToProcess;
        }

        @Override
        public Object call() {
            long sum = 0;
            for (int x = 0; x < arrayToProcess.length; x++) {
                sum = sum + arrayToProcess[x];
            }
            return sum;
        }
    }
}
