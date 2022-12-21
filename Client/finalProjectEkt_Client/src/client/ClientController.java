package client;
import java.io.*;
import java.util.ArrayList;

import common.SCCP;
import ocsf.server.ConnectionToClient;

public class ClientController
{
   public static int DEFAULT_PORT ;
   public static ArrayList<ConnectionToClient> clients = new ArrayList<ConnectionToClient>();
  public static SCCP responseFromServer = new SCCP(); 
  
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
	  client.handleMessageFromClientUI(msgToServer);
  }

}
//End of ConsoleChat class
