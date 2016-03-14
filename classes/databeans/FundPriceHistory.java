package databeans;

import java.util.Date;

import org.genericdao.PrimaryKey;

@PrimaryKey("id")
public class FundPriceHistory {
    private long id;
    private long fundId;
    private Date priceDate;
    private long price;

    public long getId() {
        return id;
    }

    public long getFundId() {
        return fundId;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public long getPrice() {
        return price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFundId(long fundId) {
        this.fundId = fundId;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
