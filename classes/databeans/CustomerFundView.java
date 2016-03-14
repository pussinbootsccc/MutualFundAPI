package databeans;

public class CustomerFundView {
    private String name;
    // Ask JSON to ignore this field though default is 0
    private transient long fundId;
    // JSON will ignore if not set since default is null
    private String symbol;
    private String price;
    private String shares;
    private String total;

    public String getFundName() {
        return name;
    }

    public void setFundName(String fundName) {
        this.name = fundName;
    }

    public long getFundId() {
        return fundId;
    }

    public void setFundId(long fundId) {
        this.fundId = fundId;
    }

    public String getFundSymbol() {
        return symbol;
    }

    public void setFundSymbol(String fundTicker) {
        this.symbol = fundTicker;
    }

    public String getFundPrice() {
        return price;
    }

    public void setFundPrice(String fundPrice) {
        this.price = fundPrice;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
