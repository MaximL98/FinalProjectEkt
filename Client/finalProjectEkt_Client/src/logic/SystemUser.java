package logic;

public class SystemUser {
	
	//system user logic part, 
	//all the following details 
	//will be saved for each system user
	private String firstName;
	private String lastName;
	private Integer id;
	private String phoneNumber;
	private String emailAddress;
	private String creditCard;
	private String username;
	private String password;
	
	
	//system user constructor
	public SystemUser(String firstName, String lastName, Integer id, String phoneNumber, String emailAddress,
			String creditCard, String username, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.creditCard = creditCard;
		this.username = username;
		this.password = password;
	}

	//getting and setting user first name
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	//getting and setting user last name
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	//getting and setting user id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	//getting and setting user phone number
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	//getting and setting user email address
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	//getting and setting user credit card number
	public String getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	//getting and setting user username
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	//getting and setting user password
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//toString method that returns system user details
	@Override
	public String toString() {
		return "SystemUser [firstName=" + firstName + ", lastName=" + lastName + ", id=" + id + ", phoneNumber="
				+ phoneNumber + ", emailAddress=" + emailAddress + ", creditCard=" + creditCard + ", username="
				+ username + ", password=" + password + "]";
	}
	
	
	
}
