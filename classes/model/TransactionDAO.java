package model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.MyTransaction;

public class TransactionDAO extends GenericDAO<MyTransaction> {
    public TransactionDAO(String tableName, ConnectionPool cp) throws DAOException {
        super(MyTransaction.class, tableName, cp);
    }

    public void add(MyTransaction t) throws RollbackException {
        try {
            Transaction.begin();

            // Create a new ItemBean in the database with the next id number
            create(t);

            Transaction.commit();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
    }

    public void addNoT(MyTransaction t) throws RollbackException {
        // Create a new ItemBean in the database with the next id number
        create(t);
    }

    public List<MyTransaction> getAllTransactionsByCustomer(long customerId) throws RollbackException {
        MyTransaction[] transactions = match(MatchArg.equals("customerId", customerId));
        return Arrays.asList(transactions);
    }

    /**
     * @param customerId
     * @return The last date transaction of this customer;
     * @throws RollbackException
     */
    public MyTransaction getLastTransactionByCustomer(long customerId) throws RollbackException {
        MyTransaction[] transactions = match(MatchArg.equals("customerId", customerId));
        for (int i = transactions.length - 1; i >= 0; i--) {
            if (transactions[i].getExecuteDate() != null)
                return transactions[i];
        }
        return null;
    }

    /**
     * @return Date Last Transaction day
     * @throws RollbackException
     */
    public Date getLastTransactionDay() throws RollbackException {
        MyTransaction[] transactions = match(MatchArg.notEquals("executeDate", null));
        for (int i = transactions.length - 1; i >= 0; i--) {
            if (transactions[i].getExecuteDate() != null)
                return transactions[i].getExecuteDate();
        }
        return null;
    }

    /**
     * @return MyTransaction[] all pending transactions
     * @throws RollbackException
     */
    public MyTransaction[] getPendingTransactions() throws RollbackException {
//        String sql = "select * from transaction where executeDate is NULL;";
//        // MyTransaction[] all = executeQuery(sql);
        MyTransaction[] all = match(MatchArg.equals("executeDate", null));
        return all;
    }

    public long getPendingAmountByCustomerId(long customerId) throws RollbackException {
        MyTransaction[] transactions = match(
                MatchArg.and(MatchArg.equals("customerId", customerId), MatchArg.equals("executeDate", null)));
        long pendings = 0;
        for (MyTransaction transaction : transactions) {
            if (transaction.getTransactionType() == MyTransaction.BUY_FUND
                    || transaction.getTransactionType() == MyTransaction.REQUEST_CHECK 
                    
                    ) {
                pendings += transaction.getAmount();
            }
        }
        return pendings;
    }
    
   // Add deposit check as pending amount to fit task 8 assumption
    public long getPendingDepositAmountByCustomerId(long customerId) throws RollbackException {
        MyTransaction[] transactions = match(
                MatchArg.and(MatchArg.equals("customerId", customerId), MatchArg.equals("executeDate", null)));
        long pendings = 0;
        for (MyTransaction transaction : transactions) {
            if (transaction.getTransactionType() == MyTransaction.DEPOSIT_CHECK) {
                pendings += transaction.getAmount();
            }
        }
        return pendings;
    }

}
