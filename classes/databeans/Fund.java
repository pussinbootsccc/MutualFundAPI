package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("fundId")
public class Fund {
    private long fundId;
    private String fundName;
    private String fundTicker;
    private long fundPrice;

    public long getFundId() {
        return fundId;
    }

    public String getFundName() {
        return fundName;
    }

    public long getFundPrice() {
        return fundPrice;
    }

    public void setFundPrice(long fundPrice) {
        this.fundPrice = fundPrice;
    }

    public String getFundTicker() {
        return fundTicker;
    }

    public void setFundId(long fundId) {
        this.fundId = fundId;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public void setFundTicker(String fundTicker) {
        this.fundTicker = fundTicker;
    }

}
