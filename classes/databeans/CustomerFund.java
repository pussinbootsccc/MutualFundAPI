package databeans;

public class CustomerFund {
    private long fundId;
    private long customerId;
    private String fundName;
    private String fundTicker;
    private long fundPrice;
    private long shares;
    private long total;

    public long getFundId() {
        return fundId;
    }

    public void setFundId(long fundId) {
        this.fundId = fundId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundTicker() {
        return fundTicker;
    }

    public void setFundTicker(String fundTicker) {
        this.fundTicker = fundTicker;
    }

    public long getFundPrice() {
        return fundPrice;
    }

    public void setFundPrice(long fundPrice) {
        this.fundPrice = fundPrice;
    }

    public long getShares() {
        return shares;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
