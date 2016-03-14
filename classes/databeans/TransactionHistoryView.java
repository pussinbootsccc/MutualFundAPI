package databeans;

public class TransactionHistoryView {
    // private long transactionId;
    private String date = "-";
    private String type = "-";
    private String fundName = "-";
    private String fundTicker = "-";
    private String price = "-";
    private String shares = "-";
    private String amount = "-";
    private String status = "-";

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
