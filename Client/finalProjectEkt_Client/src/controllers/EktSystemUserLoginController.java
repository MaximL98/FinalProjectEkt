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
import javafx.scene.text.Text;

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

	@FXML Text lblFastRecognition1;

	@FXML Text lblFastRecognition2;
	
	
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
		// don't worry, this DOES NOT reset the fast-recognition variables
		ClientController.resetVars();
		if(ClientController.isFastRecognitionToggle()) {
			// set fast recognition
			lblFastRecognition1.setText("Using fast-recognition");
			lblFastRecognition2.setText("");
			txtUsername.setDisable(true);
			txtPassword.setDisable(true);
		}
		else{
			lblFastRecognition1.setText("USERNAME");
			lblFastRecognition2.setText("PASSWORD");
			txtUsername.setDisable(false);
			txtPassword.setDisable(false);
		}
		
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
    public void getBtnLogin(ActionEvent event) {
		// Rotem 1.12.23
		// hide the status label until we finish login attempt
		statusLabel.setVisible(false);
		
    	String userName, password;
    	userName = txtUsername.getText();
    	password = txtPassword.getText();
    	if(ClientController.isFastRecognitionToggle()) {
    		userName=ClientController.getFastRecognitionUserName();
    		password=ClientController.getFastRecognitionPassword();
    	}
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
			
			// prepare the new stage:
			Stage primaryStage = new Stage();

			// switch based on the current user's role
			switch(currentUser.getRole()) {
			case SUBSCRIBER:
				// set subscriber boolean value (true)
				ClientController.setCustomerIsSubsriber(true);
				WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog", true);
				break;
				
			case CUSTOMER:
				if(ClientController.isFastRecognitionToggle()) {
					// show alert and reload window
					System.out.println("NOT IMPLEMENTED: alert customer that he is not a subscriber, request regular login");
				}
				// also set subscriber boolean value (false)
				ClientController.setCustomerIsSubsriber(false);
				WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog", true);
				break;
				
			case REGIONAL_MANAGER:
				// TODO: same as customer
				WindowStarter.createWindow(primaryStage, this, "/gui/EktRegionalManagerHomePage.fxml", null, "Regional Manager Home Page", true);
				// Rotem 1.13 -> refactored to a method to make this switch easier to analyze
				break;
				
			case LOGISTICS_MANAGER:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktLogisticsManagerHomePage.fxml", null, "Logistics Manager Home Page", true);
				break;
				
			case SERVICE_REPRESENTATIVE:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktServiceRepresentativeHomePage.fxml", null, "Service Rep Home Page", true);
				break;
				
			case DIVISION_MANAGER:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktDivisionManagerHomePage.fxml", null, "Female Division Manager Home Page", true);
				break;
			
			case SALES_MANAGER:
				WindowStarter.createWindow(primaryStage, this, "/gui/SalesManager.fxml", null, "Sales Manager", true);
				// I removed the following line as this one is already called below!
				//primaryStage.show();
				break;
				
			case SALES_WORKER:
				WindowStarter.createWindow(primaryStage, this, "/gui/SalesDepartmentWorker.fxml", null, "Ekt Sales Department Worker", true);
				// I removed the following line as this one is already called below!
				//primaryStage.show();
				break;
			case DELIVERY_WORKER:
				WindowStarter.createWindow(primaryStage, this, "/gui/DeliveryManagerPage.fxml", null, "Ekt Delivery Department Worker", true);
				break;
			case UNAPPROVED_CUSTOMER:
				statusLabel.setText("Uset not yet registered!");
				return;
				
			case INVENTORY_WORKER:
				WindowStarter.createWindow(primaryStage, this, "/gui/InventoryRestockWorkerPage.fxml", null, "Ekt Inventory Worker", true);
				break;
			default:
				throw new UnsupportedOperationException("No valid landing page for system user with role=" + currentUser.getRole());
			}
			
			// disable fast-recognition (simulation)
			
			// le-factored
			// srs
			primaryStage.show();
			((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
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
