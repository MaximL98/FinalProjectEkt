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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class _EKConfigurationCustomerHomeAreaController {

    @FXML
    private Button btnCollectOrder;

    @FXML
    private Button btnMakeOrder;

    @FXML
    private Label statusLabel;

    @FXML
    private Text txtWelcomeText;

	@FXML Button btnLeave;
    
    @FXML
    public void initialize() {
    	txtWelcomeText.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back");
    	txtWelcomeText.setLayoutX(400 - (txtWelcomeText.minWidth(0) / 2));
    }

    @FXML
    void getBtnCollectOrder(ActionEvent event) {
    	// TODO
    	
    	System.out.println("Entering order collection simulation page.");
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

    	if(res.size() == 0) {
    		// show a pop up that lets the user know he has no open orders. return user to previous page!
    		Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Collect Order"); // Rotem 1.15 the title used to be "cancel order" for some reason
            alert.setHeaderText("You have no active orders to this machine!");
            alert.setContentText("Click to return to main menu");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
            	System.out.println("User has no active orders for machine "+ ClientController._EkCurrentMachineID);
            }
        	return;

    	}
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), 
				"/gui/_EKConfigurationCustomerCollectOrderFrame.fxml", null, "Collect order", true);
		primaryStage.show();
    }

    @FXML
    void getBtnMakeOrder(ActionEvent event) {
    	// TODO
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), 
				"/gui/_EKConfigurationCustomerLocalOrderFrame.fxml", null, "Create local order", true);
		primaryStage.show();
    }

	@FXML public void getBtnLeave(ActionEvent event) {
    	// actually log him out
    	ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
    	// inform log
    	System.out.println("EK Customer "+ ClientController.getCurrentSystemUser().getUsername()+" logged out!");
		// load home area for service rep
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();

		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationLoginFrame.fxml", null, "Login", false);
		primaryStage.show();
		
	}

}
