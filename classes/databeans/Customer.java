package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("customerId")
public class Customer {
    private long customerId;
    private String userName;
    private String firstName;
    private String lastName;
    private String addr_line1;
    private String addr_line2;
    private String city;
    private String state;
    private String zip;
    private long cash = 0;
    private String  password;


    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getAddr_line1() {
        return addr_line1;
    }
    public void setAddr_line1(String addr_line1) {
        this.addr_line1 = addr_line1;
    }
    public String getAddr_line2() {
        return addr_line2;
    }
    public void setAddr_line2(String addr_line2) {
        this.addr_line2 = addr_line2;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public long getCash() {
        return cash;
    }
    public void setCash(long cash) {
        this.cash = cash;
    }

}
