package ek_configuration;

import javax.management.RuntimeErrorException;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
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
    	// request initial query:
    	// SELECT * FROM ektdb.products_in_machine WHERE machineID=9 AND stock!=max_stock;
    	// if no products are lacking, inform!
    	
    	
    	// set all products in current machine to their max stock (This worker came with a SEMI!)
    	ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.UPDATE, new Object[]
    			{"products_in_machine", "stock = max_stock, restock_flag=0", "machineID="+ClientController._EkCurrentMachineID}));
    	/*if(!ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
    		throw new RuntimeException("error!");
    	}*/
    	// inform user
		Alert successMessage = new Alert(AlertType.INFORMATION);
		successMessage.setTitle("Update Success");
		successMessage.setHeaderText("Update Success");
		successMessage.setContentText("FULL restock successful!");
		successMessage.show();
    	
    }

}
