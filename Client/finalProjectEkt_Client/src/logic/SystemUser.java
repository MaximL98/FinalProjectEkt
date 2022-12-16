package logic;
/**
 * Project Name: finalProjectEkt_Client
 * Logic class that contains the details needed to save up for each system user.
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class SystemUser {
	/**
	 *system user logic part.
	 *all the following details, will be saved for each system user
	 */
	private String firstName;
	private String lastName;
	private Integer id;
	private String phoneNumber;
	private String emailAddress;
	private String creditCard;
	private String username;
	private String password;
	
	/**
	 * system user constructor
	 * @param String firstName
	 * @param String lastName
	 * @param Integer id
	 * @param String phoneNumber
	 * @param String emailAddress
	 * @param String creditCard
	 * @param String username
	 * @param String password
	 */
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
	/**
	 * getting user first name
	 * @return String user first name
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * setting user first name
	 * @param String firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * getting user last name
	 * @return String user last name
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * setting user last name
	 * @param String lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * getting user id
	 * @return Integer user id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * setting user id
	 * @param Integer id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * getting user phone number
	 * @return String user phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * setting user phone number
	 * @param String phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * getting user email address
	 * @return String user email address
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * setting user email address
	 * @param String emailAddress
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * getting user credit card number 
	 * @return String credit card number
	 */
	public String getCreditCard() {
		return creditCard;
	}
	/**
	 * setting user credit card number
	 * @param String credit Card number
	 */
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}
	/**
	 * getting user username
	 * @return String username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * setting user username
	 * @param String username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * getting user password
	 * @return String password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * setting user password
	 * @param String password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	//toString method that returns system user details
	@Override
	/**
	 * @override
	 * toString method, returns system user details
	 */
	public String toString() {
		return "SystemUser [firstName=" + firstName + ", lastName=" + lastName + ", id=" + id + ", phoneNumber="
				+ phoneNumber + ", emailAddress=" + emailAddress + ", creditCard=" + creditCard + ", username="
				+ username + ", password=" + password + "]";
	}
	
	
	
}
