package common;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import client.ClientController;
import client.ClientUI;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class WindowStarter {
	private static Long currentWindowCount = 0L;
	/*
	 * This constant relates to taking care of shutting down EK client after 20 minutes
	 */
	private static final long TIME_FOR_INACTIVITY_RESET = 20 * 60 * 1000;
//	private static final long TIME_FOR_INACTIVITY_RESET = 20 * 1000;

	// method to save lines - opens new window
	// CHANGE:
	// Stage does not call show here, Mister! (yes it was fucker but maybe it seemed too personal to the wrong personnel, so, 
	//nothing personnel.
	// )
	
	public static void createWindow(Stage primaryStage, Object classObject, 
			String fxmlAddress, String cssAddress, String windowTitle, boolean activateActivityCheck) 
					 {
		currentWindowCount++;
		// added debug print:
		System.out.println("Loading UI page="+fxmlAddress + ", with css="+cssAddress +" and title="+windowTitle);
		Parent root;
		
		try {
			if(classObject == null) {
				// hope for the best with dees 
				classObject = new Object();
			}
			root = FXMLLoader.load(classObject.getClass().getResource(fxmlAddress));
			Scene scene = new Scene(root);
			if(cssAddress != null)
				scene.getStylesheets().add(classObject.getClass().getResource(cssAddress).toExternalForm());
			Image image = new Image("controllers/Images/ekrut.png");
			primaryStage.getIcons().add(image);
			
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
			
			/*
			 * This takes care of shutting down EK client after 20 minutes!
			 */
			if(activateActivityCheck) {
				Timer timer = new Timer();

				timer.schedule(
				    new TimerTask() {
				        @Override
				        public void run() {
				        	if(ClientController.getEKCurrentMachineID() != 0 && ClientController.getCurrentSystemUser() != null &&
				        			ClientController.getCurrentSystemUser().getUsername() != null)
				        		{	
				        			Long currentCreatedNewWindow = currentWindowCount;
				        			Platform.runLater(() -> update(primaryStage, currentCreatedNewWindow));
				        			timer.cancel();
				        			timer.purge();
				        		}
				        }
	
				        // log the user out
						private Object update(Stage primaryStage, Long windowCount) {
							// if user moved window, do nothing (NOTHING!)
							if(windowCount != currentWindowCount)
								return null;
							
							// actually log the user out
							ClientController.sendLogoutRequest();
							Stage oldStage = primaryStage;
							// move to new window
							primaryStage = new Stage();
							WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationLoginFrame.fxml", null, "Login", false);
	
							primaryStage.show();
							oldStage.close();
							// TODO Auto-generated method stub
							return null;
						}
				    }, TIME_FOR_INACTIVITY_RESET
				);
				/*
				 * rotem addd./
				 */
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	}
	
	
	
}
