package controllers;

import java.util.ArrayList;

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
    private Button btnLowStockAlerts;
    
    @FXML
    private Button btnMaybeNothing;
    
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
		setRegionalManagerLocation();
		txtManagerWelcome
				.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back!");
		txtManagerWelcome.setLayoutX(400 - (txtManagerWelcome.minWidth(0)) / 2);
	}

	@FXML
	void getBtnAcceptCustomers(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktRegionalManagerAcceptNewCustomer.fxml", null, "Ekt Manage New Customers");
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	@FXML
	void getBtnSetThreshold(ActionEvent event) {
		
	}

	@FXML
	public void getBtnReviewReports(ActionEvent event) {

		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktReportSelectForm.fxml", null, "Reviews");

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window

	}

	@FXML
	public void getLogoutBtn(ActionEvent event) {
		ClientController.sendLogoutRequest();
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login");

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
	
	@FXML
	public void getBtnStockAlerts(ActionEvent event) {
		
	}
	
	@FXML
	public void getBtnMaybeNothing(ActionEvent event) {
		
	}
	
	private void setRegionalManagerLocation() {
		int currentManagerID = ClientController.getCurrentSystemUser().getId();
		SCCP getCurrentManagerLocationNameRequestMessage = new SCCP();
		getCurrentManagerLocationNameRequestMessage.setRequestType(ServerClientRequestTypes.SELECT);
		getCurrentManagerLocationNameRequestMessage.setMessageSent(new Object[] { "manager_location LEFT JOIN ektdb.locations on locations.locationID = manager_location.locationId", true, "locationName", true, 
				"idRegionalManager = " + currentManagerID, false, null});
		
		ClientUI.clientController.accept(getCurrentManagerLocationNameRequestMessage);
		
		ArrayList<?> currentManagerLocationName = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		String locationName = ((ArrayList<Object>)currentManagerLocationName.get(0)).get(0).toString();
		ClientController.setCurrentUserRegion(locationName);
	}

}
