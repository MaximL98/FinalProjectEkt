package controllers;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import logic.Role;
import logic.SystemUser;
import javafx.scene.control.Label;

public class EktSystemUserLoginController {

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

	@FXML 
	Label statusLabel;
	
    @FXML
    void getBtnLogin(ActionEvent event) {
    	String userName, password;
    	userName = txtUsername.getText();
    	password = txtPassword.getText();
    	System.out.println(userName + " " + password);
    	
//    	if (userName.equals("q")){
//    		Stage primaryStage = new Stage();
//    		WindowStarter.createWindow(primaryStage, this, "/gui/SalesDepartmentWorker.fxml", null, "Ekt Sales Department Worker");
//			primaryStage.show();
//			return;
//    	}
    	
    	SCCP preparedMessage = new SCCP();
    	preparedMessage.setRequestType(ServerClientRequestTypes.LOGIN);
    	preparedMessage.setMessageSent(new String[] {userName, password});
		// send to server
    	System.out.println("Client: Sending login request to server as " + userName+".");
		ClientUI.clientController.accept(preparedMessage);
		// check client-side object for answer:
		// if login succeeded:
		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.LOGIN)) {
			// add test that response.messageSent is the array we had in fill[2] (SAME OBJECT)
			SystemUser connectedUser =  (SystemUser)ClientController.responseFromServer.getMessageSent();
			ClientController.setCurrentSystemUser(connectedUser);
			statusLabel.setText("Successfully connected as: " + connectedUser.getUsername() +".");
			statusLabel.setVisible(true);
			System.out.println("SLEEPING FOR A SECOND TO SHOW LABEL!");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// NOW it's dynamic
			ClientController.setCurrentUserRole(connectedUser.getRole());
			// TODO: create another map from Role to window-for-role ?
			
			// sammy D the current window
			((Node)event.getSource()).getScene().getWindow().hide();
			// prepare the new stage:
			Stage primaryStage = new Stage();
			// this was done so that we can use this button
			primaryStage.setOnCloseRequest(we -> 
			{
				System.out.println("Pressed the X button."); 
				System.exit(0);
			}
			);
			// switch based on the current user's role
			switch(connectedUser.getRole()) {
			case CUSTOMER:
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
				// TODO: replace this with a legit home area for cumsoomer
				WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog");

				primaryStage.show();
				break;
				
			case REGIONAL_MANAGER:

				WindowStarter.createWindow(primaryStage, this, "/gui/EktRegionalManagerHomePage.fxml", null, "Regional Manager Home Page");
				primaryStage.show();
				break;
				
			case LOGISTICS_MANAGER:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktLogisticsManagerHomePage.fxml", null, "Logistics Manager Home Page");
				primaryStage.show();
				break;
				
			case SERVICE_REPRESENTATIVE:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktServiceRepresentativeHomePage.fxml", null, "Service Rep Home Page");
				primaryStage.show();
				break;
			case CEO:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktCeoHomePage.fxml", null, "CEO Home Page");
				primaryStage.show();
				break;
				
			case DIVISION_MANAGER:
				WindowStarter.createWindow(primaryStage, this, "/gui/EktDivisionManagerHomePage.fxml", null, "Female Division Manager Home Page");
				primaryStage.show();
				break;
			
			case SALES_MANAGER:
				WindowStarter.createWindow(primaryStage, this, "/gui/SalesManager.fxml", null, "Sales Manager");
				primaryStage.show();
				break;
				
			case SALES_WORKER:
				WindowStarter.createWindow(primaryStage, this, "/gui/SalesDepartmentWorker.fxml", null, "Ekt Sales Department Worker");
				primaryStage.show();
				break;
				
			default:
				throw new UnsupportedOperationException("No valid landing page for system user with role=" + connectedUser.getRole());
			}
		}
		
		// login failed
		else {
			statusLabel.setText("ERROR!"); // add specifics
			statusLabel.setVisible(true);
		}    
		
    }

}
