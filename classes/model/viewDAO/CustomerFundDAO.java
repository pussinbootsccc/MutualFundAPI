package model.viewDAO;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericViewDAO;
import org.genericdao.RollbackException;

import databeans.CustomerFund;
import util.Common;

public class CustomerFundDAO extends GenericViewDAO<CustomerFund> {

    public CustomerFundDAO(Class<CustomerFund> beanClass, ConnectionPool connectionPool) throws DAOException {
        super(beanClass, connectionPool);
        // TODO Auto-generated constructor stub
    }

    public CustomerFund[] getCustomerFund(long customerId) throws RollbackException {

        String sql = "select position.fundId, position.customerId, fund.fundName,"
                + " fund.fundTicker, fund.fundPrice as fundPrice,"
                + " position.shares as shares, fundPrice * shares as total"
                + " from position,fund where fund.fundId = position.fundId and customerId =" + customerId + ";";

        CustomerFund[] cf = executeQuery(sql);
        for (int i = 0; i < cf.length; i++) {
            double tmp = (double) cf[i].getTotal() / 1000; // Calculation is incorrect here, should be /(100*1000)
            long total = (long) (Double.valueOf(Common.convertDoublePriceToString(tmp)) * 100);
            cf[i].setTotal(total);
        }
        return cf;
    }
}
