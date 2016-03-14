package databeans;

/**
 * View bean, used to save the history info of a specific fund;
 * @author Zhihao Ji
 */
public class FundHistoryView {
    private String date;
    private String price;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
