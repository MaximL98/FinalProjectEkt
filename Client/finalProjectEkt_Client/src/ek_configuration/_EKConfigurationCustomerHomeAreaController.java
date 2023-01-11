package ek_configuration;

import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class _EKConfigurationCustomerHomeAreaController {

    @FXML
    private Button btnCollectOrder;

    @FXML
    private Button btnMakeOrder;

    @FXML
    private Label statusLabel;

    @FXML
    private Text txtWelcomeText;

    @FXML
    void getBtnCollectOrder(ActionEvent event) {
    	// TODO
    	System.out.println("Entering order collection simulation page.");
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), 
				"/gui/_EKConfigurationCustomerCollectOrderFrame.fxml", null, "Collect order");
		primaryStage.show();
    }

    @FXML
    void getBtnMakeOrder(ActionEvent event) {
    	// TODO
    }

}
