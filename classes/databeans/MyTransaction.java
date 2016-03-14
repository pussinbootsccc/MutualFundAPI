package databeans;

import java.util.Date;

import org.genericdao.PrimaryKey;

@PrimaryKey("transactionId")
public class MyTransaction {
    static public final int SELL_FUND = 1;
    static public final int BUY_FUND = 2;
    static public final int DEPOSIT_CHECK = 3;
    static public final int REQUEST_CHECK = 4;

    private long transactionId;
    private long customerId;
    private long fundId;

    private Date executeDate;
    private long shares;

    private long price;
    /**
     * transactionType: 1: sell 2: buy 3: deposit check 4: request check
     */
    private int transactionType;
    private long amount;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getFundId() {
        return fundId;
    }

    public void setFundId(long fundId) {
        this.fundId = fundId;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }

    public long getShares() {
        return shares;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
