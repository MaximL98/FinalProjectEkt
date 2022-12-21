package logic;
/**
 * Project Name: finalProjectEkt_Client
 * Logic class that contains the details needed to save up for each worker.
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Worker extends SystemUser{
	/**
	 * worker logic part.
	 * private field that will contain worker role in the system
	 */
	private String role;
	/**
	 * Worker constructor, inherits fields from the system user. And have his own field "role"
	 * @param firstName
	 * @param  lastName
	 * @param id
	 * @param phoneNumber
	 * @param emailAddress
	 * @param creditCard
	 * @param username
	 * @param password
	 * @param role
	 */
	public Worker(String firstName, String lastName, Integer id, String phoneNumber, String emailAddress,
			String creditCard, String username, String password, String role) 
	{
		super(firstName, lastName, id, phoneNumber, emailAddress, creditCard, username, password);
		this.role = role;
	}

	/**
	 * getting worker role
	 * @return role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * setting worker role
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	
	/**
	 * toString method, returns worker details
	 */	
	@Override
	public String toString() {
		return "Worker [role=" + role + ", first name =" + getFirstName() + ", last name =" + getLastName()
				+ ", id =" + getId() + ", phone number =" + getPhoneNumber() + ", email address ="
				+ getEmailAddress() + ", credit card=" + getCreditCard() + ", username=" + getUsername()
				+ ", password" + getPassword() + "]";
	}

	
}
