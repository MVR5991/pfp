public class DeadlockBank implements Bank {

    @Override
    public boolean transfer(Account fromAccount, Account toAccount, int money) {
        if (fromAccount.getMoney() >= money) {
            synchronized (toAccount){
                fromAccount.setMoney(fromAccount.getMoney() - money);
                synchronized (fromAccount){
                    toAccount.setMoney(toAccount.getMoney() + money);
                }
            }
            return true;
        }
        return false;
    }
}
