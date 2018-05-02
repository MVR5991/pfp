import java.awt.*;
import java.security.InvalidParameterException;
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
        validateParameters(screen, func, nThreads);
        int maxHeight = screen.getHeight();
        int maxWidth = screen.getWidth();
        for (int x = 0; x < nThreads; x++) {
            new Thread() {
                Random generator = new Random();

                public void run() {
                    while (!screen.finished()) {
                        setValue(screen, func, generator.nextInt(maxWidth), generator.nextInt(maxHeight));
                    }
                }
            }.start();
        }
    }

    private void validateParameters(Screen screen, Function func, int threads) {
        if (screen == null || func == null || threads == 0) {
            throw new InvalidParameterException("Error: Calculation with given parameters not possible");
        }

    }

    public void synchronizedPaint(Screen screen, Function func, int nThreads) {
        validateParameters(screen, func, nThreads);
        int maxHeight = screen.getHeight();
        int maxWidth = screen.getWidth();
        for (int x = 0; x < nThreads; x++) {
            new Thread() {
                Random generator = new Random();

                public void run() {
                    synchronized (PainterScreen.class) {
                        while (!screen.finished()) {
                            FuncPainterImpl.this.setValue(screen, func, generator.nextInt(maxWidth), generator.nextInt(maxHeight));
                        }
                    }
                }
            }.start();
        }
    }

    public void syncFreePaint(Screen screen, Function func, final int nThreads) {
        validateParameters(screen, func, nThreads);
        int maxHeight = screen.getHeight();
        int heightPerThread = maxHeight / nThreads;
        int remainingRows = maxHeight % nThreads;
        int startHeight = 0;
        for (int x = 0; x < nThreads; x++) {
            PainterThread pt;
            if (remainingRows != 0) {
                pt = new PainterThread(startHeight, startHeight + heightPerThread + 1, screen, func);
                remainingRows--;
                startHeight = startHeight + heightPerThread + 1;
            } else {
                pt = new PainterThread(startHeight, startHeight + heightPerThread, screen, func);
                startHeight = startHeight + heightPerThread;
            }
            pt.start();
        }
    }

    public static void main(String[] args) {
        FuncPainter p = new FuncPainterImpl();
        int width = 70;
        int height = 70;

        Screen randomScreen = new PainterScreen(width, height);
        Screen synchronizedScreen = new PainterScreen(width, height);
        Screen syncFreeScreen = new PainterScreen(width, height);
        Screen syncFreeScreenBig = new PainterScreen(400, 400);
        p.syncFreePaint(syncFreeScreenBig, new ExampleFunction(), 4);
        p.randomPaint(randomScreen, new ExampleFunction(), 4);
        p.synchronizedPaint(synchronizedScreen, new ExampleFunction(), 4);
        p.syncFreePaint(syncFreeScreen, new ExampleFunction(), 4);
        p.syncFreePaint(null, new ExampleFunction(), 56);
    }
}

class PainterThread extends Thread {

    private int rowFrom, rowTo;
    private Screen screen;
    private Function func;

    public PainterThread(int rowFrom, int rowTo, Screen screen, Function func) {
        this.rowFrom = rowFrom;
        this.rowTo = rowTo;
        this.screen = screen;
        this.func = func;
    }

    public void run() {
        while (rowFrom < rowTo) {
            for (int breite = 0; breite < screen.getWidth(); breite++) {
                screen.setValue(breite, rowFrom, func.evaluate(breite, rowFrom));
            }
            rowFrom++;
        }
    }
}

