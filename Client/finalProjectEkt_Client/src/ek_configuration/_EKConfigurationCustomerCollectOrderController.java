package ek_configuration;

import java.util.ArrayList;
import java.util.Optional;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class _EKConfigurationCustomerCollectOrderController {
	
	// lololol (changed it to be proper)
	ArrayList<String> existingOrdersContents = new ArrayList<>();
	
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
    	// select * from orders JOIN customer_orders on orders.orderID=customer_orders.orderId WHERE statusId=1 AND typeId = 1;
    	ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.SELECT, 
    			new Object[] {"orders JOIN customer_orders on orders.orderID=customer_orders.orderId", 
    					true, "orders.orderID", 
    					true, "customerId="+ClientController.getCurrentSystemUser().getId()+
    					" AND orders.statusId=1 AND orders.typeId=1 AND orders.machineID="+ClientController._EkCurrentMachineID, false, null}));
    	@SuppressWarnings("unchecked")
		ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
    	for(ArrayList<Object> row : res) {
    		if(row.size() > 0) {
    			existingOrdersContents.add(row.get(0).toString());
    		}
    	}
    	ordersByID.getItems().addAll(existingOrdersContents);
    	txtWelcomeText.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back");
    	
    }
    
    @FXML
    void getBtnFinishCollectOrder(ActionEvent event) {
    	String orderIdString =  ordersByID.getSelectionModel().getSelectedItem();
    	System.out.println("Selected: " +orderIdString);
    	
    	System.out.println("Processing . . .");
    	// send this query:
    	ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.UPDATE, new Object[] {"orders", "statusId=3", "orderID="+orderIdString}));
    	System.out.println("Your order has been dispensed, waiting for you to collect your items.");
    	
    	// TODO:
    	// insert a query to set the order status of the selected order to "completed".
    	
    	System.out.println("Enjoy!");
    	// load previous page:
		((Node)event.getSource()).getScene().getWindow().hide();

		goBack(event);
    }

	private void goBack(ActionEvent event) {
		// sammy D the current window
		// prepare the new stage:
		Stage primaryStage = new Stage();
		String nextScreenPath = "/gui/_EKConfigurationCustomerHomeArea.fxml";
		String nextPathTitle = "Customer Home Frame";
		WindowStarter.createWindow(primaryStage, this, nextScreenPath, null, nextPathTitle, true);
		primaryStage.show();
	}

}
