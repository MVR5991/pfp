package oldWOrk;

import java.util.Random;

public class Finance {

    private static final Random rng = new Random(42);


    private static void transferRandomMoney(final int threadId, final Bank bank, final Account from,
                                            final Account to) {

        final int money = rng.nextInt(200) + 1;
        if (from != to) {
            if (bank.transfer(from, to, money)) {
                System.out.printf("[%d] Transfer from %s to %s successful%n", threadId, from, to);
            } else {
                System.out.printf("[%d] Transfer from %s to %s failed%n", threadId, from, to);
            }
        }
    }


    private static void runTransfers(final Bank bank, final int numThreads) {

        final Account[] accounts = new Account[numThreads];
        for (int i = 0; i < numThreads; ++i) {
            accounts[i] = new Account(rng.nextInt(150) + 50);
        }

        final TransferThread threads[] = new TransferThread[numThreads];
        for (int x = 0; x < numThreads; x++) {
            final TransferThread thread;

            if (x == numThreads - 1) {
                thread = new TransferThread(accounts[x], accounts[0], bank);
            } else {
                thread = new TransferThread(accounts[x], accounts[x + 1], bank);
            }
            threads[x] = thread;
            thread.start();
        }
        for(TransferThread thready : threads){
            try {
                thready.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
//        runTransfers(new oldWOrk.DeadlockBank(), 3);
//        runTransfers(new oldWOrk.QuickLockingBank(), 5);
//        runTransfers(new oldWOrk.OrderedBank(), 7);
        runTransfers(new ManagedBank(),90);
    }


    protected static class TransferThread extends Thread {
        private Account quellKonto;
        private Account zielKonto;
        private Bank bank;

        TransferThread(Account quellKonto, Account zielKonto, Bank bank) {
            this.quellKonto = quellKonto;
            this.zielKonto = zielKonto;
            this.bank = bank;
        }

        @Override
        public void run() {
            while(true){
                transferRandomMoney(quellKonto.getId(),bank,quellKonto, zielKonto );
            }

        }
    }
}

