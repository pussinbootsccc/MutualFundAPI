package model;

import java.util.Arrays;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.Customer;

public class CustomerDAO extends GenericDAO<Customer> {

    public CustomerDAO(String tableName, ConnectionPool cp) throws DAOException {
        super(Customer.class, tableName, cp);
    }

    public Customer[] getCustomers() throws RollbackException {
        Customer[] customers = match();
        Arrays.sort(customers);
        return customers;
    }

    public void add(Customer c) throws RollbackException {
        try {
            Transaction.begin();

            // Create a new ItemBean in the database with the next id number
            create(c);

            Transaction.commit();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
    }

    public void updateCustomer(Customer c) throws RollbackException {
        try {
            Transaction.begin();
            update(c);
            Transaction.commit();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
    }

    public void updateNoT(Customer c) throws RollbackException {
        update(c);
    }

    public Customer getByUserName(String userName) throws RollbackException {
        Customer[] customers;
        customers = match(MatchArg.equals("userName", userName));
        if (customers.length == 0) {
            return null;
        }
        return customers[0];
    }

    public void setPassword(String userName, String newPassword) {
        try {
            Customer c = getByUserName(userName);
            c.setPassword(newPassword);
            updateCustomer(c);
        } catch (RollbackException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Customer[] getAllCustomers() throws RollbackException {
        Customer[] c = match();
        return c;
    }
}
