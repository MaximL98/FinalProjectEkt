package logic;

/**
 * Role is an enum that designates the role of a user
 * TODO:
 * add role field to the table we need to make (table: username, password, id, role, status) [tbd]
 * @author Rotem
 *
 */

public enum Role {
	CUSTOMER, REGIONAL_MANAGER, LOGISTICS_MANAGER, SERVICE_REPRESENTATIVE, CEO, DIVISION_MANAGER;
	
	// get role object from string
	public static Role getRoleFromString(String roleAsString) {
		return Role.valueOf(roleAsString.toUpperCase());
	}

	
}
