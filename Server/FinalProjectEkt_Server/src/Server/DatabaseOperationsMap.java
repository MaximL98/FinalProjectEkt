package Server;

import java.util.HashMap;

public class DatabaseOperationsMap {
	private static String SCHEMA_EKRUT = "ekrut";

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
				System.out.println("Writing to SQL:");
				System.out.println(currentAddToTable);
				if(!DatabaseController.executeQuery(currentAddToTable)) {
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
	
	private static HashMap<DatabaseOperation, IDatabaseAction> map = 
			new HashMap<DatabaseOperation, IDatabaseAction>(){/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			{
				this.put(DatabaseOperation.INSERT, new DatabaseActionInsert());
				
			}};

	
	public static HashMap<DatabaseOperation, IDatabaseAction> getMap() {
		return map;
	}
	
}
