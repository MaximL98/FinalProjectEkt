package database;

import java.sql.*;

//import com.mysql.cj.admin.ServerController;

/*
 * 
 * Extremely IMPORTANT:
 * 
 * READ the driver part in line 58 before screaming
 * 
 */


public class DatabaseController {
	// constants
	// URL is defined with ssl disabled because the java version is too weak to handle it otherwise 
	// (sergoy: don't use java 19 because it's not buggy and I love bugs, I love them so much I eat them and shit them daily)
	private static final String URL = "jdbc:mysql://localhost/ekrutdb?serverTimezone=IST&sslMode=DISABLED&allowPublicKeyRetrieval=true";	// Rotem -> read line 28
	//private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

	private static String dbName = "root";
	private static String dbPassword;

	// Singleton instance of the class.
	private static DatabaseController instance;
	// TODO:
	// for test only
	private static Connection con;
	private static String schemaName="ektdb";

	private DatabaseController() {
	
		try {
			// R o t e m -> my java doesn't seem to want this (red error n i g)
			//Class.forName(DRIVER_NAME).getDeclaredConstructor().newInstance();
			
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}
		try {
			con = DriverManager.getConnection(URL, getDatabaseUserName(), getDatabasePassword());
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
	}

	public static DatabaseController getInstance() {
		if (instance == null)
			instance = new DatabaseController();
		return instance;
	}

	public static Connection getConnection() {
		try {
			if(con != null && !con.isClosed())
				return con;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// If connection isnt open, open it
		try {
			// Rotem -> my java doesn't seem to want this (red error ni)
			//Class.forName(DRIVER_NAME).getDeclaredConstructor().newInstance();
			
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			con = DriverManager.getConnection(URL, getDatabaseUserName(), getDatabasePassword());
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return con;
	}
	
	public static boolean checkLoginCredentials() throws SQLException {
		try {
			Connection tmp_con = DriverManager.getConnection(URL, getDatabaseUserName(), getDatabasePassword());
			if(tmp_con != null) {
				tmp_con.close();
				return true;
			}
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// password is incorrect
			if(e.getMessage().contains("Access denied for user "))
				return false;
			// error (TODO)
			throw e;
		}
	}
	
	public static void closeDBController() {
		try {
			getConnection().close();
			System.out.println("SQL controller is closed: " + con.isClosed());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean executeQuery(String sqlStatement, Object[] params) {
		con = getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sqlStatement);
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i+1, params[i]);
			}
			//System.out.println("prepared statement : " + ps.toString());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {

			System.out.println("Query execution failed.");
			System.out.println("Exception message : " + e.getMessage());

			return false; 
		}
	}


	public static boolean executeQuery(String sqlStatement) {
		con = getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sqlStatement);

			//System.out.println("prepared statement : " + ps.toString());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {

			System.out.println("Query execution failed.");
			System.out.println("Exception message : " + e.getMessage());

			return false; 
		}
	}
	
	public static ResultSet executeQueryWithResults(String sqlStatement, Object[] params) {
		con = getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sqlStatement);
			if(params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i+1, params[i]);
				}
			}
			System.out.println("SQL to execute: "+sqlStatement);
			//System.out.println("prepared statement : " + ps.toString());

			return ps.executeQuery();
		} catch (Exception e) {

			System.out.println("Query execution failed.");
			System.out.println("Exception message : " + e.getMessage());

			return null;
		}
	}
	
	public static String getDatabaseUserName() {
		return dbName;
	}

	public static void setDatabaseUserName(String databaseUserName) {
		dbName = databaseUserName;
	}

	public static String getDatabasePassword() {
		return dbPassword;
	}

	public static void setDatabasePassword(String databasePassword) {
		dbPassword = databasePassword;
	}

	
	// please, just ignore this one, I left it only to keep track of how it was done before the map existed.
	public static boolean handleQuery(DatabaseOperation operation, String tableName, Boolean addMany,
			Object[] objectsToAdd) {
		
		if(operation.equals(DatabaseOperation.INSERT)) {
			// add
			//(?, ?, ?, ?, ?, ?, ?, ?)
			String addToTable =
					"INSERT INTO " +getSchemaName()+"."+tableName+ " VALUES ";
			// sql format set in each logic class
			for(Object o : objectsToAdd) {
				
				String currentAddToTable = (new StringBuilder(addToTable)).append(o.toString()).append(";").toString();
				System.out.println("Tring to sql:");
				System.out.println(currentAddToTable);
				if(!executeQuery(currentAddToTable)) {
					return false; // TODO: add granularity
				}
			}
			return true;

		}
		return false;
	}
	
	public static Object handleQuery(DatabaseOperation operation, Object[] params) {
		// TODO: add error checking
		if(!DatabaseOperationsMap.getMap().containsKey(operation)) {
			throw new UnsupportedOperationException("SQL Controller does not support the operation " + operation + ". Contact your doctor about Levi-tra today!");
		}
		return DatabaseOperationsMap.getMap().get(operation).getDatabaseAction(params);
	}

	public static String getSchemaName() {
		return schemaName;
	}
	
	
}
