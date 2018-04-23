

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SingerExecutor {

    public static void main(String[] args) {

        if (args.length == 0) throw new IllegalArgumentException();

        ExecutorService es = Executors.newFixedThreadPool(Integer.valueOf(args[0]));
        for (int x = Integer.valueOf(args[0]); x >= 0; x--) {
            es.submit(new SingerRunner(x));
        }
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
