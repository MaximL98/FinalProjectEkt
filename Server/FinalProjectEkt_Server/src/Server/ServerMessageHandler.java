package Server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import common.IServerSideFunction;
import common.SCCP;
import common.ServerClientRequestTypes;
import logic.Customer;
import logic.Product;
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
			if (tmpMsg instanceof Object[]) {
				formattedMessage = (Object[]) tmpMsg;
				if (formattedMessage.length != MESSAGE_OBJECT_ARRAY_SIZE) {
					throw new IllegalArgumentException("Invalid message accepted in handleMessage, message: "
							+ message.getMessageSent() + " is an Object array of size != " + MESSAGE_OBJECT_ARRAY_SIZE);
				}
				// verify each input

				// table name:
				if(!(formattedMessage[0] instanceof String)) {
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
			System.out.println("Called server with ADD.\nTable name: " + tableName + "\nAdd many (boolean): " + addMany
					+ "\nObjects (to add): ");
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

	private static final class HandleMessageLogin implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP loginMessage) {
			// TODO Auto-generated method stub
			// we are supposed to get this object:
			// SCCP(
			// ServerClientRequestTypes LOGIN, new String[]{"username", "password"}
			// )

			Object res = DatabaseController.handleQuery(DatabaseOperation.USER_LOGIN,
					new Object[] { "systemuser", loginMessage.getMessageSent() });
			if (res instanceof SystemUser) {
				return new SCCP(ServerClientRequestTypes.LOGIN, (SystemUser) res);
			}

			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
		}

	}

	// Handle message of fetching all products with the category name contained in
	// fetchProductsMessage
	// fetchProductsMessage.getMessageSent() == "category"
	private static final class HandleMessageFetchProducts implements IServerSideFunction {

		@Override
		public SCCP handleMessage(SCCP fetchProductsMessage) {
			Object resultSetProducts = DatabaseController.handleQueryFetchProducts(
					DatabaseOperation.FETCH_PRODUCTS_BY_CATEGORY,
					new Object[] { fetchProductsMessage.getMessageSent() });

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
			Object resultSetOnlineOrders = DatabaseController.handleQueryFetchOnlineOrders(
					DatabaseOperation.FETCH_ONLINE_ORDERS, new Object[] { fetchOnlineOrdersMessage.getMessageSent() });

			if (resultSetOnlineOrders instanceof ArrayList) {
				return new SCCP(ServerClientRequestTypes.FETCH_ONLINE_ORDERS, resultSetOnlineOrders);
			}
			return new SCCP(ServerClientRequestTypes.ERROR_MESSAGE, "error");
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
	private static HashMap<ServerClientRequestTypes, IServerSideFunction> map = new HashMap<ServerClientRequestTypes, IServerSideFunction>() {

		private static final long serialVersionUID = 1L;

		{

			/*
			 * A dedicated function for a message to add something to some table
			 * (server-side)
			 * 
			 */
			this.put(ServerClientRequestTypes.ADD, new HandleMessageAddToTable());
			this.put(ServerClientRequestTypes.LOGIN, new HandleMessageLogin());
			this.put(ServerClientRequestTypes.FETCH_PRODUCTS_BY_CATEGORY, new HandleMessageFetchProducts());
			this.put(ServerClientRequestTypes.FETCH_ONLINE_ORDERS, new HandleMessageFetchOnlineOrders());
			this.put(ServerClientRequestTypes.UPDATE_ONLINE_ORDERS, new HandleMessageUpdateOnlineOrders());
		}
	};

	public static HashMap<ServerClientRequestTypes, IServerSideFunction> getMap() {
		return map;
	}

}
