public class OrderedBank implements Bank {

    @Override
    public boolean transfer(Account fromAccount, Account toAccount, int money) {
        if (fromAccount.getMoney() >= money) {
            if (fromAccount.getId() < toAccount.getId()) {
                transferMoney(fromAccount, toAccount, money);
            } else {
                transferMoney(toAccount, fromAccount, money);
            }
            return true;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void transferMoney(final Account acc1, final Account acc2, final int money) {
        synchronized (acc1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            acc1.setMoney(acc1.getMoney() - money);
            synchronized (acc2) {
                acc2.setMoney(acc2.getMoney() + money);
            }
        }
    }
}
