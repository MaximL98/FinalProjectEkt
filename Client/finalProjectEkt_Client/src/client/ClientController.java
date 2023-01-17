package client;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


import common.SCCP;
import common.ServerClientRequestTypes;
import entityControllers.OrderController;
import logic.Product;
import logic.Role;
import logic.SystemUser;
import logic.superProduct;

public class ClientController
{
  public static int DEFAULT_PORT ;
  
  // commented out as it is not in use:
  //public static ArrayList<ConnectionToClient> clients = new ArrayList<ConnectionToClient>();
  
  // Controller specific fields - TODO: move these to dedicated controllers, and use setters to these controllers here (I will show example )
  public static SCCP responseFromServer = new SCCP(); 
  private static Configuration launchConfig = null;
  private static SystemUser connectedSystemUser = null; // ClientController.getconnectedSystemUser().getID()
  public EKTClient client;
  private static Role currentUserRole = null;

  // rotem 1.12.23
  private static Boolean customerIsSubsriber=false;
  
  // dohot related fields
  
  /////////////Dima 6/1/2023 21:00
  private static ArrayList<String> machineID_TypeOfReport_Dates = new ArrayList<>();
  private static ArrayList<LocalDate> requestedOrderDates = new ArrayList<>();
  
  /////////////Dima 11/1/2023 11:50
  private static String currentUserRegion;

  // machine related fields
  
  // Rotem added for now (8.1)
  private static String EKCurrentMachineName;
  // Rotem added also (10.1)
  private static int EKCurrentMachineID;
  // Maxim added for now (13.1)
  private static String OLCurrentMachineName;
  // Maxim added also (13.1)
  private static int OLCurrentMachineID;
  

  
  // one buggish lookin chick
  private static boolean fastRecognitionToggle = false;
  private static String fastRecognitionUserName = null;
  private static String fastRecognitionPassword = null;
  

  

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

	public static ArrayList<String> getMachineID_TypeOfReport_Dates() {
		return machineID_TypeOfReport_Dates;
	}

	public static void setMachineID_TypeOfReport_Dates(ArrayList<String> machineID_AndReportType) {
		ClientController.machineID_TypeOfReport_Dates = machineID_AndReportType;
	}

	public static ArrayList<LocalDate> getRequestedOrderDates() {
		return requestedOrderDates;
	}

	public static void setRequestedOrderDates(ArrayList<LocalDate> requestedOrderDates) {
		ClientController.requestedOrderDates = requestedOrderDates;
	}

	public static String getCurrentUserRegion() {
		return currentUserRegion;
	}

	public static void setCurrentUserRegion(String currentUserRegion) {
		ClientController.currentUserRegion = currentUserRegion;
	}

	// connected to rotem 1.12.23
	public static Boolean getCustomerIsSubsriber() {
		return customerIsSubsriber;
	}

	public static void setCustomerIsSubsriber(Boolean customerIsSubsriber) {
		ClientController.customerIsSubsriber = customerIsSubsriber;
	}

	/**
	 * This method is used by the login manager to reset user-specific variables
	 */
	public static void resetVars() {
		OrderController.resetVars();
		// TODO Auto-generated method stub
		  connectedSystemUser = null; // ClientController.getconnectedSystemUser().getID()

		  currentUserRole = null;
		  
		  /////////////Dima 6/1/2023 21:00
		  machineID_TypeOfReport_Dates = new ArrayList<>();
		  requestedOrderDates = new ArrayList<>();
		  
		  // Maxim added for now (13.1)
		  setOLCurrentMachineName(null);
		  // Maxim added also (13.1)
		  setOLCurrentMachineID(0);

		  // rotem 1.12.23
		  customerIsSubsriber=null;

	}

	public static boolean isFastRecognitionToggle() {
		return fastRecognitionToggle;
	}

	public static void setFastRecognitionToggle(boolean fastRecognitionToggle) {
		ClientController.fastRecognitionToggle = fastRecognitionToggle;
	}

	public static String getFastRecognitionUserName() {
		return fastRecognitionUserName;
	}

	public static void setFastRecognitionUserName(String fastRecognitionUserName) {
		ClientController.fastRecognitionUserName = fastRecognitionUserName;
	}

	public static String getFastRecognitionPassword() {
		return fastRecognitionPassword;
	}

	public static void setFastRecognitionPassword(String fastRecognitionPassword) {
		ClientController.fastRecognitionPassword = fastRecognitionPassword;
	}

	

	public static String getEKCurrentMachineName() {
		return EKCurrentMachineName;
	}

	public static void setEKCurrentMachineName(String eKCurrentMachineName) {
		EKCurrentMachineName = eKCurrentMachineName;
	}

	public static int getEKCurrentMachineID() {
		return EKCurrentMachineID;
	}

	public static void setEKCurrentMachineID(int eKCurrentMachineID) {
		EKCurrentMachineID = eKCurrentMachineID;
	}

	

	public static String getOLCurrentMachineName() {
		return OLCurrentMachineName;
	}

	public static void setOLCurrentMachineName(String oLCurrentMachineName) {
		OLCurrentMachineName = oLCurrentMachineName;
	}

	public static int getOLCurrentMachineID() {
		return OLCurrentMachineID;
	}

	public static void setOLCurrentMachineID(int oLCurrentMachineID) {
		OLCurrentMachineID = oLCurrentMachineID;
	}




}
//End of ConsoleChat class
