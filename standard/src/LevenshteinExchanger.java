import java.util.concurrent.Exchanger;

public class LevenshteinExchanger extends Levenshtein {
    volatile private int levenStheinMatrix[][];
    private ComputeLevenstheinThread[] threads;
    private Exchanger[] exchanger;
    private int numberofThreads;

    public LevenshteinExchanger(int i) {
        this.numberofThreads = i;
    }

    private void initialiseExchanger() {
        exchanger = new Exchanger[numberofThreads - 1];
        for (int x = 0; x < numberofThreads - 1; x++) {
            exchanger[x] = new Exchanger();
        }
    }

    @Override
    public int[][] computeLevenshtein(char[] wordHorizontal, char[] wordVertical) {
        levenStheinMatrix = new int[wordVertical.length + 1][wordHorizontal.length + 1];
        initialiseExchanger();
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
        threads = new ComputeLevenstheinThread[numberofThreads];
        int fromColumn = 0;
        int rowsPerThread = wordVertical.length / numberofThreads;
        int remainingThreads = wordVertical.length % numberofThreads;
        for (int x = 0; x < numberofThreads; x++) {
            if (remainingThreads != 0) {
                threads[x] = new ComputeLevenstheinThread(x, fromColumn, (fromColumn + rowsPerThread), levenStheinMatrix, wordHorizontal, wordVertical);
                setExchangers(x);
                fromColumn = fromColumn + rowsPerThread + 1;
                remainingThreads--;
            } else {
                threads[x] = new ComputeLevenstheinThread(x, fromColumn, (fromColumn + rowsPerThread), levenStheinMatrix, wordHorizontal, wordVertical);
                setExchangers(x);
                fromColumn = fromColumn + rowsPerThread;
            }
            threads[x].start();
        }
    }

    private void setExchangers(int x) {
        if (x == 0) {
            threads[x].setExchangerLeft(null);
            threads[x].setExchangerRight(exchanger[x]);
        } else if (x == numberofThreads - 1) {
            threads[x].setExchangerLeft(exchanger[x - 1]);
            threads[x].setExchangerRight(null);
        } else {
            threads[x].setExchangerLeft(exchanger[x - 1]);
            threads[x].setExchangerRight(exchanger[x]);
        }
    }

    class ComputeLevenstheinThread extends Thread {
        private int fromColumn;
        private int toColumn;
        private Exchanger exchangerLeft;
        private Exchanger exchangerRight;
        private int[][] levenStheinMatrix;
        private char[] wordHorizontal;
        private char[] wordVertical;


        public void setExchangerLeft(Exchanger exchangerLeft) {
            this.exchangerLeft = exchangerLeft;
        }

        public void setExchangerRight(Exchanger exchangerRight) {
            this.exchangerRight = exchangerRight;
        }

        public ComputeLevenstheinThread(int id, int fromColumn, int toColumn, int[][] levenStheinMatrix, char[] wordHorizontal, char[] wordVertical) {
            this.fromColumn = fromColumn;
            this.toColumn = toColumn;
            this.levenStheinMatrix = levenStheinMatrix;
            this.wordHorizontal = wordHorizontal;
            this.wordVertical = wordVertical;
        }


//                [0, 1, 2, 3, 4, 5, 6, 7, 8]
//                [1, 0, 1, 2, 3, 4, 5, 6, 7]
//                [2, 1, 1, 2, 2, 3, 4, 5, 6]
//                [3, 2, 2, 2, 3, 3, 4, 5, 6]
//                [4, 3, 3, 3, 3, 4, 3, 4, 5]
//                [5, 4, 3, 4, 4, 4, 4, 3, 4]
//                [6, 5, 4, 4, 5, 5, 5, 4, 3]


//                [0, 1, 2, 3, 4, 5, 6, 7, 8]
//                [1, 0, 1, 2, 3, 4, 5, 6, 7]
//                [2, 1, 1, 2, 2, 1, 1, 1, 1]
//                [3, 2, 2, 2, 3, 2, 2, 2, 2]
//                [4, 1, 1, 1, 1, 1, 2, 1, 1]
//                [5, 2, 1, 2, 2, 2, 2, 2, 2]
//                [6, 3, 2, 2, 3, 3, 3, 3, 2]

        @Override
        public void run() {
            int rows = levenStheinMatrix[0].length;


            for (int j = 0; j < rows; j++) {
                if (exchangerLeft != null) {
                    try {
                        exchangerLeft.exchange(null);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = fromColumn; i <= toColumn; i++) {
                    synchronized (LevenshteinExchanger.class) {
                        levenStheinMatrix[i][j] = computeValue(i, j, levenStheinMatrix, wordHorizontal, wordVertical);
                    }
                }
                if (exchangerRight != null) {
                    try {
                        exchangerRight.exchange(null);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


