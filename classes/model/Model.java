package model;

import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;

import databeans.CustomerFund;
import model.viewDAO.CustomerFundDAO;

public class Model {
    private EmployeeDAO employeeDAO;
    private CustomerDAO customerDAO;
    private TransactionDAO transactionDAO;
    private FundDAO fundDAO;
    private PositionDAO positionDAO;
    private FundPriceHistoryDAO priceDAO;
    private CustomerFundDAO customerFundDAO;

    public Model() throws ServletException {
        try {
            String jdbcDriver = "com.mysql.jdbc.Driver";
            String jdbcURL = "jdbc:mysql:///test";
            ConnectionPool pool = new ConnectionPool(jdbcDriver, jdbcURL);

            customerDAO = new CustomerDAO("customer", pool);
            employeeDAO = new EmployeeDAO("employee", pool);
            fundDAO = new FundDAO("fund", pool);
            priceDAO = new FundPriceHistoryDAO("fundPriceHistory", pool);
            positionDAO = new PositionDAO("position", pool);
            transactionDAO = new TransactionDAO("transaction", pool);
            customerFundDAO = new CustomerFundDAO(CustomerFund.class, pool);

        } catch (DAOException e) {
            throw new ServletException(e);
        }

    }

    public CustomerFundDAO getCustomerFundDAO() {
        return customerFundDAO;
    }

    public CustomerDAO getCustomerDAO() {
        // TODO Auto-generated method stub
        return customerDAO;
    }

    public EmployeeDAO getEmployeeDAO() {
        // TODO Auto-generated method stub
        return employeeDAO;
    }

    public PositionDAO getPositionDAO() {
        // TODO Auto-generated method stub
        return positionDAO;
    }

    public FundDAO getFundDAO() {
        // TODO Auto-generated method stub
        return fundDAO;
    }

    public FundPriceHistoryDAO getFundPriceHistoryDAO() {
        // TODO Auto-generated method stub
        return priceDAO;
    }

    public TransactionDAO getTransactionDAO() {
        // TODO Auto-generated method stub
        return transactionDAO;
    }
}
