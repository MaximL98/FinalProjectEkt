package logic;

public class Worker extends SystemUser{
	
	private String role;
	//Worker constructor, inherits fields from the system user. And have his own field "role"
	public Worker(String firstName, String lastName, Integer id, String phoneNumber, String emailAddress,
			String creditCard, String username, String password, String role) 
	{
		super(firstName, lastName, id, phoneNumber, emailAddress, creditCard, username, password);
		this.role = role;
	}

	//getting and setting worker role
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	//toString method that returns worker details
	@Override
	public String toString() {
		return "Worker [role=" + role + ", getFirstName()=" + getFirstName() + ", getLastName()=" + getLastName()
				+ ", getId()=" + getId() + ", getPhoneNumber()=" + getPhoneNumber() + ", getEmailAddress()="
				+ getEmailAddress() + ", getCreditCard()=" + getCreditCard() + ", getUsername()=" + getUsername()
				+ ", getPassword()=" + getPassword() + ", toString()=" + super.toString() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + "]";
	}

	
}
