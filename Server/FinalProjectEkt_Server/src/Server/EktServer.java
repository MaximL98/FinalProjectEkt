package Server;

import java.io.IOException;

import common.SCCP;
import common.ServerClientRequestTypes;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EktServer extends AbstractServer 
{

  public EktServer(int port) 
  {
	  // create the server
    super(port);
    // create a database connection
    initDBConnector();
    
  }
  
  private void initDBConnector() {
		DatabaseController.getInstance();
  }

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   * @param 
   */
  public void handleMessageFromClient  (Object msg, ConnectionToClient client)
  {
	  // TODO: this
	  
//	 int flag=0;
//	    System.out.println("Message received: " + msg + " from " + client);
//	    for(int  i=0;i<4;i++) {
//			if(students[i].getId().equals(msg))
//			{ 
//				System.out.println("Server Found");
//				this.sendToAllClients(students[i].toString());
//				flag=1;
//			}
//		
//		}
//	    if (flag!=1) {
//			System.out.println("Not Found");
//			this.sendToAllClients("Error");
//		}        
  }
   
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	  System.out.println ("Server listening for connections on port " + getPort());

  }
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()  {
	  DatabaseController.closeDBController();
	  System.out.println ("Server has stopped listening for connections.");
  }  
  
  protected void handleForcedShutdown() {
		// TODO (probably): move the logic and the loop to a method in EktServer
	  // DONE (test)
		// handle open connections
		for(Thread c : ServerUI.getEktServerObject().getClientConnections()) {
			ConnectionToClient cConn = (ConnectionToClient)c;
			// do stuff with the connection (send message? forced shutdown? TODO: as of now it's "CRASH")
			try {
				cConn.sendToClient(new SCCP(ServerClientRequestTypes.CRASH, "Server has been forcefully shut down."));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// TODO: move this to EktServer too (just call a new method there and remove everything from here)
		// DONE (test)
		// disconnect from DB (not needed, taken care of in the serverStopped method)
		//DatabaseController.closeDBController();
		
		// close the server
		try {
			this.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
}
