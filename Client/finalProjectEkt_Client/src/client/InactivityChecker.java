package client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import common.SCCP;
import common.ServerClientRequestTypes;

public class InactivityChecker implements Runnable {

    private long inactivityThreshold; // Threshold for inactivity in milliseconds
    private long lastActivityTime; // Timestamp of the user's last activity
    private boolean running; // Flag to indicate whether the inactivity checker is running

    public InactivityChecker(long inactivityThreshold) {
        this.inactivityThreshold = inactivityThreshold;
        this.lastActivityTime = System.currentTimeMillis();
        this.running = true;
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
     */
    private void logoutUser() {
    	System.out.println("Innactive for 5 min. you have been logged out.");
    	try {
			if(ClientController.getCurrentSystemUser() != null) {
				ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
			}
			
			ClientUI.clientController.client.closeConnection();
			// TODO:
			// move the client to the login page
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.exit(0);
    }
}

