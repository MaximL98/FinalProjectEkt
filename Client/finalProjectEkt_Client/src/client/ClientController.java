package client;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import common.SCCP;
import logic.Product;
import logic.Role;
import logic.SystemUser;
import ocsf.server.ConnectionToClient;

public class ClientController
{
  public static int DEFAULT_PORT ;
  public static ArrayList<ConnectionToClient> clients = new ArrayList<ConnectionToClient>();
  public static SCCP responseFromServer = new SCCP(); 
  private static SystemUser connectedSystemUser = null;
  public static ArrayList<String> CurrentProductCategory = new ArrayList<>();
  //Map that holds the current cart contents of the user
  public static HashMap<String,Integer> currentUserCart = new HashMap<>();
  
  ////// Dima 30/12 20:00
  public static HashMap<String,Product> getProductByID = new HashMap<>();
  ////////////////////////////////////////////////////////////////////
  
  public static ArrayList<Product> arrayOfAddedProductsToGridpane = new ArrayList<>();

  public static long orderCounter = 5;
  public static Double orderTotalPrice = new Double(0.0);
  public static HashMap<Product, Double> cartPrice = new HashMap<>();
  public EKTClient client;
  private static Role currentUserRole = null;


  public ClientController(String host, int port) throws IOException
  {

      client= new EKTClient(host, port);

  }

  public void accept(SCCP msgToServer) 
  {
	  System.out.println(msgToServer.toString());
	  client.handleMessageFromClientUI(msgToServer);
  }

	public static SystemUser getCurrentSystemUser() {
		return connectedSystemUser;
	}
	
	public static void setCurrentSystemUser(SystemUser currentSystemUser) {
		ClientController.connectedSystemUser = currentSystemUser;
	}
	
//	  public static String getCurrentProductCategory() {
//			return CurrentProductCategory;
//	}
//
//	public static void setCurrentProductCategory(String currentProductCategory) {
//		System.out.println("im getting it from here");
//		CurrentProductCategory = currentProductCategory;
//	}

	public static void setCurrentUserRole(Role role) {
		// sets the 'role' variable in this class to the assigned value
		currentUserRole = role;
	}
	

	public static Role getCurrentUserRole() {
		// gets the 'role' variable of this class
		return currentUserRole;
	}


}
//End of ConsoleChat class
