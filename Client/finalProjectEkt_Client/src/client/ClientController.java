package client;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import common.SCCP;
import common.ServerClientRequestTypes;
import logic.Product;
import logic.Role;
import logic.SystemUser;
import ocsf.server.ConnectionToClient;

public class ClientController
{
  public static int DEFAULT_PORT ;
  public static ArrayList<ConnectionToClient> clients = new ArrayList<ConnectionToClient>();
  public static SCCP responseFromServer = new SCCP(); 
  private static Configuration launchConfig = null;
  
  // Controller specific fields - TODO: move these to dedicated controllers, and use setters to these controllers here (I will show example )
  private static SystemUser connectedSystemUser = null;
  public static ArrayList<String> CurrentProductCategory = new ArrayList<>();
  //Map that holds the current cart contents of the user
  public static HashMap<String,Integer> currentUserCart = new HashMap<>();
  
  ////// Dima 30/12 20:00
  public static HashMap<String,Product> getProductByID = new HashMap<>();
  ////////////////////////////////////////////////////////////////////
  //Max 7/1-----------------------------------------------------------------------//
  public static Integer orderNumber = 8; //for now, change later
  public static HashMap<Integer, ArrayList<String>> userOrders = new HashMap<>();
  //------------------------------------------------------------------------------//
  public static ArrayList<Product> arrayOfAddedProductsToGridpane = new ArrayList<>();

  public static long orderCounter = 5;   // TODO: remove this
  
  public static Double orderTotalPrice = new Double(0.0);
  public static HashMap<Product, Double> cartPrice = new HashMap<>();
  public EKTClient client;
  private static Role currentUserRole = null;
  
  /////////////Dima 6/1/2023 21:00
  private static ArrayList<String> machineID_AndReportType = new ArrayList<>();
  private static ArrayList<LocalDate> requestedOrderDates = new ArrayList<>();

  // Rotem added for now (8.1)
  public static String _EkCurrentMachineName;


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

	public static Configuration getLaunchConfig() {
		return launchConfig;
	}

	public static void setLaunchConfig(Configuration launchConfig) {
		ClientController.launchConfig = launchConfig;
	}

	public static void sendLogoutRequest() {
		System.out.println("Logout operation started.");
		if( getCurrentSystemUser() != null) {
			System.out.println("Processing a log-out request from client (user="+getCurrentSystemUser().getUsername()+").");
			ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.LOGOUT, getCurrentSystemUser().getUsername()));
		}
	}

	public static ArrayList<String> getMachineID_AndReportType() {
		return machineID_AndReportType;
	}

	public static void setMachineID_AndReportType(ArrayList<String> machineID_AndReportType) {
		ClientController.machineID_AndReportType = machineID_AndReportType;
	}

	public static ArrayList<LocalDate> getRequestedOrderDates() {
		return requestedOrderDates;
	}

	public static void setRequestedOrderDates(ArrayList<LocalDate> requestedOrderDates) {
		ClientController.requestedOrderDates = requestedOrderDates;
	}


}
//End of ConsoleChat class
