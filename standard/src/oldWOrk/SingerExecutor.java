package oldWOrk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class SingerExecutor {

    public static void main(String[] args) throws InterruptedException {

        if (args.length == 0) throw new IllegalArgumentException();
        int anzahlThreads = Integer.valueOf(args[0]);
        ExecutorService es = Executors.newFixedThreadPool(anzahlThreads);
        for (int x = 0; x < anzahlThreads; x++) {
            es.execute(new SingerRunner(x));
        }
        es.shutdown();
        es.awaitTermination(30, TimeUnit.MINUTES);
    }

}
