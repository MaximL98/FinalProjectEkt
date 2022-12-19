package client;

import controllers.CEOPageController;
import controllers.ClientLoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientUI extends Application {
	public static ClientController clientController; //only one instance
	public static String serverIP;
	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	//Start
	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientLoginController aFrame = new ClientLoginController(); // create the window
		CEOPageController ceopc = new CEOPageController();
		
		ceopc.start(primaryStage);

		//aFrame.start(primaryStage);
	}

	public static void connectToServer() {
		clientController= new ClientController(serverIP, 5555);
	}
	
	
}
