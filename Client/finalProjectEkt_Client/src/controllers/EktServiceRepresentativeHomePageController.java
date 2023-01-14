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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class EktServiceRepresentativeHomePageController {

    @FXML
    private Button btnLogout;
    
    @FXML
    private Button btnMisc;
    
    @FXML
    private Text txtGreeting;

	@FXML Button btnRegisterCustomer;

    @FXML
    private void initialize() {
    	String tmp = txtGreeting.getText();
    	tmp = tmp.replace(", ", ", "+ClientController.getCurrentSystemUser().getFirstName()+" ");
    	txtGreeting.setText(tmp);
    }
    
    @FXML
    void getBackLogout(ActionEvent event) {
    	// actually log him out
    	ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
    	// inform log
    	System.out.println("Service representative "+ ClientController.getCurrentSystemUser().getUsername()+" logged out!");
		// load home area for service rep
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();

		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);
		primaryStage.show();
    }

	@FXML public void getRegisterCustomer(ActionEvent event) {
		// load home area for service rep
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();

		WindowStarter.createWindow(primaryStage, this, "/gui/EktServiceRepAddCustomerForm.fxml", null, "Customer Registration Page", true);
		primaryStage.show();
		
	}

	@FXML public void getBtnMisc(ActionEvent event) {
		// load home area for service rep
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();

		WindowStarter.createWindow(primaryStage, this, "/gui/EktServiceRepAddSubscriberForm.fxml", null, "Subscriber Registration Page", true);
		primaryStage.show();
	}

}
