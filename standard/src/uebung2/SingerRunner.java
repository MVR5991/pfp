package uebung2;

public class SingerRunner implements Runnable {

    private int THREADNUMMER;

    public SingerRunner(int threadnummer) {
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
        int anzahlThreads;

        if (args.length == 0) throw new IllegalArgumentException();
        anzahlThreads = Integer.valueOf(args[0]);

        for (int x = anzahlThreads; x >= 0; x--) {
            new Thread(new SingerRunner(x)).start();
        }
    }
}
