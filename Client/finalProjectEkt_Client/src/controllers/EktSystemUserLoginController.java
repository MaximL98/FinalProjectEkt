package controllers;

import java.io.IOException;
import java.util.ArrayList;
// Rotem-specific imports
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Role;
import logic.SystemUser;
import javafx.scene.control.Label;

public class EktSystemUserLoginController {
	
	private ClientController clientController;

	public SystemUser currentUser = null;
	
    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;
    
	@FXML 
	Label statusLabel;
	
	
	/**
	 * This event grabs the enter key when we are on the password field.
	 * @param ae
	 */
	@FXML
	public void onEnter(ActionEvent ae){
	   getBtnLogin(ae);
	}
	
	/*
	 * This method does nothing
	 */
	@FXML
	private void initialize() {
		ClientController.resetVars();
		// attempt to have the label re-written every few seconds. (failure)
		/*
	    ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

	    Runnable task = new Runnable() {
		    int secondsToWait = 3;
	        @Override
	        public void run() {
	            secondsToWait--;
	            if (secondsToWait == 0) {
	                exec.shutdown();
	            }
	        }
	    };
	    statusLabel.setVisible(true);
	    statusLabel.setText("Standing by for login");
	    exec.scheduleAtFixedRate(task, 1, Integer.MAX_VALUE, TimeUnit.SECONDS);
		*/
	}
	
	/**
	 * This is the login method for a system user
	 * TODO: write tests for it
	 * @param event: not used
	 */
	@SuppressWarnings("unchecked")
	/*
	 * TODO:
	 * handle not existing user
	 * 
	 * 
	 */
	
	@FXML
    public Role getBtnLogin(ActionEvent event) {
		// Rotem 1.12.23
		// hide the status label until we finish login attempt
		statusLabel.setVisible(false);
		
    	String userName, password;
    	userName = txtUsername.getText();
    	password = txtPassword.getText();
    	System.out.println(userName + " " + password);
    	    	
    	/*
    	 * TODO
    	 * Remove this test segment! (when no longer needed)
    	 */	
    	
    	// ask to connect

    	SCCP preparedMessage = new SCCP();
    	preparedMessage.setRequestType(ServerClientRequestTypes.LOGIN);
    	preparedMessage.setMessageSent(new String[] {userName, password});
    	// request the login:
		// send to server
    	System.out.println("Client: Sending login request to server as " + userName+".");
		ClientUI.clientController.accept(preparedMessage);
		// check client-side object for answer:
		// if login succeeded:
		ServerClientRequestTypes responseType = ClientController.responseFromServer.getRequestType();
		System.out.println("TEST?"+responseType);
		if(responseType.equals(ServerClientRequestTypes.LOGIN)) {
			// add test that response.messageSent is the array we had in fill[2] (SAME OBJECT)
			currentUser =  (SystemUser)ClientController.responseFromServer.getMessageSent();
			ClientController.setCurrentSystemUser(currentUser);
			statusLabel.setText("Successfully connected as: " + currentUser.getUsername() +".");
			statusLabel.setVisible(true);
			System.out.println("SLEEPING FOR A SECOND TO SHOW LABEL!");
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// NOW it's dynamic
			ClientController.setCurrentUserRole(currentUser.getRole());
			// TODO: create another map from Role to window-for-role ?
			
			// sammy D the current window
			((Node)event.getSource()).getScene().getWindow().hide();
			// prepare the new stage:
			Stage primaryStage = new Stage();

			// switch based on the current user's role
			switch(currentUser.getRole()) {
			case SUBSCRIBER:
				// set subscriber boolean value (true)
				ClientController.setCustomerIsSubsriber(true);
				WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog");
				break;
				
			case CUSTOMER:
				// also set subscriber boolean value (false)
				ClientController.setCustomerIsSubsriber(false);
				WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog");
				break;
				
			case REGIONAL_MANAGER:
				// TODO: same as customer
				WindowStarter.createWindow(primaryStage, this, "/gui/EktRegionalManagerHomePage.fxml", null, "Regional Manager Home Page");
				// Rotem 1.13 -> refactored to a method to make this switch easier to analyze
				break;
				
			case LOGISTICS_MANAGER:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktLogisticsManagerHomePage.fxml", null, "Logistics Manager Home Page");
				break;
				
			case SERVICE_REPRESENTATIVE:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktServiceRepresentativeHomePage.fxml", null, "Service Rep Home Page");
				break;
				
			case DIVISION_MANAGER:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktDivisionManagerHomePage.fxml", null, "Female Division Manager Home Page");
				break;
			
			case SALES_MANAGER:
				WindowStarter.createWindow(primaryStage, this, "/gui/SalesManager.fxml", null, "Sales Manager");
				// I removed the following line as this one is already called below!
				//primaryStage.show();
				break;
				
			case SALES_WORKER:
				WindowStarter.createWindow(primaryStage, this, "/gui/SalesDepartmentWorker.fxml", null, "Ekt Sales Department Worker");
				// I removed the following line as this one is already called below!
				//primaryStage.show();
				break;
			case DELIVERY_WORKER:
				WindowStarter.createWindow(primaryStage, this, "/gui/DeliveryManagerPage.fxml", null, "Ekt Delivery Department Worker");
				break;
			case INVENTORY_WORKER:
				WindowStarter.createWindow(primaryStage, this, "/gui/InventoryRestockWorkerPage.fxml", null, "Ekt Inventory Worker");
				break;
			default:
				throw new UnsupportedOperationException("No valid landing page for system user with role=" + currentUser.getRole());
			}
			
			// le-factored
			// srs
			primaryStage.show();

		}
		
		// login failed
		// Rotem 1.12.23 added granularity
		else {
			statusLabel.setVisible(true);
			if(responseType.equals(ServerClientRequestTypes.LOGIN_FAILED_ILLEGAL_INPUT)) {
				statusLabel.setText("Invalid input for login");
			}
			else if(responseType.equals(ServerClientRequestTypes.LOGIN_FAILED_ALREADY_LOGGED_IN)) {
				statusLabel.setText("Sorry, user is already logged in");
			}
			else {
			statusLabel.setText("ERROR (unspecified login error)!"); // add specifics
			//statusLabel.setVisible(true);
			}
		}
		if (currentUser != null)
			return currentUser.getRole();
		return null;
    }
}

// dead code:
// this here code is here as it helps me insert peppo to the DB via this java app.
// to use it - stick it in case CUSTOMER in the switch inside getBtnLogin

/*
 *  This is an example for a home page
String path = "";
String title = "";
path = "/gui/EktCustomerHomeAreaForm.fxml";
title = "Customer home page";
	System.out.println("Successfully connected as: " + connectedUser.getUsername() +".");
// added here:
// check user role (SHOULD BE INSERTED TO THE TABLE)
// and set current role for fetching the correct new window			
// load the new window:
((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
Stage primaryStage = new Stage();
WindowStarter.createWindow(primaryStage, this, path, null, title);
// this was done so that we can use this button					primaryStage.setOnCloseRequest(we -> 
{
	System.out.println("Pressed the X button."); 
	System.exit(0);					
}
);
primaryStage.show();
*/
