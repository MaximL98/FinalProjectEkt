package ek_configuration;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Role;
import logic.SystemUser;

public class _EKConfigurationLoginFrameController {

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField pwdField;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField txtUsername;

    @FXML
    void getBtnLoginEK(ActionEvent event) {
    	// verify that the user type is either customer or tipuli worker (kita tifulit yantu)
		statusLabel.setVisible(false);
    	String userName, password, nextScreenPath = null;
    	userName = txtUsername.getText();
    	password = pwdField.getText();
    	
    	SCCP preparedMessage = new SCCP();
    	preparedMessage.setRequestType(ServerClientRequestTypes.EK_LOGIN);
    	preparedMessage.setMessageSent(new String[] {userName, password});
    	// request the login:
		// send to server
    	System.out.println("Client: Sending login request to server as " + userName+".");
		ClientUI.clientController.accept(preparedMessage);
		// check client-side object for answer:
		// if login succeeded:
		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			SystemUser su = (SystemUser)ClientController.responseFromServer.getMessageSent();
			System.out.println("Logged in successfuly as " + su);
			ClientController.setCurrentSystemUser(su);
			ClientController.setCurrentUserRole(su.getRole());
			switch(su.getRole()) {
			case CUSTOMER:
				nextScreenPath = "/gui/_EKConfigurationCustomerHomeArea.fxml";
				break;
			case LOGISTICS_EMPLOYEE:
				nextScreenPath = "/gui/_EKConfigurationLogisticsEmployeeFrame.fxml";
				break;
				
				/*
				 * TODO:
				 * add cases for SUBSCRIBER and TIPULI etc...
				 * 
				 */
			default:
				statusLabel.setText("EK Configuration only supports customers and machine maintenance employees.");
				statusLabel.setVisible(true);
				return;
				
			}
			
		}
		else {
			// invalid login - add tests if it's 'user already logged' error or 'password' error, or something else.
			statusLabel.setText("Invalid input in login!");
			return;
		}
    	
    	// move user to next screen
		
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();

		System.out.println("Trying to run cock sucking path: " + nextScreenPath);
		WindowStarter.createWindow(primaryStage, new Object(), nextScreenPath, null, "Customer Home Frame");
		primaryStage.show();
    }

}
