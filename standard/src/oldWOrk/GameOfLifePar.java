package oldWOrk;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class GameOfLifePar extends GameOfLife {

    private int numberOfThreads;
    private int[][] currentGameState;
    private int[][] newBoard;
    private GolThread[] threads;
    volatile private int maximumGenerations;
    private CyclicBarrier barrier;
    private AtomicInteger currentGeneration = new AtomicInteger(0);

    @Override
    public void configure(int[][] initialGameState, int threads, int cellDisplaySize, int generations, int displayUpdateRate, int sleepTime, boolean showUI, boolean printGPS) {
        currentGameState = initialGameState;
        maximumGenerations = generations;
        this.numberOfThreads = threads;
        barrier = new CyclicBarrier(numberOfThreads + 1);
        initialiseThreads();
        currentGeneration = new AtomicInteger(0);
    }

    private void initialiseThreads() {
        int fromColumn = 0;
        int rowsPerThread = currentGameState.length / numberOfThreads;
        int remainingThreads = currentGameState.length % numberOfThreads;
        threads = new GolThread[numberOfThreads];
        for (int x = 0; x < numberOfThreads; x++) {
            int rows = remainingThreads > 0 ? rowsPerThread + 1 : rowsPerThread;
            GolThread t = new GolThread(fromColumn, fromColumn + rows, barrier);
            fromColumn = fromColumn + rows;
            remainingThreads--;
            threads[x] = t;
            t.start();
        }
    }

    private void joinThreads() {
        for (int x = 0; x < numberOfThreads; x++) {
            try {
                threads[x].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int[][] evolve(int generations) {
        for (int gen = 0; gen < generations; gen++) {
            newBoard = new int[currentGameState.length][currentGameState[0].length];
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            while (barrier.getNumberWaiting() != numberOfThreads) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentGameState = newBoard;
            currentGeneration.getAndIncrement();
        }
        return currentGameState;
    }

    private void sleep() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        joinThreads();
    }

    @Override
    public int[][] getEndPosition() {
        return currentGameState;
    }

    private int[][] calculateNextGeneration() {
        newBoard = new int[currentGameState.length][currentGameState[0].length];

        for (int x = 0; x < currentGameState.length; x++) {
            for (int y = 0; y < currentGameState[0].length; y++) {
                newBoard[x][y] = getField(x, y);
                checkBoard(x, y);
            }
        }
        currentGameState = newBoard;
        return currentGameState;
    }

    private void checkBoard(int x, int y) {
        int[] indexY = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] indexX = {-1, 0, 1, -1, 1, -1, 0, 1};

        int fieldValue = currentGameState[x][y];

        int neighbours = 0;
        for (int i = 0; i < 8; i++) {
            if (x + indexX[i] >= 0 && y + indexY[i] >= 0 && x + indexX[i] < currentGameState.length && y + indexY[i] < currentGameState.length) {
                neighbours = neighbours + getField(x + indexX[i], y + indexY[i]);
            }
        }
        checkConditions(x, y, fieldValue, neighbours);
    }

    private void checkConditions(int x, int y, int fieldValue, int neighbours) {
        checkIfDeadCellHasThreeLivingNeighbours(x, y, fieldValue, neighbours);
        checkIfLivingCellHasLessThanTwoLivingNeighbours(x, y, fieldValue, neighbours);
        checkIfLivingCellHasTwoOrThreeLivingNeighbours(x, y, fieldValue, neighbours);
        checkIfLivingCellHasMoreThanThreeLivingNeighbours(x, y, fieldValue, neighbours);
    }

    //rules
    private void checkIfLivingCellHasMoreThanThreeLivingNeighbours(int x, int y, int fieldValue, int neighbours) {
        if (fieldValue == 1 && neighbours > 3) {
            newBoard[x][y] = 0;
        }
    }

    private void checkIfLivingCellHasTwoOrThreeLivingNeighbours(int x, int y, int fieldValue, int neighbours) {
        if (fieldValue == 1 && (neighbours == 2 || neighbours == 3)) {
            newBoard[x][y] = 1;
        }
    }

    private void checkIfLivingCellHasLessThanTwoLivingNeighbours(int x, int y, int fieldValue, int neighbours) {
        if (fieldValue == 1 && neighbours < 2) {
            newBoard[x][y] = 0;
        }
    }

    private void checkIfDeadCellHasThreeLivingNeighbours(int x, int y, int fieldValue, int neighbours) {
        if (fieldValue == 0 && neighbours == 3) {
            newBoard[x][y] = 1;
        }
    }

    private int getField(int x, int y) {
        return currentGameState[x][y];
    }

class GolThread extends Thread {

    private final int fromColumn;
    private final int toColumn;
    private CyclicBarrier barrier;

    public GolThread(int fromColumn, int toColumn, CyclicBarrier barrier) {
        this.fromColumn = fromColumn;
        this.toColumn = toColumn;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        while (currentGeneration.get() < maximumGenerations) {
                for (int x = fromColumn; x < toColumn; x++) {
                    for (int y = 0; y < currentGameState[0].length; y++) {
                        newBoard[x][y] = getField(x, y);
                        checkBoard(x, y);
                    }
                }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
}
