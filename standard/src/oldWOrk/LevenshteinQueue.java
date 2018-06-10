package oldWOrk;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LevenshteinQueue extends Levenshtein {

    private int levenStheinMatrix[][];
    private ComputeLevenstheinQueueThread[] threads;
    private int numberofThreads;

    public LevenshteinQueue(int i) {
        this.numberofThreads = i;
    }

    @Override
    public int[][] computeLevenshtein(char[] wordHorizontal, char[] wordVertical) {
        levenStheinMatrix = new int[wordVertical.length + 1][wordHorizontal.length + 1];
        initialiseThreads(wordVertical, wordHorizontal);
        for (int x = 0; x < numberofThreads; x++) {
            try {
                threads[x].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return levenStheinMatrix;
    }

    private void initialiseThreads(char[] wordVertical, char[] wordHorizontal) {
        threads = new ComputeLevenstheinQueueThread[numberofThreads];
        int fromColumn = 0;
        int rowsPerThread = wordVertical.length / numberofThreads;
        int remainingThreads = wordVertical.length % numberofThreads;
        for (int x = 0; x < numberofThreads; x++) {
            if (remainingThreads != 0) {
                threads[x] = new ComputeLevenstheinQueueThread(x, fromColumn, (fromColumn + rowsPerThread), levenStheinMatrix, wordHorizontal, wordVertical);
                fromColumn = fromColumn + rowsPerThread + 1;
                remainingThreads--;
            } else {
                threads[x] = new ComputeLevenstheinQueueThread(x, fromColumn, (fromColumn + rowsPerThread), levenStheinMatrix, wordHorizontal, wordVertical);
                fromColumn = fromColumn + rowsPerThread;
            }
        }
        for (int y = 0; y < numberofThreads - 1; y++) {
            threads[y].setNextThread(threads[y + 1]);
        }
        for (int x = 0; x < numberofThreads; x++){
            threads[x].start();
        }
    }

    class ComputeLevenstheinQueueThread extends Thread {
        private int id;
        private int fromColumn;
        private int toColumn;
        private int[][] levenStheinMatrix;
        private char[] wordHorizontal;
        private char[] wordVertical;
        BlockingQueue fertigeZeilenQueue = new LinkedBlockingQueue();
        ComputeLevenstheinQueueThread nextThread;

        public void setNextThread(ComputeLevenstheinQueueThread nextThread) {
            this.nextThread = nextThread;
        }

        public ComputeLevenstheinQueueThread(int id, int fromColumn, int toColumn, int[][] levenStheinMatrix, char[] wordHorizontal, char[] wordVertical) {
            this.fromColumn = fromColumn;
            this.toColumn = toColumn;
            this.levenStheinMatrix = levenStheinMatrix;
            this.wordHorizontal = wordHorizontal;
            this.wordVertical = wordVertical;
            this.id = id;
        }


        @Override
        public void run() {
            int rows = levenStheinMatrix[0].length;

            for (int j = 0; j < rows; j++) {
                if (id != 0) {
                    try {
                        fertigeZeilenQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = fromColumn; i <= toColumn; i++) {
                        levenStheinMatrix[i][j] = computeValue(i, j, levenStheinMatrix, wordHorizontal, wordVertical);
                }
                if (nextThread!= null) {
                    try {
                        nextThread.fertigeZeilenQueue.put(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


