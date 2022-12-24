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

	@FXML Label statusLabel;

    @FXML
    void getBtnLogin(ActionEvent event) {
    	String userName, password;
    	userName = txtUsername.getText();
    	password = txtPassword.getText();
    	// ask to connect
    	
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
	    	System.out.println("Successfully connected as: " + connectedUser.getUsername() +".");
	    	// added here:
	    	// check user role (SHOULD BE INSERTED TO THE TABLE)
	    	// and set current role for fetching the correct new window
	    	
	    	
	    	// TODO: YOU CAN NOW SEE HOW LARGE THIS SHIT GETS!
	    	// TODO: this is hard coded, change it!
			ClientController.setCurrentUserRole(Role.CUSTOMER);
			// TODO: create another map from Role to window-for-role ?
			String path = "";
			String title = "";
			if(ClientController.getCurrentUserRole().equals(Role.CUSTOMER)) {
				path = "/gui/EktCustomerHomeAreaForm.fxml";
				title = "Customer home page";
			}
			
			// load the new window:
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();


			WindowStarter.createWindow(primaryStage, this, path, null, title);
			// this was done so that we can use this button
			primaryStage.setOnCloseRequest(we -> 
			{
				System.out.println("Pressed the X button."); 
				System.exit(0);
			}
			);
			primaryStage.show();
		}
		
		// login failed
		else {
			statusLabel.setText("ERROR!"); // add specifics
			statusLabel.setVisible(true);
		}    
		
    }

}
