package logic;

//import java.io.Serializable;

public class Customer extends SystemUser{

	//private static final long serialVersionUID = 1L;
		
	//if customer don't have subscriber number
	private String subscriberNumber = null;

	//customer constructor, inherits fields from system user
	public Customer(String firstName, String lastName, Integer id, String phoneNumber, String emailAddress,
			String creditCard, String username, String password) {
		super(firstName, lastName, id, phoneNumber, emailAddress, creditCard, username, password);
	}

	//getting and setting customer subscription number
	public String getSubscriberNumber() {return subscriberNumber;}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber; 
	}
	
	//if he is a subscriber return the number
	public boolean isSubscriber() {return subscriberNumber != null;}

	//toString method that returns customer details
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
