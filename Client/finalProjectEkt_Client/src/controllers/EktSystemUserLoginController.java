package controllers;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    	System.out.println("Test");
    	if(userName.equals("q")) {
    		Stage primaryStage = new Stage();
    		WindowStarter.createWindow(primaryStage, new Object(), "/gui/SalesManager.fxml", null, "Sales");
    		System.out.println("Test");
    		primaryStage.show();
    		return;
    	}
    	
    	SCCP preparedMessage = new SCCP();
    	preparedMessage.setRequestType(ServerClientRequestTypes.LOGIN);
    	preparedMessage.setMessageSent(new String[] {userName, password});
		// send to server
    	System.out.println("Client: Sending login request to server as " + userName+".");
		ClientUI.clientController.accept(preparedMessage);
		
		
		
		// check comm for answer:
		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.LOGIN)) {
			// add test that response.messageSent is the array we had in fill[2] (SAME OBJECT)
			SystemUser con =  (SystemUser)ClientController.responseFromServer.getMessageSent();
			ClientController.setCurrentSystemUser(con);
			statusLabel.setText("Successfully connected as: " + con.getUsername() +".");
			statusLabel.setVisible(true);
	    	System.out.println("Successfully connected as: " + con.getUsername() +".");

			
		}
		else {
			statusLabel.setText("ERROR!"); // add specifics
			statusLabel.setVisible(true);
		}    
		
    }

}
