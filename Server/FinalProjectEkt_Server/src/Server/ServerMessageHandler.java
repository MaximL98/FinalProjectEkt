package Server;

import java.util.HashMap;

import common.IServerSideFunction;
import common.SCCP;
import common.ServerClientRequestTypes;
import logic.Customer;

/*
 * Dabru hiti ani asbir mkave shze lo yeye mazben ela moil
 */

public class ServerMessageHandler {
	// Class that handles a message which adds row/rows to database
	private static final class HandleMessageAddToTable implements IServerSideFunction {
		private static final int MESSAGE_OBJECT_ARRAY_SIZE = 3;

		@Override
		public SCCP handleMessage(SCCP message) {
			// message should be: Type(SCRT), {String_tableName, Boolean_addMany, Object[]_whatToAdd}
			SCCP response = new SCCP();
			ServerClientRequestTypes type = message.getRequestType();
			Object tmpMsg = message.getMessageSent();
			Object[] formattedMessage;
			
			// parts of the message:
			String tableName;
			Boolean addMany;
			Object[] objectsToAdd;
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

			// debug
			System.out.println("Called server with ADD:");
			System.out.println("table name: " + tableName);
			System.out.println("Add many (boolean): " + addMany);
			System.out.println("Objects: ");
			for(Object o : objectsToAdd) {
				System.out.println(o);
			}
			// end debug
			
			
			System.out.println("Calling the DB controller now (UNDER TEST)");
			// if addMany = false, the controller will use a different query that will expect an array of size 1 (1 object)  
			// now, we pass this three to the database controller.
			boolean res = DatabaseController.handleQuery(DatabaseOperations.INSERT, tableName, addMany, objectsToAdd);
			
			
			// here, we return the proper message to the client
			// we will need some imports to do so (NOT IMPLEMENTED)
			System.out.println("Returning result to client (UNDER TEST)");
			if(res) {
				response.setRequestType(ServerClientRequestTypes.ACK);
				response.setMessageSent(objectsToAdd); // send the array of objects we sent to add to the db, to indicate success
			}
			else {
				response.setRequestType(ServerClientRequestTypes.ERROR_MESSAGE);
				response.setMessageSent("ERROR: adding to DB failed"); // TODO: add some valuable information.
			}
			
			return response;
			
		}
	}


	private static HashMap<ServerClientRequestTypes, IServerSideFunction> map = 
			new HashMap<ServerClientRequestTypes, IServerSideFunction>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		
		/*
		 * A dedicated function for a message to add something to some table (server-side)
		 * 
		 */
		this.put(ServerClientRequestTypes.ADD, new HandleMessageAddToTable());
	}};

	
	public static HashMap<ServerClientRequestTypes, IServerSideFunction> getMap() {
		return map;
	}
	
	
	/*
	 * This main is used as a test to what I just did
	 */
	/*
	 public static void main(String[] args) {
		new ServerMessageHandler();
		System.out.println("Testing message handler for \"ADD\" message type!");
		String tableName = "Users";
		Boolean many = false;
		Object[] objectsToAdd = new Object[1];
		objectsToAdd[0] = new Customer("David", "Dahookie", 1,"0505550000", "adolph@boy.are.you.fat", 
				"1234-4321-5678-8765", "cumdumpster2020", "x0x0Y0banevr0t");
		
		SCCP msg = new SCCP();
		
		msg.setRequestType(ServerClientRequestTypes.ADD);
		
		msg.setMessageSent(new Object[] {
				// first is string (table name)
				tableName,
				// second is Boolean (many?)
				many,
				// third is the array of objects to add
				objectsToAdd
		});
		HashMap<ServerClientRequestTypes, IServerSideFunction> mymap = getMap();
		IServerSideFunction f = mymap.get(msg.getRequestType());
		f.handleMessage(msg);
		
	}*/
	
}
