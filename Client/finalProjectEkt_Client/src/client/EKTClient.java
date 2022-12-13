package client;

import ocsf.client.*;
import common.SCCP;
import logic.Customer;

import java.io.*;


public class EKTClient extends AbstractClient
{

  public static Customer customer = new Customer(null,null,null, null, null, null, null, null);
  public static boolean awaitResponse = false;
  public static SCCP comm = new SCCP();

  
  public EKTClient(String host, int port) throws IOException 
  {
	  super(host, port); //Call the superclass constructor
      openConnection();

  }

  public void handleMessageFromServer(Object msg) 
  {
	  System.out.println("--> handleMessageFromServer");
     
	  awaitResponse = false;
	  // Rotem -- heavy modification so that it will finally work!

	  // we don't care what message it is, as long as it's wrapped in our defined class
	  if(msg instanceof SCCP) {
		  SCCP tmp = (SCCP)msg;
		  System.out.println("Got an object from server " + " object looks like: " +  tmp);
		  
		  // a common object for communications between server and client
		  comm.setRequestType(tmp.getRequestType());
		  comm.setMessageSent(tmp.getMessageSent());
		  
	  }
	  else {
		  // error! (invalid input to client)
		  comm.setRequestType(null);
		  comm.setMessageSent(null);
	  }
  }


  // TODO: this is a copy of the above method but with a different parameter
  // new method can send the dedicated object from client to server
  public void handleMessageFromClientUI(SCCP message)  
  {
    try
    {
    	openConnection();//in order to send more than one message
       	awaitResponse = true;
    	sendToServer(message);
		// wait for response
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    catch(IOException e)
    {
    	e.printStackTrace();
    	ClientController.display("Could not send message to server: Terminating client."+ e);
      quit();
    }
  }
  
  //This method terminates the client.
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {
    	System.err.println("Exception in EKTClient.quit: " + e.getMessage());
    	e.printStackTrace();
    }
    System.exit(0);
  }
}
