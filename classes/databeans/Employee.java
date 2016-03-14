package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("employeeId")
public class Employee {
	private long employeeId;
	private String userName;
	private String firstName;
	private String lastName;
    private String password;

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getEmployeeId() {
		return employeeId;
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
