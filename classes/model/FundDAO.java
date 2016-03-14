
package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.Fund;

public class FundDAO extends GenericDAO<Fund> {

    public FundDAO(String tableName, ConnectionPool pool) throws DAOException {
        super(Fund.class, tableName, pool);
    }

    // To fetch a list of all funds
    public Fund[] getAllFunds() throws RollbackException {
        Fund[] fundsList = match();
        return fundsList;
    }

    public Fund getById(long fundId) throws RollbackException {
        Fund fund = read(fundId);
        return fund;
    }

    // To fetch a specific fund by the given fund ticker. ignoreCase added.
    public Fund getByTicker(String ticker) throws RollbackException {
        Fund[] fundList = match(MatchArg.equalsIgnoreCase("fundTicker", ticker));

        if (fundList.length != 0) {
            return fundList[0];
        } else {
            return null;
        }
    }

    // To fetch a specific fund by the given fund name. ignoreCase added.
    public Fund getByName(String name) throws RollbackException {
        Fund[] fundList = match(MatchArg.equalsIgnoreCase("fundName", name));

        if (fundList.length != 0) {
            return fundList[0];
        } else {
            return null;
        }
    }

    // To implement fuzzy search when provided a keyword.
    public Fund[] getByKeyword(String keyword) throws RollbackException {
        Fund[] fundList = match(MatchArg.or(MatchArg.containsIgnoreCase("fundName", keyword),
                MatchArg.containsIgnoreCase("fundTicker", keyword)));
        return fundList;
    }

    // To create a new fund.
    public void createFund(Fund fund) throws RollbackException {
        try {
            Transaction.begin();
            if (getByTicker(fund.getFundTicker()) != null) {
                throw new RollbackException("This ticker already exists");
            }
            create(fund);
            Transaction.commit();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
    }

    public void updateAllFundPrices(long[] fundIds, long[] fundPrices) throws RollbackException {
        for (int i = 0; i < fundIds.length; i++) {
            Fund fund = read(fundIds[i]);
            fund.setFundPrice(fundPrices[i]);
            update(fund);
        }
    }

    // Get the number of all funds
    public int getFundNumbers() throws RollbackException {
        Fund[] funds = match();
        if (funds.length == 0) {
            return 0;
        } else {
            return funds.length;
        }
    }

}
