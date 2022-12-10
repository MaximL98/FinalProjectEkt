package Server;

import gui.ServerPortFrameController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	private static EktServer serverObject;

	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub				  		
		ServerPortFrameController aFrame = new ServerPortFrameController(); // create StudentFrame
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if(serverObject != null) {
                	System.out.println("X button has been clicked!");
                		serverForcedShutdown();
                }
            }
        }); 
		aFrame.start(primaryStage);
	}
	
	public static void runServer(String p)
	{
		 int port = 0; //Port to listen on

	        try
	        {
	        	port = Integer.parseInt(p); //Set port to 5555
	          
	        }
	        catch(Throwable t)
	        {
	        	System.out.println("ERROR - Could not connect!");
	        }
	    	
	        serverObject = new EktServer(port);
	        
	        try 
	        {
	          getEktServerObject().listen(); //Start listening for connections
	        } 
	        catch (Exception ex) 
	        {
	        	System.out.println("Exception in calling .listen() on server object: "+ ex);
	        	System.out.println("ERROR - Could not listen for clients!");
	        }
	}

	public static EktServer getEktServerObject() {
		return serverObject;
	}
	
	public static void serverForcedShutdown() {
		if(getEktServerObject() != null)
			getEktServerObject().handleForcedShutdown();
	}
	

}
