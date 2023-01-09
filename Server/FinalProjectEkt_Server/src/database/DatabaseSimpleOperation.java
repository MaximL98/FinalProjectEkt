package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseSimpleOperation {
	public static boolean executeQuery(String sqlStatement, Object[] params) {
		PreparedStatement ps;
		try {
			ps = DatabaseController.getConnection().prepareStatement(sqlStatement);
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
		// we don't need ps or conn here
		try {
			//System.out.println("prepared statement : " + ps.toString());
			return DatabaseController.getConnection().prepareStatement(sqlStatement).executeUpdate() > 0;
		} catch (Exception e) {

			System.out.println("Query execution failed.");
			System.out.println("Exception message : " + e.getMessage());

			return false; 
		}
	}
	
	public static ResultSet executeQueryWithResults(String sqlStatement, Object[] params) {
		
		PreparedStatement ps;
		try {
			System.out.println(sqlStatement);
			ps = DatabaseController.getConnection().prepareStatement(sqlStatement);
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
}
