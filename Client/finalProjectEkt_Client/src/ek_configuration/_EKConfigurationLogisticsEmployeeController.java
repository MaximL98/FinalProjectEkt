package ek_configuration;

import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class _EKConfigurationLogisticsEmployeeController {

    @FXML
    private Button btnPartialRestock;

    @FXML
    private Button btnRestockAll;

    @FXML
    private Label statusLabel;

    @FXML
    private Text txtWelcomeText;

    @FXML
    void getBtnPartialRestock(ActionEvent event) {
    	String nextScreenPath = "/gui/_EKConfigurationPartialRestockFrame.fxml";
    	// load table - insert all products in this machine
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), nextScreenPath, null, "Partial Restock Frame");
		primaryStage.show();
    }

    @FXML
    void getBtnRestockAll(ActionEvent event) {
    	
    }

}
