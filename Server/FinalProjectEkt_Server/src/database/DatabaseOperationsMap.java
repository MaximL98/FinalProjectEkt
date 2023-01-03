package database;

import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import logic.OnlineOrder;
import logic.Product;
import logic.Promotions;
import logic.SystemUser;
import logic.OnlineOrder.Status;
import logic.OnlineOrder.Type;

public class DatabaseOperationsMap {
	private static String SCHEMA_EKRUT = "ektdb";

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
	
	 //Return currently logged in user back to client
	 protected static final class DatabaseActionSelectForLogin implements IDatabaseAction{
		private String tableName;

		// this performs the action we had in DatabaseController for .INSERT
		// returns a boolean (Boolean) as an Object (because we implement an interface we have to be general)
		@Override
		public Object getDatabaseAction(Object[] params) {
			// add input check - I can't be bothered to such an extent

			String user, pass;
			tableName = (String)params[0];
			String[] up = (String[])params[1];
			user = up[0];
			pass = up[1];

			String sqlQuery ="SELECT * FROM " +DatabaseOperationsMap.SCHEMA_EKRUT+"."+tableName+
			 " WHERE username = \"" + user + "\" AND password = \"" + pass + "\";";
			ResultSet queryResult  = DatabaseController.executeQueryWithResults(sqlQuery, null);
			Boolean flag = false;
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
					  
					  String role = queryResult.getString(9);
					  flag = true;
					  connectedUser = new SystemUser(idNew, fname, lname, fone, email, cc, retUser, retPass, role.toLowerCase());
				  }
				  queryResult.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return connectedUser; 
			
		}


	}
	 
	 //Class which is used to return a result set of all products with the category name categoryName[0] 
	 protected static final class DatabaseActionSelectForFetchProducts implements IDatabaseAction {
        private String tableName;

        @Override
        public Object getDatabaseAction(Object[] categoryName) {
            tableName = "product";
            String sqlQuery;
	        String productCategory = (String)categoryName[0];
	        if (productCategory.equals("ALL PRODUCTS")) {
	        	sqlQuery = "SELECT * FROM " + DatabaseOperationsMap.SCHEMA_EKRUT + "." + tableName;
	        	System.out.println(sqlQuery);
	        }
	        else {
	        	sqlQuery = "Select * FROM " + DatabaseOperationsMap.SCHEMA_EKRUT + "." +
	        			tableName + " Where category = \"" + productCategory + "\" OR subCategory =" + 
	    	            " \"" + productCategory + "\";";
	        }
	
	        //Uses simpler version of execute query with one input string variable (the requested sql query)
	        ResultSet fetchProductsResultSet = DatabaseController.executeQueryWithResults(sqlQuery, null);
	        
	        ArrayList<Product> arrayOfProducts = new ArrayList<>();
	        try {
	        	while (fetchProductsResultSet.next()) {
	        		String productID = fetchProductsResultSet.getString("productID");
					
					String productName = fetchProductsResultSet.getString("productName");
					
					String costPerUnit = fetchProductsResultSet.getString("costPerUnit");
					
					String category = fetchProductsResultSet.getString("category");
					
					String subCategory = fetchProductsResultSet.getString("subCategory");
					
					Product tempProduct = new Product(productID, productName, costPerUnit, category, subCategory);
	        		
					System.out.println(tempProduct.toString());
					
					arrayOfProducts.add(tempProduct);
	        	}
	        }catch (SQLException sqle) {
	        	sqle.printStackTrace();
	        }
	        return arrayOfProducts;
        }

    }
	 

		protected static final class DatabaseActionUpdateForUpdateOnlineOrders implements IDatabaseAction {

			private String ONLINE_ORDERS_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "online_order";
			private String ORDER_TYPES_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "order_type";
			private String ORDER_STATUSES_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "order_status";

			@Override
			public Object getDatabaseAction(Object[] params) {
				if (!(params[0] instanceof Object[]))
					throw new InvalidParameterException("Error: expected array of Objects.");
				Object[] orders = (Object[]) params[0];
				for (Object o : orders) {
					if (!(o instanceof OnlineOrder))
						throw new InvalidParameterException("Error: expected array of Objects that includes OnlineOrders.");
					OnlineOrder order = (OnlineOrder) o;
					String sqlQuery = "UPDATE " + ONLINE_ORDERS_TABLE + " SET " + "typeId = (select statusId FROM "
							+ ORDER_TYPES_TABLE + "  where typeName = \"" + order.getType().name() + "\"), "
							+ "statusId = (select statusId FROM " + ORDER_STATUSES_TABLE + "  where statusName = \""
							+ order.getStatus().name() + "\"), " + "deliveryTime = '"
							+ Timestamp.valueOf(order.getDeliveryTime()).toString() + "' " + "WHERE orderId = \""
							+ order.getOrderID() + "\";";
					boolean success = DatabaseController.executeQuery(sqlQuery);
					if (!success)
						return success;
				}
				return true;
			}

		}

		// Class which is used to return a result set of all online orders with the
		// status name in orderFilters[0]
		protected static final class DatabaseActionSelectForFetchOnlineOrders implements IDatabaseAction {
			private String ONLINE_ORDERS_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "online_order";
			private String ORDERS_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "orders";
			private String ORDER_TYPES_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "order_type";
			private String ORDER_STATUSES_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "order_status";
			private String MACHINES_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "machine";
			private String LOCATIONS_TABLE = DatabaseOperationsMap.SCHEMA_EKRUT + "." + "locations";

			@Override
			public Object getDatabaseAction(Object[] orderFilters) {
				String sqlQuery = "Select " + ORDERS_TABLE
						+ ".*, typeName ,statusName, date_recieved, deliveryTime, locationName " + "FROM "
						+ ONLINE_ORDERS_TABLE + " " + "LEFT JOIN " + ORDERS_TABLE + " " + "ON " + ONLINE_ORDERS_TABLE
						+ ".orderID = " + ORDERS_TABLE + ".orderID " + "LEFT JOIN " + ORDER_TYPES_TABLE + " " + "ON "
						+ ONLINE_ORDERS_TABLE + ".typeId = " + ORDER_TYPES_TABLE + ".typeId " + "LEFT JOIN "
						+ ORDER_STATUSES_TABLE + " " + "ON " + ONLINE_ORDERS_TABLE + ".statusId = " + ORDER_STATUSES_TABLE
						+ ".statusId " + "LEFT JOIN " + MACHINES_TABLE + " " + "ON " + ORDERS_TABLE + ".machineId = "
						+ MACHINES_TABLE + ".machineId " + "LEFT JOIN " + LOCATIONS_TABLE + " " + "ON " + MACHINES_TABLE
						+ ".locationId = " + LOCATIONS_TABLE + ".locationID";
				if (orderFilters.length > 0) {
					String statusName = ((String[]) orderFilters[0])[0];
					sqlQuery += " WHERE statusName = \"" + statusName + "\"";
				}

				// Uses simpler version of execute query with one input string variable (the
				// requested sql query)
				ResultSet fetchOnlineOrdersResultSet = DatabaseController
						.executeQueryWithResults(sqlQuery, null);

				ArrayList<OnlineOrder> orders = new ArrayList<>();
				try {
					while (fetchOnlineOrdersResultSet.next()) {
						String orderID = fetchOnlineOrdersResultSet.getString("orderID");
						Integer totalAmount = fetchOnlineOrdersResultSet.getInt("total_amount");
						String attribute = fetchOnlineOrdersResultSet.getString("attribute");
						Type type = Type.valueOf(fetchOnlineOrdersResultSet.getString("typeName"));
						Status status = Status.valueOf(fetchOnlineOrdersResultSet.getString("statusName"));
						LocalDate dateReceived = fetchOnlineOrdersResultSet.getDate("date_recieved").toLocalDate();
						LocalDateTime deliveryTime = null;
						Timestamp deliveryTimeStamp = fetchOnlineOrdersResultSet.getTimestamp("deliveryTime");
						if (deliveryTimeStamp != null)
							deliveryTime = deliveryTimeStamp.toLocalDateTime();
						String location = fetchOnlineOrdersResultSet.getString("locationName");
						OnlineOrder tempOrder = new OnlineOrder(orderID, totalAmount, attribute, location, dateReceived,
								deliveryTime, type, status);
						System.out.println(tempOrder.toString());

						orders.add(tempOrder);
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
				return orders;
			}

		}

		protected static final class DatabaseActionSelectPromotionNames implements IDatabaseAction {

			@Override
			public Object getDatabaseAction(Object[] params) {
				String promotionNam = (String) params[0];
				ResultSet fetchPromotionNames = DatabaseController.executeQueryWithResults(promotionNam, null);
				return fetchPromotionNames;
			}
		}

		// Object[] params contains just the sqlQuery at the [0] index
		protected static final class DatabaseActionSelectPromotion implements IDatabaseAction {

			@Override
			public Object getDatabaseAction(Object[] params) {
				String promotionNam = (String) params[0];
				//String sqlQuery = "SELECT * FROM promotions WHERE promotionName = '\" + promotionNam + \"';";
				ResultSet fetchPromotionNames = DatabaseController.executeQueryWithResults(promotionNam, null);
				ArrayList<Promotions> arrayOfPromotions = new ArrayList<>();
				try {
					while (fetchPromotionNames.next()) {
						String promotionName = fetchPromotionNames.getString("promotionName");

						String promotionDescription = fetchPromotionNames.getString("promotionDescription");

						int locationId = Integer.parseInt(fetchPromotionNames.getString("locationId"));

						String productID = fetchPromotionNames.getString("productID");

						String discountPercentage = fetchPromotionNames.getString("discountPercentage");

						Date startDate = fetchPromotionNames.getDate("startDate");
						Date endDate = fetchPromotionNames.getDate("endDate");

						boolean promotionStatus = fetchPromotionNames.getBoolean("promotionStatus");

						Promotions tempPromtions = new Promotions(promotionName, locationId, promotionDescription,
								productID, null, discountPercentage, startDate, endDate, promotionStatus);

						System.out.println(tempPromtions.toString());

						arrayOfPromotions.add(tempPromtions);

					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
				return arrayOfPromotions;
			}
		}

	
	 private static HashMap<DatabaseOperation, IDatabaseAction> map = new HashMap<DatabaseOperation, IDatabaseAction>(){
		 
		private static final long serialVersionUID = 1L;
		
		{
			this.put(DatabaseOperation.INSERT, new DatabaseActionInsert());
			this.put(DatabaseOperation.USER_LOGIN, new DatabaseActionSelectForLogin());
			this.put(DatabaseOperation.FETCH_PRODUCTS_BY_CATEGORY, new DatabaseActionSelectForFetchProducts());
			this.put(DatabaseOperation.FETCH_ONLINE_ORDERS, new DatabaseActionSelectForFetchOnlineOrders());
			this.put(DatabaseOperation.UPDATE_ONLINE_ORDERS, new DatabaseActionUpdateForUpdateOnlineOrders());
			this.put(DatabaseOperation.SELECT, new DatabaseActionSelectPromotion());
			this.put(DatabaseOperation.INSERT_PROMOTION_NAMES,  new DatabaseActionSelectPromotionNames());
		}};

	
	public static HashMap<DatabaseOperation, IDatabaseAction> getMap() {
		return map;
	}
	
}

