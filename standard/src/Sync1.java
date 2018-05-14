public class Sync1 {
    int x = 0; int y = 0;

    class MyThread1 extends Thread{
        public void run() {
            x = 1;
            System.out.println("y = "+y);
        }
    }

    class MyThread2 extends Thread{
        public void run() {
            y = 1;
            System.out.println("x = "+x);
        }
    }

    public void execute() throws InterruptedException {
        MyThread1 thread1 = new MyThread1();
        MyThread2 thread2 = new MyThread2();
        thread1.start(); thread2.start();
        thread1.join(); thread2.join();
    }

    public static void main(String[] args) {

        for(int x = 0; x < 1000; x++){
            Sync1 sync = new Sync1();
            System.out.println("***************RUNDE " + x + ":");
            try {
                sync.execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}