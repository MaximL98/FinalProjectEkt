package common;


// this should be extended to support many operations easily
// I think it should be a matrix-like enumeration:
// example: 1, 2 -> 1=customer, 2=edit
// 			4, 1 -> 4=product , 1=add
// as in separate the object and the operation into two enums, so that I don't have to write a book here.


public enum ServerClientRequestTypes {
	// special messages:
	CONNECT_TO_SERVER, LOGIN, LOGOUT, ERROR_MESSAGE, ACK, CRASH,
	// database-related messages
	CREATE, UPDATE, REMOVE, ADD, DISPLAY, FETCH_PRODUCTS_BY_CATEGORY, 
	FETCH_ONLINE_ORDERS, UPDATE_ONLINE_ORDERS,ADD_PROMOTION, DISPLAY_PROMOTIONS, 

	DISPLAY_SELECTED_PROMOTIONS, INSERT_ORDER_TO_DATABASE,
	SELECT, EK_LOGIN, LOGIN_FAILED_ALREADY_LOGGED_IN, LOGIN_FAILED_ILLEGAL_INPUT, REQUEST_ALL_MACHINES; // Rotem hadded all of these
	
	/*
	 * Legend:
	 * CONNECT_TO_SERVER - sends an empty object, used by server to maintain a list of connected clients (better than my original way)
	 * LOGIN - message to be sent when authentication is performed, pass tuple[user,pass] along it
	 * LOGOUT - we maintain an object for the current logged user - disconnect the user, pass[user,pass] along it
	 * ERROR_MESSAGE - whenever communication leads to an error, send this type and a code for the error - either String or enum (tbd)
	 * ACK - whenever a message does not require a response, return this to indicate the operation was successful (enum, to represent the exact operation)
	 * CRASH - if client crashes - perform log-out, set database to default state (tbd), and show special window.
	 * 		 - if server crashes - disconnect all clients (send this message to all clients) and show proper crash message on both sides (server and client)
	 * 
	 * CREATE - create a database schema/table, might not be required (tbd)
	 * UPDATE - update entry/entries in a table - object passed: tuple[table_name, what_column_to_change, which_row_to_change, what_to_change_to]
	 * REMOVE - remove an entry/entries from a table - object passed: tuple[table_name, columns_to_remove_by, value_to_remove] 
	 * ADD - 	add an entry/entries to a table - object passed: tuple[table_name, object_to_add(array of strings)]
	 * DISPLAY -display entry/entries from a table - object passed: tuple[table_name, columns_to_search_by, value_to_search] 
	 */
	
	
	// old (from prototype)
	//UPDATE_CUSTOMER, ADD_CUSTOMER, DISPLAY_CUSTOMER;
	@Override
	public String toString(){
	    return name();
	}

}
