package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import common.TypeChecker;
import logic.SystemUser;

public class DatabaseOperationsMap {
	private static String SCHEMA_EKRUT = DatabaseController.getSchemaName();

	private static void printDebug(String msg) {
		System.out.println(msg);
	}
	
	// this has to be protected (not private) because we need it in DatabaseController
	 protected static final class DatabaseActionInsert implements IDatabaseAction{
		private String tableName;
		private Boolean addMany;
		private Object[] objectsToAdd;
		
		// this performs the action we had in DatabaseController for .INSERT
		// returns a boolean (Boolean) as an Object (because we implement an interface we have to be general)
		@Override
		public Object getDatabaseAction(Object[] params) {
			// add input check - I can't be bothered to such an extent
			if(params.length != 3)
				throw new IllegalArgumentException("Amount of parameters is not 3 in getDatabaseAction (INSERT)");
			// set class fields:
			// *again - we might need to test input for validity, but it's too much for me right now
			setTableName((String)params[0]);
			setAddMany((Boolean)params[1]);
			setObjectsToAdd((Object[])params[2]); 

			
			String addToTable ="INSERT INTO " +DatabaseOperationsMap.SCHEMA_EKRUT+"."+getTableName()+ " VALUES ";
			// we need:
			// SQL format set in each logic class
			for(Object o : getObjectsToAdd()) {
					
				String currentAddToTable = (new StringBuilder(addToTable)).append(o.toString()).append(";").toString();
				printDebug("Sending query to SQL: " + currentAddToTable);
				if(!DatabaseController.executeQuery(currentAddToTable)) {
					// we need this line because we need getMany AFTER we delete it in "cleanUp()", 
					// and we clean up because I used fields where I shouldn't have
					Boolean tmpAddMany = getAddMany();
					cleanUp(); // careful with this line
					if(tmpAddMany) {
						// TODO: add granularity (idea - return a list of objects NOT YET INSERTED)
						return false;
					}
					return false; 
				}
			}
			cleanUp(); // careful with this line
			return true;
		}

		// after action - clean up static stuff
		public void cleanUp() {
			tableName = null;
			addMany = null;
			objectsToAdd = null;
			
		}
		
		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		
		public Boolean getAddMany() {
			return addMany;
		}

		public void setAddMany(Boolean addMany) {
			this.addMany = addMany;
		} 
		
		public Object[] getObjectsToAdd() {
			return objectsToAdd;
		}

		public void setObjectsToAdd(Object[] objectsToAdd) {
			this.objectsToAdd = objectsToAdd;
		}


		
	}
	
	 protected static final class DatabaseActionSelectForLogin implements IDatabaseAction{
		private String tableName;

		// this performs the action we had in DatabaseController for .INSERT
		// returns a boolean (Boolean) as an Object (because we implement an interface we have to be general)
		@Override
		public Object getDatabaseAction(Object[] params) {
			// add input check - I can't be bothered to such an extent

			// input check:
			ArrayList<Class<?>> typesList = new ArrayList<>();
			typesList.add(String.class);
			typesList.add(String[].class);
			
			// validate input
			if(!TypeChecker.validate(params, typesList, 0)) {
				throw new IllegalArgumentException("Invalid input in getDatabaseAction for action: " +
													DatabaseOperation.USER_LOGIN + " (wrong input types)");
			}
			
			String user, pass;
			tableName = (String)params[0];
			String[] up = (String[])params[1];
			if(up.length != 2) {
				throw new IllegalArgumentException("Invalid input in getDatabaseAction for action: " + 
													DatabaseOperation.USER_LOGIN + " (size of user-password array not 2)");
			}
			
			user = up[0];
			pass = up[1];

			String sqlQuery ="SELECT * FROM " +DatabaseOperationsMap.SCHEMA_EKRUT+"."+tableName+
			 " WHERE username = \"" + user + "\" AND password = \"" + pass + "\";";
			
			printDebug("Sending query to SQL: " + sqlQuery);

			
			ResultSet queryResult  = DatabaseController.executeQueryWithResults(sqlQuery, null);
			SystemUser connectedUser = null;
			try {
				queryResult.next();
				  if(queryResult.getRow() != 0)
				  {
					  // get fields from DB
					  Integer idNew= queryResult.getInt(1);
					  String fname = queryResult.getString(2);
					  String lname = queryResult.getString(3);
					  String email = queryResult.getString(4);
					  String fone = queryResult.getString(5);
					  String cc = queryResult.getString(6);
					  String retUser = queryResult.getString(7);
					  String retPass = queryResult.getString(8);
					  connectedUser = new SystemUser(idNew, fname, lname, fone, email, cc, retUser, retPass);
				  }
				  queryResult.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return connectedUser; 
			
		}


	}
	
	private static HashMap<DatabaseOperation, IDatabaseAction> map = 
			new HashMap<DatabaseOperation, IDatabaseAction>(){/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			{
				this.put(DatabaseOperation.INSERT, new DatabaseActionInsert());
				this.put(DatabaseOperation.USER_LOGIN, new DatabaseActionSelectForLogin());
				

			}};

	
	public static HashMap<DatabaseOperation, IDatabaseAction> getMap() {
		return map;
	}
	
}
