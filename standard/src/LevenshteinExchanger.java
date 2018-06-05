import java.util.concurrent.Exchanger;

public class LevenshteinExchanger extends Levenshtein {
    private int levenStheinMatrix[][];
    private computeLevenstheinThread[] threads;
    private Exchanger[] exchanger;
    private int numberofThreads;

    public LevenshteinExchanger(int i) {
        this.numberofThreads = i;
    }

    private void initialiseExchanger() {
        exchanger = new Exchanger[numberofThreads-1];
        for(int x = 0; x < numberofThreads-1; x++){
            exchanger[x] = new Exchanger();
        }
    }

    @Override
    public int[][] computeLevenshtein(char[] wordHorizontal, char[] wordVertical) {
        levenStheinMatrix = new int[wordVertical.length+1][wordHorizontal.length+1];
        initialiseThreads(wordVertical);
        initialiseExchanger();
        return levenStheinMatrix;
    }

    private void initialiseThreads(char[] wordVertical) {
        threads = new computeLevenstheinThread[numberofThreads];
        int fromColumn = 1;
        int rowsPerThread = wordVertical.length/numberofThreads;
        int remainingThreads = wordVertical.length&numberofThreads;
        for (int x = 0; x < numberofThreads; x++) {
            if(remainingThreads != 0){
                threads[x] = new computeLevenstheinThread(x, fromColumn, fromColumn+rowsPerThread);
                fromColumn = fromColumn + rowsPerThread;
                remainingThreads--;
            } else {
                threads[x] = new computeLevenstheinThread(x, fromColumn, (fromColumn+rowsPerThread)-1);
                fromColumn = fromColumn + rowsPerThread + 1;
            }
        }
    }

    static class computeLevenstheinThread extends Thread {
        private int threadNum;
        private int fromColumn;
        private int toColumn;
        private Exchanger exchangerLeft;
        private Exchanger exchangerRight;

        public computeLevenstheinThread(int id, int fromColumn, int toColumn) {
            this.fromColumn = fromColumn;
            this.toColumn = toColumn;
            this.threadNum = id;
        }

        @Override
        public void run() {
            super.run();
        }
    }
}


