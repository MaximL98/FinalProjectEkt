package controllers;

import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EktLogisticsManagerHomePageController {

    @FXML
    private Button btnLogout;

    @FXML
    private Text txtGreeting;
    
    @FXML
    private Button btnRestock;

    @FXML
    private void initialize() {
    	txtGreeting.setText(txtGreeting.getText().concat(" ".concat(ClientController.getCurrentSystemUser().getFirstName())));
    }
    
    @FXML
    void getBackLogout(ActionEvent event) {
    	// actually log the user out
    			ClientController.sendLogoutRequest();

    			// move to new window
    			Stage primaryStage = new Stage();
    			WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login");

    			primaryStage.show();
    			((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    }

    @FXML
    void getBtnRestock(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/InventoryRestockWorkerPage.fxml", null, "Inventory Restock");
		primaryStage.show();
    }
}
