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
	 * @param firstName
	 * @param lastName
	 * @param id
	 * @param phoneNumber
	 * @param emailAddress
	 * @param creditCard
	 * @param username
	 * @param password
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
	 * @return user first name
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * setting user first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * getting user last name
	 * @return user last name
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * setting user last name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * getting user id
	 * @return user id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * setting user id
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * getting user phone number
	 * @return user phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * setting user phone number
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * getting user email address
	 * @return user email address
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * setting user email address
	 * @param emailAddress
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * getting user credit card number 
	 * @return credit card number
	 */
	public String getCreditCard() {
		return creditCard;
	}
	/**
	 * setting user credit card number
	 * @param credit Card number
	 */
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}
	/**
	 * getting user username
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * setting user username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * getting user password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * setting user password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	/**
	 * toString method, returns system user details
	 */
	@Override
	public String toString() {
		return "SystemUser [firstName=" + firstName + ", lastName=" + lastName + ", id=" + id + ", phoneNumber="
				+ phoneNumber + ", emailAddress=" + emailAddress + ", creditCard=" + creditCard + ", username="
				+ username + ", password=" + password + "]";
	}
	
	
	
}
