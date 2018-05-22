import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Cashier {
    final ConcurrentHashMap<Account, Account> currentTransaction = new ConcurrentHashMap<>();
    final ConcurrentHashMap<Long, Transaction> transactionsInProgress = new ConcurrentHashMap<>();

    public synchronized boolean checkPermissionWithObject(Account fromAccount, Account toAccount, int countInquieries) {
        if(countInquieries > 10){
            forceTransactionWithObject(fromAccount, toAccount);
            return true;
        }
        if (isAccountBeingUsedForTransactionWithTransaktionObject(fromAccount, toAccount)) {
            return false;
        } else {
            transactionsInProgress.put(Thread.currentThread().getId(), new Transaction(fromAccount,toAccount));
            System.out.println("Anzahl Transaktionen: " + transactionsInProgress.size());
            return true;
        }
    }

    public boolean checkPermission(Account fromAccount, Account toAccount, int countInquieries) {
        if(countInquieries > 50){
            forceTransaction(fromAccount, toAccount);
            return true;
        }
        if (isAccountBeingUsedForTransaction(fromAccount, toAccount)) {
            return false;
        } else {
            currentTransaction.put(fromAccount, toAccount);
            System.out.println("Anzahl Transaktionen: " + currentTransaction.size());
            return true;
        }
    }

    private void forceTransactionWithObject(Account fromAccount, Account toAccount) {
        Iterator it = transactionsInProgress.entrySet().iterator();
        Long threadid = null;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Transaction value = (Transaction) pair.getValue();
            if (value.getFromAccount().equals(fromAccount) || value.getFromAccount().equals(toAccount) || value.getToAccount().equals(fromAccount )|| value.getToAccount().equals(toAccount)){
                threadid = (Long) pair.getKey();
            }
        }
        if (threadid != null){
            Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();

            for(Thread thread : setOfThread){
                if(thread.getId()== threadid){
                    try {
                        thread.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void forceTransaction(Account fromAccount, Account toAccount) {
        if(isAccountBeingUsedForTransaction(fromAccount, toAccount)){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isAccountBeingUsedForTransaction(fromAccount,toAccount)){
                fromAccount.unlock();
                toAccount.unlock();
                transactionDone(fromAccount, toAccount);
            }
        }
    }

    private boolean isAccountBeingUsedForTransactionWithTransaktionObject(Account fromAccount, Account toAccount) {
        Iterator it = transactionsInProgress.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Transaction value = (Transaction) pair.getValue();
            if (value.getFromAccount().equals(fromAccount) || value.getFromAccount().equals(toAccount) || value.getToAccount().equals(fromAccount )|| value.getToAccount().equals(toAccount)){
                return true;
            }
        }
        return false;
    }

    private boolean isAccountBeingUsedForTransaction(Account fromAccount, Account toAccount) {
        Iterator it = currentTransaction.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getKey() == fromAccount || pair.getKey() == toAccount || pair.getValue() == fromAccount || pair.getValue() == toAccount){
                return true;
            }
        }
        return false;
    }

    public void transactionDone(Account fromAccount, Account toAccount) {
        currentTransaction.remove(fromAccount, toAccount);
    }

    public void transactionDoneWithObject(Account fromAccount, Account toAccount) {
        transactionsInProgress.remove(Thread.currentThread().getId(), new Transaction(fromAccount, toAccount));
    }

}
class Transaction{
    private Account fromAccount;
    private Account toAccount;

    @Override
    public int hashCode() {

        return Objects.hash(fromAccount, toAccount);
    }

    Transaction(Account fromAccount, Account toAccount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(fromAccount, that.fromAccount) &&
                Objects.equals(toAccount, that.toAccount);
    }
}
