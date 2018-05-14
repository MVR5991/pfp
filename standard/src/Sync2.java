public class Sync2 {
    Object lock = new Object();
    public int counter = 0;

    class MyThread1 extends Thread{
        public void run() {
            Object myLock = lock;
            for (int i = 0; i < 2; i++) {
                synchronized(myLock) {
                    counter++;
                    myLock = new Object();
                }
            }
        }
    }

    class MyThread2 extends Thread{
        public void run() {
            synchronized(lock) {
                counter++;
            }
        }
    }

    public void output() {
        if(counter == 1){
            System.out.println("counter = " + counter);
            System.exit(1);
        }
    }

    public void execute() throws InterruptedException {
        MyThread1 thread1 = new MyThread1();
        MyThread2 thread2 = new MyThread2();
        thread1.start(); thread2.start();
        thread1.join(); thread2.join();
        output();
    }

    public static void main(String[] args) {

        for(long x = 0; x < 99999999999999l; x++){
            Sync2 sync = new Sync2();
//            System.out.println("***************RUNDE " + x + ":");

            try {
                sync.execute();
//                if ( sync.counter != 3) System.err.println("*************************************"); System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}