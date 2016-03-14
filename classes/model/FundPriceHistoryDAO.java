
package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import databeans.FundPriceHistory;

public class FundPriceHistoryDAO extends GenericDAO<FundPriceHistory> {
    public FundPriceHistoryDAO(String tableName, ConnectionPool pool) throws DAOException {
        super(FundPriceHistory.class, tableName, pool);
    }

    // getByFindId() To fetch the price history of a given fundId (sorted by date).
    public List<FundPriceHistory> getPriceHistory(long fundId) throws RollbackException {
        List<FundPriceHistory> priceList = Arrays.asList(match(MatchArg.equals("fundId", fundId)));
        Collections.sort(priceList, new MyComparator());// To sort prices based on date
        return priceList;
    }

    // To fetch the current price of a given fund.
    public FundPriceHistory getCurrentPrice(long fundId) throws RollbackException {
        FundPriceHistory[] priceList = match(MatchArg.equals("fundId", fundId));
        if (priceList.length == 0 || priceList == null)
            return null;
        FundPriceHistory currentPrice = priceList[0];
        for (FundPriceHistory price : priceList) {
            // This date is after the argument date, reset the current price.
            if (price.getPriceDate().compareTo(currentPrice.getPriceDate()) > 0) {
                currentPrice = price;
            }
        }
        return currentPrice;
    }

    public Date getLastTransitionDay() throws RollbackException {
        FundPriceHistory[] priceList = match();
        if (priceList.length == 0 || priceList == null) {
            Date date = new Date();
            date.setTime(1441065600);
            return date;
        } else {
            FundPriceHistory currentPrice = priceList[0];
            Date date = currentPrice.getPriceDate();
            for (FundPriceHistory price : priceList) {
                // This date is after the argument date, reset the current price.
                if (price.getPriceDate().compareTo(currentPrice.getPriceDate()) > 0) {
                    date = price.getPriceDate();
                }
            }
            return date;
        }
    }

    // add all funds price history of the date
    public void UpdateAllFundsPrice(long[] fundIds, long[] prices, Date date) throws RollbackException {
        for (int i = 0; i < prices.length; i++) {
            FundPriceHistory fundPriceHistory = new FundPriceHistory();
            fundPriceHistory.setFundId(fundIds[i]);
            fundPriceHistory.setPrice(prices[i]);
            fundPriceHistory.setPriceDate(date);
            create(fundPriceHistory);
        }
    }

    // To specify the sorting criteria by date, from early to later.
    private static class MyComparator implements Comparator<FundPriceHistory> {
        @Override
        public int compare(FundPriceHistory p1, FundPriceHistory p2) {
            return p1.getPriceDate().compareTo(p2.getPriceDate());
        }
    }

}
