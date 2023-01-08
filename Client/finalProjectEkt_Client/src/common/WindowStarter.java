package common;

import java.io.IOException;

import client.ClientController;
import client.ClientUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowStarter {
	
	// method to save lines - opens new window
	// CHANGE:
	// Stage does not call show here, Mister! (yes it was fucker but maybe it seemed too personal to the wrong personnel, so, 
	//nothing personnel.
	// )
	
	public static void createWindow(Stage primaryStage, Object classObject, 
			String fxmlAddress, String cssAddress, String windowTitle) 
					 {
		Parent root;
		try {
			root = FXMLLoader.load(classObject.getClass().getResource(fxmlAddress));
			Scene scene = new Scene(root);
			if(cssAddress != null)
				scene.getStylesheets().add(classObject.getClass().getResource(cssAddress).toExternalForm());
			
			primaryStage.setTitle(windowTitle);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			
			primaryStage.setOnCloseRequest(we -> 
			{
				System.out.println("Pressed the X button."); 
				if(ClientController.getCurrentSystemUser() != null) {
					System.out.println("Logging off user " +  ClientController.getCurrentSystemUser().getUsername());
					ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
				}
				System.out.println("Shutting down client."); 
				System.exit(0);
			}
			);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	}
	
	
	
}
