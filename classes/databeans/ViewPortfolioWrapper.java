package databeans;

public class ViewPortfolioWrapper {
	public CustomerFundView[] Funds;
	private String cash;
	private String Message;
	public ViewPortfolioWrapper() {
		// JSON will ignore null fields
		this.Funds = null;
		this.cash = null;
		this.Message = null;
	}
	public String getCash() {
		return this.cash;
	}
	public String getMessage() {
		return Message;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	public void setMessage(String message) {
		this.Message = message;
	}
}
