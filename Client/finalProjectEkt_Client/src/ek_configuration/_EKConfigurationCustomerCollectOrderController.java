package ek_configuration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class _EKConfigurationCustomerCollectOrderController {
	
	private String[] exampleContents = {"ass", "tities", "pizza", "shawarma", "pastrama"};
	
    @FXML
    private Button btnFinishCollectOrder;

    @FXML
    private ListView<String> ordersByID;

    @FXML
    private Label statusLabel;

    @FXML
    private Text txtWelcomeText;

    @FXML 
    private void initialize() {
    	
    	// TODO: send query to get all orders from this customer that can be picked up on this machine
    	// (replace the argument in the following line with the result of a query)
    	ordersByID.getItems().addAll(exampleContents);
    	
    	
    }
    
    @FXML
    void getBtnFinishCollectOrder(ActionEvent event) {
    	System.out.println("Selected: " + ordersByID.getSelectionModel().getSelectedItem());
    	System.out.println("Processing . . .");
    	System.out.println("Your order has been dispensed, waiting for you to collect your items.");
    	
    	// TODO:
    	// insert a query to set the order status of the selected order to "completed".
    	
    	System.out.println("Enjoy!");
    }

}
