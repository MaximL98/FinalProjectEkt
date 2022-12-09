package logic;

import java.io.Serializable;

public class Customer implements Serializable{

	private static final long serialVersionUID = 1L;
	//updated costumer logic part, 
	private String firstName;
	private String lastName;
	private Integer id;
	private String phoneNumber;
	private String emailAddress;
	private String creditCard;
	//if customer don't have subscriber number
	private String subscriberNumber = null;

	
	public Customer(String firstName, String lastName, Integer id, String phoneNumber, String emailAddress,
			String creditCard) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.creditCard = creditCard;
		//i guess this field don't have to be in the constructor
		//this.subscriberNumber = subscriberNumber;
	}

	public String getFirstName() {return firstName;}

	public void setFirstName(String firstName) {this.firstName = firstName;}

	public String getLastName() {return lastName;}

	public void setLastName(String lastName) {this.lastName = lastName;}

	public Integer getId() {return id;}

	public void setId(Integer id) {this.id = id;}

	public String getPhoneNumber() {return phoneNumber;}

	public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

	public String getEmailAddress() {return emailAddress;}

	public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}

	public String getCreditCard() {return creditCard;}

	public void setCreditCard(String creditCard) {this.creditCard = creditCard;}

	public String getSubscriberNumber() {return subscriberNumber;}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber; 
	}
	
	// just for convenience
	public boolean isSubscriber() {return subscriberNumber != null;}

	@Override
	public String toString() {
		return "Customer [first Name = " + firstName + ", last Name = " + lastName + ", id= " + id + ", phoneNumber = "
				+ phoneNumber + ", emailAddress = " + emailAddress + ", creditCard = " + creditCard + ", subscriberNumber = "
				+ subscriberNumber + "]";
	}
	
	
}
