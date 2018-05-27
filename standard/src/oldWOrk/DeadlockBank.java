package oldWOrk;

public class DeadlockBank implements Bank {

    @Override
    public boolean transfer(Account fromAccount, Account toAccount, int money) {
        if (fromAccount.getMoney() >= money) {
            try {
                fromAccount.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fromAccount.setMoney(fromAccount.getMoney() - money);
            try {
                toAccount.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            toAccount.setMoney(toAccount.getMoney() + money);
                    toAccount.unlock();
                    fromAccount.unlock();
            return true;
        }
        return false;
    }
}
