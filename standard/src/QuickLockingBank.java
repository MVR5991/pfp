import java.util.concurrent.locks.ReentrantLock;

public class QuickLockingBank implements Bank {

    @Override
    public boolean transfer(Account fromAccount, Account toAccount, int money) {
        if (fromAccount.getMoney() >= money) {
            try {
                fromAccount.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fromAccount.setMoney(fromAccount.getMoney() - money);
            fromAccount.unlock();
            try {
                toAccount.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            toAccount.setMoney(toAccount.getMoney() + money);
            toAccount.unlock();
            return true;
        }
        return false;

    }

}


