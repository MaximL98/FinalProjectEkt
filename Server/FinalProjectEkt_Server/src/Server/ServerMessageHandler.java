package Server;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import common.IServerSideFunction;
import common.SCCP;
import common.ServerClientRequestTypes;
import database.DatabaseController;
import database.DatabaseOperation;
import database.*;
import logic.Customer;
import logic.Product;
import logic.Promotions;
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
						throw new IllegalArgumentException("Invalid value for whatToAdd (String input) in handleMessage.");
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
			System.out.println("Called server with SELECT. Table name: " + tableName + " query to be performed:");
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
			
			System.out.println("Calling the DB controller now (UNDER TEST)");
			ResultSet res = (ResultSet)DatabaseController.handleQuery(DatabaseOperation.SELECT, new Object[] {query});
			if(res == null) {
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				// maybe we should create a special type for errors too, and pass a dedicated one that will provide valuable info to the client?
				response.setMessageSent("ERROR: adding to DB failed"); // TODO: add some valuable information.
				return response;
			}
			ResultSetMetaData rsmd;
			try {
				rsmd = res.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				ArrayList<ArrayList<Object>> result = new ArrayList<>();
				while(res.next()) {
					ArrayList<Object> row = new ArrayList<>();
					for(int i=0;i<columnsNumber;i++) {
						row.add(res.getObject(i + 1));
						
					}
					result.add(row);
				}
				// here, we return the proper message to the client
				System.out.println("Returning result to client (UNDER TEST)");
				response.setRequestType(ServerClientRequestTypes.SELECT);
				response.setMessageSent(result); // send the ResultSet back to the client 
				return response;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// error - this should never happen
			return null;
		}
	}

	// UPDATE table_name
	// SET column1 = value1, column2 = value2, ...
	// WHERE condition; 
	// {tablename, "amuda1 = \"something\", amuda2 = else", "id=3"};  
	private static final class HandleMessageUpdateInTable implements IServerSideFunction{

		@Override
		public SCCP handleMessage(SCCP message) {
			// The input message should look like:
			// Object[]{ tableName, setters (String), conditions};
			// I'm not going to go crazy here, no million billion tests:
			if(!message.getRequestType().equals(ServerClientRequestTypes.UPDATE)) {
				throw new IllegalArgumentException("Invalid input to HandleMessageUpdateInTable (Invalid type)");
			}
			if(!(message.getMessageSent() instanceof Object[])) {
				throw new IllegalArgumentException("Invalid input to HandleMessageUpdateInTable (not Object[])");
			}
			Object[] input = (Object[])message.getMessageSent();
			// as I said - 3 arguments stored in
			if(input.length != 3) {
				throw new IllegalArgumentException("Invalid input to HandleMessageUpdateInTable (length != 3)");
			}
			// get the values:
			String tableName, setters, conditions;
			tableName = (String)input[0];
			setters = (String)input[1];
			conditions = (String)input[2];
			
			StringBuilder sqlQuery = new StringBuilder("UPDATE ");
			sqlQuery.append(DatabaseController.getSchemaName()).append(".").append(tableName).append(" SET ");
			sqlQuery.append(setters).append(" WHERE ");
			sqlQuery.append(conditions).append(";");
			System.out.println("Sending query to database=" + sqlQuery.toString());
			
			// prep response
			SCCP response = new SCCP();
			/**
			 * Now this is important - I can't be bothered to map this inside the database map - for now it's a direct call to the controller.
			 */
			
			boolean b = DatabaseController.executeQuery(sqlQuery.toString());
			if(!b) {
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
			
			return response;
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

			Object res = DatabaseController.
					handleQuery(DatabaseOperation.USER_LOGIN, new Object[] {"systemuser", loginMessage.getMessageSent()});
			if(res instanceof SystemUser) {
				// we already know now that the user exists and has inserted the correct password
				return new SCCP(ServerClientRequestTypes.LOGIN, (SystemUser)res);
			}
			// we know that something wrong occurred
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");		
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

