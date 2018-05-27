package oldWOrk;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cashier {
    final ConcurrentHashMap<Account, Account> currentTransaction = new ConcurrentHashMap<>();

    public synchronized boolean checkPermission(Account fromAccount, Account toAccount, int countInquieries) {
//        nicht benoetiegt, Uebungsangabe ein wenig unklar an dieser Stelle
//        if(countInquieries > 50){
//            forceTransaction(fromAccount, toAccount);
//            return true;
//        }
        if (isAccountBeingUsedForTransaction(fromAccount, toAccount)) {
            return false;
        } else {
            currentTransaction.put(fromAccount, toAccount);
            System.out.println("Anzahl Transaktionen: " + currentTransaction.size());
            return true;
        }
    }

//    private void forceTransaction(oldWOrk.Account fromAccount, oldWOrk.Account toAccount) {
//        if(isAccountBeingUsedForTransaction(fromAccount, toAccount)){
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if(isAccountBeingUsedForTransaction(fromAccount,toAccount)){
//                fromAccount.unlock();
//                toAccount.unlock();
//                transactionDone(fromAccount, toAccount);
//            }
//        }
//    }


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

}
