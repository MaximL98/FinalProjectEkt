package client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/*
 * Rotem: I added (1.13) a field event in this class - we need it to hide the correct window when sending user back to login!
 * 
 * 
 * 
 *  * Important: IF YOU USE THIS ANYWHERE WHERE YOU KEEP STATIC VARIABLES - REMEMBER TO RESET THEM ON EACH INITIALIZE!
 *  
 *  
 *  
 */

public class InactivityChecker implements Runnable {

    private long inactivityThreshold; // Threshold for inactivity in milliseconds
    private long lastActivityTime; // Timestamp of the user's last activity
    private boolean running; // Flag to indicate whether the inactivity checker is running
    private ActionEvent eventForStageClose;
    
    public InactivityChecker(long inactivityThreshold, ActionEvent event) {
        this.inactivityThreshold = inactivityThreshold;
        this.lastActivityTime = System.currentTimeMillis();
        this.running = true;
        if(event == null) {
        	throw new IllegalArgumentException("ActionEvent passed to InactivityChecker is NULL");
        }
        // Rotem added the event!
        this.eventForStageClose = event;
    }

    public void updateActivityTime() {
        // Update the timestamp of the user's last activity
        this.lastActivityTime = System.currentTimeMillis();
    }

    public void stop() {
        // Stop the inactivity checker
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            long elapsedTime = System.currentTimeMillis() - lastActivityTime;
            if (elapsedTime > inactivityThreshold) {
                // User has been inactive for too long - log them out
                logoutUser();
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                // Handle exception
            }
        }
    }

    /**
     * ROTEM: added (7.1) a server request for log-out (I assume this function is called when a user is connected)
     * Rotem 1.13 black dark friday - Fixed this to NOT EXIT but to just log the user out - it moves you to the login menu.
     * 
     * Important: IF YOU USE THIS ANYWHERE WHERE YOU KEEP STATIC VARIABLES - REMEMBER TO RESET THEM ON EACH INITIALIZE!
     */
    private void logoutUser() {
    	System.out.println("Inactive for 5 min. you have been logged out.");
		if(ClientController.getCurrentSystemUser() != null) {
			ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
			System.out.println("Loading login page (OL)");
			// use the event pointer to shut down current window
			((Node)eventForStageClose.getSource()).getScene().getWindow().hide(); 
			// and load the login window
			Stage primaryStage = new Stage();
			WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);
			primaryStage.show();
		}
    	/*try {

			
			ClientUI.clientController.client.closeConnection();
			// TODO:
			// move the client to the login page
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.exit(0);*/
    }
}

