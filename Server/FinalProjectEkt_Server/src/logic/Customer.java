package logic;

//import java.io.Serializable;
/**
 * Project Name: finalProjectEkt_Server
 * Logic class that contains the details needed to save up for each customer.
 * this class extends the other logic class "SystemUser"
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Customer extends SystemUser{
	
	//private static final long serialVersionUID = 1L;
	
	/**
	 * at the beginning customer subscriber number is null 
	 */
	private String subscriberNumber = null;

	/**
	 * Customer constructor, inherits fields from system user
	 *
	 * @param customer first name
	 * @param customer last name
	 * @param customer id
	 * @param customer phone number
	 * @param customer email address
	 * @param customer credit card
	 * @param customer "username"
	 * @param customer password
	 */
	public Customer(String firstName, String lastName, Integer id, String phoneNumber, String emailAddress,
			String creditCard, String username, String password) {
		super(firstName, lastName, id, phoneNumber, emailAddress, creditCard, username, password);
	}
	
	
	/**
	 * getting customer subscription number
	 * @return customer subscription number
	 */
	public String getSubscriberNumber() {return subscriberNumber;}
	/**
	 * setting customer subscription number
	 * @param subscriberNumber
	 */
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber; 
	}
	
	/**
	 * returns if the customer is a subscriber or not
	 * @return true or false
	 */
	public boolean isSubscriber() {return subscriberNumber != null;}
	/**
	 * toString method, returns customer details
	 */
	@Override
	public String toString() {
		return "Customer [subscriberNumber=" + subscriberNumber + ", getSubscriberNumber()=" + getSubscriberNumber()
				+ ", isSubscriber()=" + isSubscriber() + ", getFirstName()=" + getFirstName() + ", getLastName()="
				+ getLastName() + ", getId()=" + getId() + ", getPhoneNumber()=" + getPhoneNumber()
				+ ", getEmailAddress()=" + getEmailAddress() + ", getCreditCard()=" + getCreditCard()
				+ ", getUsername()=" + getUsername() + ", getPassword()=" + getPassword() + ", toString()="
				+ super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}

	
	
	
}
