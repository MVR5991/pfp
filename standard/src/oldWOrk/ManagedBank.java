package oldWOrk;

public class ManagedBank implements Bank {
    private Cashier cashier = new Cashier();


    @Override
    public boolean transfer(Account fromAccount, Account toAccount, int money) {
        int countInquieries = 0;
        if (fromAccount.getMoney() >= money) {
            while (!cashier.checkPermission(fromAccount, toAccount, countInquieries)) {
                countInquieries++;
                System.err.println("***************************" + countInquieries);
            }
                doTransfer(fromAccount, toAccount, money);
            cashier.transactionDone(fromAccount, toAccount);
            return true;
        } else {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private void doTransfer(Account fromAccount, Account toAccount, int money)  {
        try {
            fromAccount.lock();
            fromAccount.setMoney(fromAccount.getMoney() - money);
            toAccount.lock();
            toAccount.setMoney(toAccount.getMoney() + money);
            toAccount.unlock();
            fromAccount.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
