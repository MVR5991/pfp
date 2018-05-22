import java.util.concurrent.locks.ReentrantLock;

public class QuickLockingBank implements Bank {
    ReentrantLock lock = new ReentrantLock();
    ReentrantLock lock2 = new ReentrantLock();

    @Override
    public boolean transfer(Account fromAccount, Account toAccount, int money) {
        if (fromAccount.getMoney() >= money) {
            lock.lock();
            synchronized (toAccount) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fromAccount.setMoney(fromAccount.getMoney() - money);
                lock.unlock();
            }
            lock2.lock();
            synchronized (fromAccount) {
                toAccount.setMoney(toAccount.getMoney() + money);
                lock2.unlock();
            }
            return true;
        }
        return false;

    }

}

