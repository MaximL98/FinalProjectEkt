package common;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import client.ClientUI;

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

    private void logoutUser() {
    	System.out.println("Innactive for 5 min. you have been logged out.");
    	try {
			ClientUI.clientController.client.closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.exit(0);
    }
}

