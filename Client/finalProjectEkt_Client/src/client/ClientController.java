package client;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import common.SCCP;
import logic.Product;
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
  public static HashMap<Product,Integer> currentUserCart = new HashMap<>();
  public static ArrayList<Product> arrayOfAddedProductsToGridpane = new ArrayList<>();

  public static long orderCounter = 5;
  public static Double orderTotalPrice = new Double(0);
  
public EKTClient client;

  public ClientController(String host, int port) 
  {
    try 
    {
      client= new EKTClient(host, port);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"+ " Terminating client.");
      System.exit(1);
    }
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
}
//End of ConsoleChat class
