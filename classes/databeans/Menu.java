package databeans;


public class Menu {
	/**
	 * Set static content for testing purposes.
	 */
	String link = "/createAccount";
	String function = "Create Account";
	public Menu(String l, String f) {
		this.link = l;
		this.function = f;
	}
	
	public String getLink() {
		return this.link;
	}
	public String getFunction() {
		return this.function;
	}
}
