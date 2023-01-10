package client;

import controllers.EktReportSelectFormController;

import java.io.IOException;

import controllers.ClientLoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientUI extends Application {
	public static ClientController clientController; //only one instance
	public static String serverIP;
	/**
	 * This is the main function for the client side of the EKT project.
	 * main calls the JavaFX function start, where the client-to-server connection GUI window is loaded.
	 * @param args - these are the command-line arguments passed to the client application on start-up. (currently, 1/4/23 no arguments)
	 */
	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   }

	/**
	 * Load the client login window (which has a misleading name, as it does not perform a login, but rather the handshake between the client and the server).
	 * The client login window allows the user to enter an IP address (or leave it blank for localhost) which the client will try to connect to, on port 5555.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientLoginController aFrame = new ClientLoginController(); // create the window
		// I removed this from here(just this one line, and 22 to reflect this)
		// but the mimshak loox aight
		//CEOPageController ceopc = new CEOPageController();
		
		//ceopc.start(primaryStage);

		aFrame.start(primaryStage);
	}

	public static void connectToServer() throws IOException{
		clientController= new ClientController(serverIP, 5555);
	}
	
	
}
