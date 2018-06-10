public class GameOfLifePar extends GameOfLife{

    private int[][] initialGameState;
    private int threads;
    private int cellDisplaySize;
    private int generations;
    private int displayUpdateRate;
    private int sleepTime;
    private boolean showUI;
    private boolean printGPS;

    @Override
    public void configure(int[][] initialGameState, int threads, int cellDisplaySize, int generations, int displayUpdateRate, int sleepTime, boolean showUI, boolean printGPS) {
        this.initialGameState = initialGameState;
        this.threads = threads;
        this.cellDisplaySize = cellDisplaySize;
        this.generations = generations;
        this.displayUpdateRate = displayUpdateRate;
        this.sleepTime = sleepTime;
        this.showUI = showUI;
        this.printGPS = printGPS;
    }

    @Override
    public int[][] evolve(int generations) {
        return new int[0][];
    }

    @Override
    public void shutdown() {

    }

    @Override
    public int[][] getEndPosition() {
        return new int[0][];
    }
}
