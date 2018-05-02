import java.awt.*;
import java.util.Random;

public class FuncPainterImpl implements FuncPainter {
    /**
     * Helper method for setting the value of the screen.
     *
     * @param screen The screen
     * @param func   The function to evaluate
     * @param x      x component of the point to color (in range [ 0; screen.getWidth() ))
     * @param y      y component of the point to color (in range [ 0; screen.getHeight() ))
     */
    private void setValue(Screen screen, Function func, int x, int y) { //hier synchronized works
        if (!screen.hasValue(x, y)) {
            screen.setValue(x, y, func.evaluate(x, y));
        }
    }

    public void randomPaint(Screen screen, Function func, int nThreads) {
        int maxHeight = screen.getHeight();
        int maxWidth = screen.getWidth();
        for (int x = 0; x < nThreads; x++) {
            Thread th = new Thread() {
                Random generator = new Random();

                public void run() {
                    while (!screen.finished()) {

                        setValue(screen, func, generator.nextInt(maxHeight), generator.nextInt(maxWidth));
                    }

                }

            };
            th.start();
        }
    }

    public void synchronizedPaint(Screen screen, Function func, int nThreads) {
        int maxHeight = screen.getHeight();
        int maxWidth = screen.getWidth();
        for (int x = 0; x < nThreads; x++) {
            Thread th = new Thread() {
                Random generator = new Random();

                public void run() {
                    synchronized (PainterScreen.class) {
                        while (!screen.finished()) {
                            FuncPainterImpl.this.setValue(screen, func, generator.nextInt(maxHeight), generator.nextInt(maxWidth));
                        }
                    }
                }
            };
            th.start();
        }
    }

    public void syncFreePaint(Screen screen, Function func, final int nThreads) {
        int maxHeight = screen.getHeight();
        int maxWidth = screen.getWidth();
        int heightPerThread = maxHeight / nThreads;
        int remainingRows = maxHeight % nThreads;
        int startHeight = 0;
        for (int x = 0; x < nThreads; x++) {

            th.start();
            startHeight = startHeight + heightPerThread;
        }
    }

    public static void main(String[] args) {
        FuncPainter p = new FuncPainterImpl();
        int width = 70;
        int height = 70;

        Screen randomScreen = new PainterScreen(width, height);
        Screen synchronizedScreen = new PainterScreen(width, height);
//        Screen syncFreeScreen = new PainterScreen(width, height);
        p.randomPaint(randomScreen, new ExampleFunction(), 4);
        p.synchronizedPaint(synchronizedScreen, new ExampleFunction(), 4);
//        p.syncFreePaint(syncFreeScreen, new ExampleFunction(), 4);
    }
}

class PainterThread extends Thread {

    private int startHeight;

    public PainterThread(int startHeight, int iterateToHeight, int maxwidth){

    }

    public void run(){
        for(int hoehe = 0; hoehe <= heightPerThread; hoehe++){
            for(int breite = 0; breite <= maxWidth; breite++){
                FuncPainterImpl.this.setValue(screen, func, hoehe + localstartHeight, breite);
            }
        }
    }
}

