package Server;

import gui.ServerPortController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	private static EktServer serverObject;

	/**
	 * This is the main function for the server side of the EKT project.
	 * main calls the JavaFX function start, where the main server GUI window is loaded.
	 * @param args - these are the command-line arguments passed to the server application on start-up. (currently, 1/4/23 no arguments)
	 */
	public static void main(String args[])
	   {   
		try {
		 launch(args);
		}catch(Exception ex) {
			System.out.println("JavaFX Application ServerUI threw an exception and stopped running. Exception: " + ex.getMessage());
			ex.printStackTrace();
		}
	  } 
	
	/**
	 * This method override the JavaFX start method - it calls the server-side GUI controller and loads the window shown to the user.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerPortController serverGuiWindow = new ServerPortController();
		// override the X button, (possible option: to disable it, like this:
		//
		// if we want to only allow the X button to stay:
		// 
		//)
//		primaryStage.initStyle(StageStyle.UNDECORATED);
//		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.setOnCloseRequest(we -> {
	    	System.out.println("X button has been clicked!");

		    if(serverObject != null && serverObject.isListening()) {
		    		serverForcedShutdown();
		    }
		}); 
		serverGuiWindow.start(primaryStage);
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
	        	System.out.println("ERROR - Could not connect! Message: " + t.getLocalizedMessage());
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
		System.out.println("Server starting shutdown process");
		if(getEktServerObject() != null)
			getEktServerObject().handleForcedShutdown();
	}
	

}
