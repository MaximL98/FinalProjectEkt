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
    void onEnter(ActionEvent ae) {
    	getBtnLoginEK(ae);
    }
    
    /*
     * Rotem added for inactivity stuff
     */
    @FXML
    private void initialize() {
    	ClientController.resetVars();
    }
    
    @FXML
    void getBtnLoginEK(ActionEvent event) {
		statusLabel.setVisible(false);
    	String userName, password, nextScreenPath = null, nextPathTitle = "Frame";
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
			case SUBSCRIBER:
				ClientController.setCustomerIsSubsriber(true);
				nextScreenPath = "/gui/_EKConfigurationCustomerHomeArea.fxml";
				nextPathTitle = "Customer Home Frame";
				break;
			case CUSTOMER:
				ClientController.setCustomerIsSubsriber(false);
				nextScreenPath = "/gui/_EKConfigurationCustomerHomeArea.fxml";
				nextPathTitle = "Customer Home Frame";
				break;
			case LOGISTICS_EMPLOYEE:
				nextScreenPath = "/gui/_EKConfigurationLogisticsEmployeeFrame.fxml";
				nextPathTitle = "Logistics Employee Frame";
				break;
			case SUBSCRIBER_20DISCOUNT:
				ClientController.setCustomerIsSubsriber(true);
				nextScreenPath = "/gui/_EKConfigurationCustomerHomeArea.fxml";
				nextPathTitle = "Customer Home Frame";
				break;
				
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
    	System.out.println("Login EK -> " + nextScreenPath);
    	// move user to next screen
		// prepare the new stage:
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), nextScreenPath, null, nextPathTitle, true);
		primaryStage.show();
		((Stage) ((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
    }

}
