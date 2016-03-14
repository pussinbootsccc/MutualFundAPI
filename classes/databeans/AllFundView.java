package databeans;

public class AllFundView {
    private String fundName;
    private long fundId;
    private String fundTicker;
    private String fundPrice;

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public long getFundId() {
        return fundId;
    }

    public void setFundId(long fundId) {
        this.fundId = fundId;
    }

    public String getFundTicker() {
        return fundTicker;
    }

    public void setFundTicker(String fundTicker) {
        this.fundTicker = fundTicker;
    }

    public String getFundPrice() {
        return fundPrice;
    }

    public void setFundPrice(String fundPrice) {
        this.fundPrice = fundPrice;
    }
}
