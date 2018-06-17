package oldWOrk;

public class GameOfLifeSeq extends GameOfLife{

    int[][] currentGameState;
    int[][] newBoard;
//    private int maximumGenerations;
    @Override
    public void configure(int[][] initialGameState, int threads, int cellDisplaySize, int generations, int displayUpdateRate, int sleepTime, boolean showUI, boolean printGPS) {
     currentGameState = initialGameState;
//     maximumGenerations = generations;
    }

    @Override
    public int[][] evolve(int generations) {
        int [][] result = null;
        for(int x = 0; x < generations; x ++){
                result = calculateNextGeneration();
        }
        return result;
    }

    @Override
    public void shutdown() {
        //not needed for seq. calculation
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
}
