
public class SingerThread extends Thread {

    private int THREADNUMMER;

    public SingerThread(int threadnummer) {
        this.THREADNUMMER = threadnummer;
    }

    @Override
    public void run() {
        if (THREADNUMMER == 0) {
            System.out.println("No more bottles of beer on the wall, no more bottles of beer. Go to the\n" +
                    "store and buy some more, 99 bottles of beer on the wall.\n");
        } else if (THREADNUMMER == 1) {
            System.out.println("1 bottle of beer on the wall, 1 bottle of beer. Take one down and pass it\n" +
                    "around, no more bottles of beer on the wall.\n");
        } else if (THREADNUMMER == 2) {
            System.out.println("2 bottles of beer on the wall, 2 bottles of beer. Take one down and pass\n" +
                    "it around, 1 bottle of beer on the wall.\n");
        } else {
            System.out.println(THREADNUMMER + " bottles of beer on the wall, " + THREADNUMMER + " bottles of beer.\n" +
                    "Take one down and pass it around, " + (THREADNUMMER - 1) + " bottles of beer on the\n" +
                    "wall.\n");
        }
    }

    public static void main(String[] args) {
        try {
            new SingerThread(Integer.MAX_VALUE).startSinging(args);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void startSinging(String[] args) throws InterruptedException {
        int anzahlThreads;
        if (args.length == 0) throw new IllegalArgumentException();
        anzahlThreads = Integer.valueOf(args[0]);
        final Thread[] ts = new Thread[anzahlThreads];
        for (int i = 0; i < anzahlThreads; i++) {
            ts[i] = new SingerThread(i);
            ts[i].start();
        }
        for (Thread t : ts) {
            t.join();
        }
    }
}

