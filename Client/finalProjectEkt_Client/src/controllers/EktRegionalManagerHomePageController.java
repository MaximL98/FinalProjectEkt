package controllers;

import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/*
 * Rotem:
 * I co-opted this window for regional manager.
 * Original class name:
 * EktManagerWelcomePageController
 * new one:
 * this obv
 * 
 */
public class EktRegionalManagerHomePageController {
	
	@FXML
    private Button btnAcceptCustomers;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnReviewReports;

    @FXML
    private Button btnSetThreshold;

    @FXML
    private Text txtManagerWelcome;
    
    public void initialize() {
    	//txtManagerWelcome.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back!");
    	txtManagerWelcome.setLayoutX(400 - (txtManagerWelcome.minWidth(0))/2);
    }

    @FXML
    void getBtnAcceptCustomers(ActionEvent event) {
    	//Implement
    }

    @FXML
    void getBtnSetThreshold(ActionEvent event) {
    	//Implement
    }
	
	@FXML
	public void getBtnReviewReports(ActionEvent event) {
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktReportSelectForm.fxml", null, "Reviews");

		primaryStage.show();
		((Stage) ((Node)event.getSource()).getScene().getWindow()).close(); //closing primary window
	}

	@FXML 
	public void getLogoutBtn(ActionEvent event) {
		ClientController.sendLogoutRequest();
    	// move to new window
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login");

		primaryStage.show();	
	}
}
