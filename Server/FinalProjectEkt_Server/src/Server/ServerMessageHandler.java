package Server;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import common.IServerSideFunction;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.TypeChecker;
import database.DatabaseController;
import database.DatabaseOperation;
import database.*;
import logic.Customer;
import logic.Product;
import logic.Promotions;
import logic.Role;
import logic.SystemUser;
/**
 * ServerMessageHandler: a wrapper class for a HashMap - map operation types to operations
 * operation types: ServerClientRequestTypes objects
 * operations: IServerSideFunction objects - implement the interface IServerSideFunction, 
 * 											 and with it the handleMessage method that does 
 * 											 the server's work for a given message
 * @author Rotem
 *
 */

public class ServerMessageHandler {
	// Class that handles a message which adds row/rows to database
	private static final class HandleMessageAddToTable implements IServerSideFunction {
		// this is defined as a constant since, for adding to table, we always want a 3 element Object array.
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 3;
		
		@Override
		public SCCP handleMessage(SCCP message) {
			// message should be: Type(ServerClientRequestTypes), Object[]{String_tableName, Boolean_addMany, Object[]_whatToAdd}
			// preparing response: will eventually contain a type[error or success], and a message[should be the original added object(s)
			SCCP response = new SCCP();
			ServerClientRequestTypes type = message.getRequestType();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;
			
			
			// parts of the message:
			String tableName;
			Boolean addMany;
			Object[] objectsToAdd;
			
			/// Start input validation
			// verify type
			if(!(type.equals(ServerClientRequestTypes.ADD))) {
				throw new IllegalArgumentException("Invalid type used in handleMessage, type: " + message.getRequestType());
			}
			// verify message format
			if(tmpMsg instanceof Object[]) {
				formattedMessage = (Object[])tmpMsg;
				if(formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: " 
				+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input
				
				// table name:
				if(!(formattedMessage[0] instanceof String)) {
					throw new IllegalArgumentException("Invalid value for tableName (String input) in handleMessage.");
				}
				// boolean addMany
				if(!(formattedMessage[1] instanceof Boolean)) {
					throw new IllegalArgumentException("Invalid value for addMany (Boolean input) in handleMessage.");
				}
				if(!(formattedMessage[2] instanceof Object[])) {
					throw new IllegalArgumentException("Invalid value for whatToAdd (Object[] input) in handleMessage.");
				}
				
				// assign proper values to parts of the message
				tableName = (String)formattedMessage[0];
				addMany = (Boolean)formattedMessage[1];
				objectsToAdd = (Object[])formattedMessage[2];
				
			}
			else {
				// invalid input branch
				throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
			+ message.getMessageSent() + " is not of type Object[]");
			}

			/// End input validation
			
			// debug
			System.out.println("Called server with ADD.\nTable name: " + tableName + "\nAdd many (boolean): " + addMany+"\nObjects (to add): ");
			for(Object o : objectsToAdd) {
				System.out.println(o);
			}
			// end debug
			
			
			System.out.println("Calling the DB controller now (UNDER TEST)");
			// if addMany = false, the controller will use a different query that will expect an array of size 1 (1 object)  
			// now, we pass this three to the database controller.
			boolean res = (boolean)DatabaseController.handleQuery(DatabaseOperation.INSERT, new Object[] {tableName, addMany, objectsToAdd});
			
			
			// here, we return the proper message to the client
			// we will need some imports to do so (NOT IMPLEMENTED)
			System.out.println("Returning result to client (UNDER TEST)");
			if(res) {
				response.setRequestType(ServerClientRequestTypes.ACK);
				response.setMessageSent(objectsToAdd); // send the array of objects we sent to add to the db, to indicate success
			}
			else {
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				// idea - maybe we should create a special type for errors too, and pass a dedicated one that will provide valuable info to the client?
				response.setMessageSent("ERROR: adding to DB failed"); // TODO: add some valuable information.
			}
			return response;
		}
	}

	// Rotem - 1/5/23 - select
	// select what from table where what and;
	private static final class HandleMessageSelectFromTable implements IServerSideFunction {
		/*
		 * The format sent to this class:
		 * Object[] {
		 * tableName: String
		 * filterColumns (if false: select * from mama, as in ALL COLUMNS): Boolean
		 * filterColumnsArgs: String
		 * filterRows (select ? from mama where ass = shit): Boolean
		 * conditions: String (this is the part where you pass what comes after the "WHERE" in "WHERE ass = shit")
		 * conditions = null if filterRows is false
		 * useSpecialArgs: Boolean (self explanatory -read next line)
		 * specialArgs: String (for example: SORT BY shit_amount, you pass it with the SORT BY inside the string because I can't be bothered to that much of an extent)
		 * specialArgs = null if useSpecialArgs = false
		 * }
		 */
		// this is defined as a constant since, for selecting from table, we always want a 5 element Object array.
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 7;
		
		@Override
		public SCCP handleMessage(SCCP message) {
			// message should be: Type(ServerClientRequestTypes), Object[]{String_tableName, Boolean_addMany, Object[]_whatToAdd}
			// preparing response: will eventually contain a type[error or success], and a message[should be the original added object(s)
			SCCP response = new SCCP();
			ServerClientRequestTypes type = message.getRequestType();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;
			
			
			// parts of the message:
			String tableName;
			Boolean filterColumns;
			String filterColumnsArgs;

			Boolean filterRows;
			Boolean useSpecialArgs;
			String conditions;
			String specialArgs;

			
			/// Start input validation
			// verify type
			if(!(type.equals(ServerClientRequestTypes.SELECT))) {
				throw new IllegalArgumentException("Invalid type assigned in ServerMessageHandler map, type: " + message.getRequestType() + " was passed to SELECT");
			}
			// verify message format
			if(tmpMsg instanceof Object[]) {
				formattedMessage = (Object[])tmpMsg;
				if(formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: " 
				+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input
				
				// table name:
				if(!(formattedMessage[0] instanceof String)) {
					throw new IllegalArgumentException("Invalid value for tableName (String input) in handleMessage.");
				}
				// boolean filterColumns
				if(!(formattedMessage[1] instanceof Boolean)) {
					throw new IllegalArgumentException("Invalid value for selectAll (Boolean input) in handleMessage.");
				}
				filterColumns = (Boolean)formattedMessage[1];
				if(filterColumns) {
					// String filterColumnsArgs:
					if(!(formattedMessage[2] instanceof String)) {
						throw new IllegalArgumentException("Invalid value for conditions (String input) in handleMessage.");
					}
				}
				// boolean filterRows
				if(!(formattedMessage[3] instanceof Boolean)) {
					throw new IllegalArgumentException("Invalid value for filterRows (Boolean input) in handleMessage.");
				}
				// check if we want specific rows
				filterRows = (Boolean)formattedMessage[3];
				if(filterRows)
				{
					// String conditions:
					if(!(formattedMessage[4] instanceof String)) {
						throw new IllegalArgumentException("Invalid value for conditions (String input) in handleMessage.");
					}
				}
				// boolean useSpecialArgs
				if(!(formattedMessage[5] instanceof Boolean)) {
					throw new IllegalArgumentException("Invalid value for useSpecialArgs (Boolean input) in handleMessage.");
				}
				// check if want to use special arguments
				useSpecialArgs = (Boolean)formattedMessage[5];
				if(useSpecialArgs) {
					// string specialArgs
					if(!(formattedMessage[6] instanceof String)) {
						throw new IllegalArgumentException("Invalid value for specialArgs (String input) in handleMessage.");
					}
				}
				// assign proper values to from the rest of the message
				tableName = (String)formattedMessage[0];
				filterColumnsArgs = (String)formattedMessage[2];
				conditions = (String)formattedMessage[4];
				specialArgs = (String)formattedMessage[6];

			}
			else {
				// invalid input branch
				throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
			+ message.getMessageSent() + " is not of type Object[]");
			}

			/// End input validation
			
			// debug
			System.out.print("Called server with SELECT. Table name: " + tableName + " query to be performed=");
			StringBuilder queryBuilder = new StringBuilder("SELECT ");
			// select what (rows)
			if(!filterColumns) {
				queryBuilder.append("*");
			}
			else {
				queryBuilder.append(filterColumnsArgs).append(" ");
			}
			// table name:
			queryBuilder.append("FROM ").append(tableName).append(" ");
			// select where (columns)
			if(filterRows)
			{
				queryBuilder.append("WHERE ").append(conditions).append(" ");
			}
			if(useSpecialArgs) {
				queryBuilder.append(specialArgs);
			}
			queryBuilder.append(";");
			
			String query = queryBuilder.toString();
			System.out.println(query);
			
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>)DatabaseController.handleQuery(DatabaseOperation.GENERIC_SELECT, new Object[] {query});
			if(res == null) {
				// error
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				response.setMessageSent("Invalid input to SELECT query.");
			}
			response.setRequestType(ServerClientRequestTypes.ACK);
			response.setMessageSent(res);
			return response;
		}
	}

	// UPDATE table_name
	// SET column1 = value1, column2 = value2, ...
	// WHERE condition; 
	// {tablename, "amuda1 = \"something\", amuda2 = else", "id=3"};  
	private static final class HandleMessageUpdateInTable implements IServerSideFunction{
		private static final int NUMBER_OF_ARGUMENTS_UPDATE = 3;

		@Override
		public SCCP handleMessage(SCCP message) {
			// The input message should look like:
			// Object[]{ tableName, setters (String), conditions};
			// I'm not going to go crazy here, no million billion tests:
			if(!message.getRequestType().equals(ServerClientRequestTypes.UPDATE)) {
				throw new IllegalArgumentException("Invalid input in HandleMessageUpdateInTable (Invalid type)");
			}
			if(!(message.getMessageSent() instanceof Object[])) {
				throw new IllegalArgumentException("Invalid input in HandleMessageUpdateInTable (not Object[])");
			}
			Object[] input = (Object[])message.getMessageSent();
			// as I said - 3 arguments stored in
			if(input.length != NUMBER_OF_ARGUMENTS_UPDATE) {
				throw new IllegalArgumentException("Invalid input in HandleMessageUpdateInTable (length != 3)");
			}
			// we want three strings: table name, setters, conditions (setters example: "id = 123", conditions example: "name = \"Doodoo\"")
			Class<?> types[] = new Class[] {String.class, String.class, String.class};
			
			if(TypeChecker.validate(input, (List<Class<?>>) Arrays.asList(types), 0)) {
				// prep response
				SCCP response = new SCCP();
				/**
				 * Now this is important - I can't be bothered to map this inside the database map - for now it's a direct call to the controller.
				 */
				// send the values to the database map:
				Object dbAnswer = DatabaseController.handleQuery(DatabaseOperation.UPDATE, input);
				Boolean dbAnsBoolean = (Boolean)dbAnswer;
				if(dbAnsBoolean) {
					// failure
					response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
					// maybe we should create a special type for errors too, and pass a dedicated one that will provide valuable info to the client?
					response.setMessageSent("ERROR: updating in DB failed"); // TODO: add some valuable information.
				}
				else {
					// socc secc
					response.setRequestType(ServerClientRequestTypes.ACK);
					response.setMessageSent(message.getMessageSent());
				}
			}
			else {
				throw new IllegalArgumentException("Invalid input in HandleMessageUpdateInTable (3 String arguments expected!)");
			}


			
			return null;
		}
		
	}
	
	private static final class HandleMessageLogin implements IServerSideFunction{

		// TODO:
		// we need to modify this queer, we need to ask the DB for an entry with username,
		// if not found, we return error "no such user",
		// else, we compare passwords HERE (too bad, too much work 2 queers for one action)
		// if true, connect,
		// else, respond "wrong password"
		@Override
		public SCCP handleMessage(SCCP loginMessage) {
			// we are supposed to get this object:
			// SCCP(
			// ServerClientRequestTypes LOGIN, new String[]{"username", "password"}
			// )
			String username = (String)((Object[])loginMessage.getMessageSent())[0];
			String password = (String)((Object[])loginMessage.getMessageSent())[1];
			System.out.println("Server processing login request for user "+ username);
			Object res = DatabaseController.
					handleQuery(DatabaseOperation.USER_LOGIN, new Object[] {"systemuser", loginMessage.getMessageSent()});
			if(res == null) {
				System.out.println("Server failed login request for user "+ username + " (invalid username or password).");
				return new SCCP(ServerClientRequestTypes.LOGIN_FAILED_ILLEGAL_INPUT, res);
			}
			
			if(res instanceof SystemUser) {
				// we already know now that the user exists and has inserted the correct password
				
				 // now, check if user is already connected
				String sqlQueryLoggedInTest ="SELECT * FROM logged_users WHERE username = \"" + username+"\";";
				Object res2 = DatabaseController.
							handleQuery(DatabaseOperation.GENERIC_SELECT, new Object[] {sqlQueryLoggedInTest});
				@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Object>> actualResult2 = (ArrayList<ArrayList<Object>>)res2;

				// if already connected, inform client

				  if((!(actualResult2 == null)) && actualResult2.size() != 0) {
					  System.out.println("Server login request for user "+ username + " failed - user already connected.");
					 return new SCCP(ServerClientRequestTypes.LOGIN_FAILED_ALREADY_LOGGED_IN, (SystemUser)res);
				}
				  // else, connect user (and write him in database)!
				DatabaseController.handleQuery(DatabaseOperation.INSERT, new Object[] {"logged_users", false, new Object[]{"(\""+username+"\")"}});
				System.out.println("Server login request for user "+ username + " completed: user inserted to logged_users table.");

				return new SCCP(ServerClientRequestTypes.LOGIN, (SystemUser)res);
			}
			// we know that something wrong occurred
			System.out.println("Server login request for user "+ username + " completed: user inserted to logged_users table.");
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");		
		}
		
	}
	
	

	// EK LOGIN (Electronic Turk machine login)
	private static final class HandleMessageLoginEK implements IServerSideFunction{
		private static final int SYSTEM_USER_TABLE_COL_COUNT = 9;

		// TODO:
		// we need to modify this queer, we need to ask the DB for an entry with username,
		// if not found, we return error "no such user",
		// else, we compare passwords HERE (too bad, too much work 2 queers for one action)
		// if true, connect,
		// else, respond "wrong password"
		@Override
		public SCCP handleMessage(SCCP loginMessage) {
			// we are supposed to get this object:
			// SCCP(
			// ServerClientRequestTypes EK_LOGIN (or LOGIN_EK), new Object[]{"username", "password"}
			// )
			SCCP responseFromServer = new SCCP();

			String username = (String)((Object[])loginMessage.getMessageSent())[0];
			String password = (String)((Object[])loginMessage.getMessageSent())[1];
			
			System.out.println("cock sucking got "+username + ", " + password + "and working onit");
			Object res = DatabaseController.
					handleQuery(DatabaseOperation.SELECT, 
							new Object[]{"SELECT * FROM systemuser WHERE username = '" + username + "' AND password = '" + password+"';"});
			if(!(res instanceof ResultSet)) {
				throw new RuntimeException("Improbable error in LoginEK");
			}
			ResultSet rs = (ResultSet)res;
			ResultSetMetaData rsmd;
				try {
					rsmd = rs.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					ArrayList<ArrayList<Object>> result = new ArrayList<>();
					while(rs.next()) {
						ArrayList<Object> row = new ArrayList<>();
						for(int i=0;i<columnsNumber;i++) {
							row.add(rs.getObject(i + 1));
						}
						result.add(row);
					}
					// close rs
					rs.close();
					System.out.println("cock sucking finished working on first queer");
					// now, we expect result to be of size 1, and contain an array list with 2 columns! (else, we have an invalid login attempt)
					if(result.size() != 1) {
						// invalid login
						responseFromServer.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
						responseFromServer.setMessageSent("Invalid input for login");
						return responseFromServer;
					}
					if(result.get(0).size() != SYSTEM_USER_TABLE_COL_COUNT) {
						throw new SQLDataException("We expect a table with "+SYSTEM_USER_TABLE_COL_COUNT+" columns for EVERY USER - Illegal database state");
					}
					String roleString = (String)result.get(0).get(8);
					try{
						Role role = Role.getRoleFromString(roleString);
						SystemUser su = new SystemUser(
								(Integer)result.get(0).get(0),
								result.get(0).get(1).toString(),
								result.get(0).get(2).toString(),
								result.get(0).get(3).toString(), 
								result.get(0).get(4).toString(), 
								result.get(0).get(5).toString(), 
								username, 
								password, 
								role);
								
						if(validEKConfigRole(role)) {
							System.out.println("cock sucking role is OK so working on 2nd queery");
							// now, check if it is already logged in (... you know what I want to say)
							ResultSet rs2 = (ResultSet)DatabaseController.
									handleQuery(DatabaseOperation.SELECT, 
											new Object[]{"SELECT username FROM logged_users WHERE username = '" + username + "'"});
							try {
								rsmd = rs2.getMetaData();
								int columnsNumber2 = rsmd.getColumnCount();
								ArrayList<ArrayList<Object>> result2 = new ArrayList<>();
								while(rs2.next()) {
									ArrayList<Object> row2 = new ArrayList<>();
									for(int i=0;i<columnsNumber2;i++) {
										row2.add(rs2.getObject(i + 1));
									}
									result.add(row2);
								}
								// close rs2
								System.out.println("cock sucking finishged working on 2nd queery");
								rs2.close();
								// now, we expect result to be of size 1, and contain an array list with 2 columns! (else, we have an invalid login attempt)
								if(result2.size() == 0) {
									System.out.println("query 2 is fine, working on 3");
									// we don't have user in table, good!
									// append username to this table and shut up and close
									boolean tmp = (Boolean)DatabaseController.handleQuery(DatabaseOperation.INSERT, new Object[] {"logged_users", false, new Object[]{"('"+username+"')"}});
									if(!tmp) {
										throw new IllegalStateException("Should never happen (crashed adding "+username+" to db, even though we know he wasn't there).");
									}
									System.out.println("FINISHED FUCKING WORKIING ON 3, got boolean " + tmp);
									responseFromServer.setRequestType(ServerClientRequestTypes.ACK);
									responseFromServer.setMessageSent(su); // pass the SystemUser object of the currently logged in user!
									return responseFromServer;
								}
								else {
									// user aleady logged in
									responseFromServer.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
									responseFromServer.setMessageSent("Cannot login twice for user user");
									return responseFromServer;
								}
									
								
							}catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						
					}catch(IllegalArgumentException ex) {
						throw new SQLDataException("We expect Role(typeOfUser column) to be of a set of available roles "
								+ "(role='"+roleString+"' does not exist)");
					}
					// 

					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(res instanceof SystemUser) {
				// we already know now that the user exists and has inserted the correct password
				return new SCCP(ServerClientRequestTypes.LOGIN, (SystemUser)res);
			}
			// we know that something wrong occurred
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");		
		}

		private boolean validEKConfigRole(Role role) {
			// TODO Auto-generated method stub
			/*
			 * TODO: add checks for each type - SUBSCRIBER, OVER_TIPULI . . .
			 */
			return role.equals(Role.CUSTOMER);
		}
		
	}

	
	/**
	 * VERY IMPORTANT: INSERT THIS CALL ANYWHERE YOU ACTUALLY LOG OFF, INCLUDING ANY X BUTTONS
	 * @author Rotem
	 */
	// simple - get username, remove it from table
	private static final class HandleMessageLogout implements IServerSideFunction{

		@Override
		public SCCP handleMessage(SCCP message) {
			/**
			 * TODO: move this call to a dedicated "DELETE" handleQuery implementation in DatabaseOperationsMap
			 */
			String sqlQuery = "DELETE FROM " +DatabaseController.getSchemaName()+".logged_users WHERE (username = '"+ message.getMessageSent()+"');";
			Boolean tmp = DatabaseController.executeQuery(sqlQuery);
			SCCP response = new SCCP(ServerClientRequestTypes.ACK, tmp);
			return response;
		}
		
	}
	
	// explain it
	private static final class HandleMessageGet implements IServerSideFunction{

		@Override
		public SCCP handleMessage(SCCP loginMessage) {
			// TODO Auto-generated method stub
			// we are supposed to get this object:
			// SCCP(
			// ServerClientRequestTypes GET, Object[]{where_to_look(tableName), boolean(getMany), 
			// what_to_get(String[]{(column = match),(column = match)})}
			// )

			assert loginMessage.getMessageSent() instanceof Object[]; // assertion as shortcut
			
			String tableName = (String)((Object[])loginMessage.getMessageSent())[0];
			String[] columns = (String[])((Object[])loginMessage.getMessageSent())[1];
			
			Object res = DatabaseController.
					handleQuery(DatabaseOperation.USER_LOGIN, new Object[] {tableName, loginMessage.getMessageSent()});
			if(res instanceof SystemUser) {
				
				return new SCCP(ServerClientRequestTypes.LOGIN, (SystemUser)res);
			}
			
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");		
		}
		
	}
	
	//Handle message of fetching all products with the category name contained in fetchProductsMessage
    // fetchProductsMessage.getMessageSent() == "category"
    private static final class HandleMessageFetchProducts implements IServerSideFunction {

        @Override
        public SCCP handleMessage(SCCP fetchProductsMessage) {
            Object resultSetProducts = DatabaseController.handleQuery(
            		DatabaseOperation.FETCH_PRODUCTS_BY_CATEGORY, new Object[] {fetchProductsMessage.getMessageSent()});

            if (resultSetProducts instanceof ArrayList) {
            	return new SCCP(ServerClientRequestTypes.FETCH_PRODUCTS_BY_CATEGORY, resultSetProducts);
            }	
            return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
        }
    }

    
    
	// Handle message of fetching all order types from the order_type table.
	// fetchProductsMessage.getMessageSent() == "category"
	private static final class HandleMessageFetchOnlineOrders implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP fetchOnlineOrdersMessage) {
			Object resultSetOnlineOrders = DatabaseController.handleQuery(
					DatabaseOperation.FETCH_ONLINE_ORDERS, new Object[] { fetchOnlineOrdersMessage.getMessageSent() });

			if (resultSetOnlineOrders instanceof ArrayList) {
				return new SCCP(ServerClientRequestTypes.FETCH_ONLINE_ORDERS, resultSetOnlineOrders);
			}
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
		}
	}

	public static final class HandleMessageDisplayPromotions implements IServerSideFunction {

		ArrayList<String> promotionNames = new ArrayList<String>();

		@Override
		public SCCP handleMessage(SCCP displayPromotionMessage) {

			try {
				ResultSet resultSet = (ResultSet) DatabaseController
						.handleQuery(DatabaseOperation.INSERT_PROMOTION_NAMES,new Object[] {"SELECT DISTINCT promotionName FROM promotions;"});
				resultSet.beforeFirst();
				while (resultSet.next()) {
					String promotionName = resultSet.getString("promotionName");
					promotionNames.add(promotionName);
				}
				resultSet.close();

				return new SCCP(ServerClientRequestTypes.DISPLAY, promotionNames);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
		}

	}

	public static final class HandleMessageDisplaySelectedPromotions implements IServerSideFunction{

		ArrayList<Promotions> promotions = new ArrayList<>();

		@Override
		public SCCP handleMessage(SCCP displayPromotionMessage) {
			String promotionName = (String) displayPromotionMessage.getMessageSent();
			//promotions = (ArrayList<Promotions>) DatabaseController
					//.handleQuery(DatabaseOperation.SELECT,new Object[] {"SELECT * FROM promotions WHERE promotionName = '\" + promotionName +"\';"});
			promotions = (ArrayList<Promotions>) DatabaseController.handleQuery(DatabaseOperation.SELECT_PROMOTION, new Object[] {"SELECT * FROM promotions WHERE promotionName = '" + promotionName + "';"});
			return new SCCP(ServerClientRequestTypes.DISPLAY, promotions);
		}
	}

	private static final class HandleMessageUpdateOnlineOrders implements IServerSideFunction {
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 1;

		@Override
		public SCCP handleMessage(SCCP message) {
			ServerClientRequestTypes type = message.getRequestType();
			SCCP response = new SCCP();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;

			// parts of the message:
			Object[] objectsToUpdate;

			/// Start input validation

			// verify type
			if (!(type.equals(ServerClientRequestTypes.UPDATE_ONLINE_ORDERS))) {
				throw new IllegalArgumentException(
						"Invalid type used in handleMessage, type: " + message.getRequestType());
			}
			// verify message format
			if (tmpMsg instanceof Object[]) {
				formattedMessage = (Object[]) tmpMsg;
				if (formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
							+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input

				// objects to update
				if (!(formattedMessage[0] instanceof Object[])) {
					throw new IllegalArgumentException(
							"Invalid value for whatToUpdate (Object[] input) in handleMessage.");
				}

				// assign proper values to parts of the message
				objectsToUpdate = (Object[]) formattedMessage[0];

			} else {
				// invalid input branch
				throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
						+ message.getMessageSent() + " is not of type Object[]");
			}

			boolean res = (boolean) DatabaseController.handleQuery(DatabaseOperation.UPDATE_ONLINE_ORDERS,
					new Object[] { objectsToUpdate });
			if (res) {
				response.setRequestType(ServerClientRequestTypes.ACK);
				response.setMessageSent(objectsToUpdate); // send the array of objects we sent to add to the db, to
															// indicate success
			} else {
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				// idea - maybe we should create a special type for errors too, and pass a
				// dedicated one that will provide valuable info to the client?
				response.setMessageSent("ERROR: updaing DB failed"); // TODO: add some valuable information.
			}
			return response;
		}
	}

	private static final class HandleMessageAddPromotion implements IServerSideFunction {
		// this is defined as a constant since, for adding a promotion, we always want a
		// 3 element Object array.
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 7;

		@Override
		public SCCP handleMessage(SCCP message) {
			// message should be: Type(ServerClientRequestTypes), Object[]{String_tableName,
			// Boolean_addMany, Object[]_whatToAdd}
			// preparing response: will eventually contain a type[error or success], and a
			// message[should be the original added object(s)
			SCCP response = new SCCP();
			ServerClientRequestTypes type = message.getRequestType();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;

			// parts of the message:
			String tableName;
			Boolean addMany;
			Object[] objectsToAdd;

			/// Start input validation

			// verify type
			if (!(type.equals(ServerClientRequestTypes.ADD))) {
				throw new IllegalArgumentException(
						"Invalid type used in handleMessage, type: " + message.getRequestType());
			}

			// verify message format
			if (tmpMsg instanceof Object[]) {
				formattedMessage = (Object[]) tmpMsg;
				if (formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
							+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input

				// table name:
				if (!(formattedMessage[0] instanceof String)) {
					throw new IllegalArgumentException("Invalid value for tableName (String input) in handleMessage.");
				}
				// boolean addMany
				if (!(formattedMessage[1] instanceof Boolean)) {
					throw new IllegalArgumentException("Invalid value for addMany (Boolean input) in handleMessage.");
				}
				if (!(formattedMessage[2] instanceof Object[])) {
					throw new IllegalArgumentException(
							"Invalid value for whatToAdd (Object[] input) in handleMessage.");
				}

				// assign proper values to parts of the message
				tableName = (String) formattedMessage[0];
				addMany = (Boolean) formattedMessage[1];
				objectsToAdd = (Object[]) formattedMessage[2];
			} else {
				// invalid input branch
				throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
						+ message.getMessageSent() + " is not of type Object[]");
			}

			/// End input validation

			// debug
			System.out.println("Called server with ADD_PROMOTION.\nTable name: " + tableName + "\nAdd many (boolean): "
					+ addMany + "\nObjects (to add): ");
			for (Object o : objectsToAdd) {
				System.out.println(o);
			}
			// end debug
			System.out.println("Calling the DB controller now (UNDER TEST)");
			// if addMany = false, the controller will use a different query that will
			// expect an array of size 1 (1 object)
			// now, we pass this three to the database controller.
			boolean res = (boolean) DatabaseController.handleQuery(DatabaseOperation.INSERT,
					new Object[] { tableName, addMany, objectsToAdd });

			// here, we return the proper message to the client
			// we will need some imports to do so (NOT IMPLEMENTED)
			System.out.println("Returning result to client (UNDER TEST)");
			if (res) {
				response.setRequestType(ServerClientRequestTypes.ACK);
				response.setMessageSent(objectsToAdd); // send the array of objects we sent to add to the db, to
														// indicate success
			} else {
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				// idea - maybe we should create a special type for errors too, and pass a
				// dedicated one that will provide valuable info to the client?
				response.setMessageSent("ERROR: adding to DB failed"); // TODO: add some valuable information.
			}
			return response;
		}

	}

	private static HashMap<ServerClientRequestTypes, IServerSideFunction> map = 
			new HashMap<ServerClientRequestTypes, IServerSideFunction>() {

		private static final long serialVersionUID = 1L;

	{
			/*
		 * A dedicated function for a message to add something to some table (server-side)
		 * 
		 */
		this.put(ServerClientRequestTypes.ADD, new HandleMessageAddToTable());
		this.put(ServerClientRequestTypes.SELECT, new HandleMessageSelectFromTable());
		this.put(ServerClientRequestTypes.UPDATE, new HandleMessageUpdateInTable());
		this.put(ServerClientRequestTypes.LOGIN, new HandleMessageLogin());
		this.put(ServerClientRequestTypes.EK_LOGIN, new HandleMessageLoginEK());
		this.put(ServerClientRequestTypes.LOGOUT, new HandleMessageLogout());

		
		this.put(ServerClientRequestTypes.FETCH_PRODUCTS_BY_CATEGORY, new HandleMessageFetchProducts());
		this.put(ServerClientRequestTypes.FETCH_ONLINE_ORDERS, new HandleMessageFetchOnlineOrders());
		this.put(ServerClientRequestTypes.DISPLAY_PROMOTIONS, new HandleMessageDisplayPromotions());
		this.put(ServerClientRequestTypes.DISPLAY_SELECTED_PROMOTIONS, new HandleMessageDisplaySelectedPromotions());
		this.put(ServerClientRequestTypes.UPDATE_ONLINE_ORDERS, new HandleMessageUpdateOnlineOrders());
		this.put(ServerClientRequestTypes.ADD_PROMOTION, new HandleMessageAddPromotion());
		
		
		
	}};

	
	public static HashMap<ServerClientRequestTypes, IServerSideFunction> getMap() {
		return map;
	}
	
	
}

