package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.CustomerOrder;
import logic.Order;
import logic.Product;

public class EktPaymentFormController {
	
	@FXML
    private Button btnBack;

    @FXML
    private Button btnChargeMyCreditCard;

    @FXML
    private Button btnPayUsingTheEktApp;

    @FXML
    private Button btnPayWithBalance;

    @FXML
    private Text txtAccountBalance;

    @FXML
    private Text txtCreditCard;
    
    @FXML
    private Text txtProcessing;

    
    public void initialize() {
    	
    }
    
    private Integer getTypeId(String orderType) {
		switch (orderType) {
		case "Pickup":
			return 1;
		case "Delivery":
			return 2;
		case "Local":
			return 3;
		}
    	return null;
    }
    
    private Integer getMachineId(String pickupPlace) {
		
    	switch (pickupPlace) {
		case "Haifa, Downtown":
			return 1;
		case "Beer Sheva, Center":
			return 2;
		case "Beer Sheva, Downtown":
			return 3;
		
	    case "Kiryat Motzkin, Center":
			return 4;
		
		case "Kiryat Shmona, Center":
			return 5;
		
		case "Beer Sheva, Updog":
			return 6;
		
		case "Abu Dabi, Center":
			return 7;
		
		case "Abu Naji, Center":
			return 8;
    	}
    	return null;
    }
    
    @FXML
    void getBtnChargeMyCreditCard(ActionEvent event) {
    	txtProcessing.setText("PROCESSING...");
    	processOrder(event);
    }

    @FXML
    void getBtnPayUsingTheEktApp(ActionEvent event) {
    	txtProcessing.setText("PROCESSING...");
    	processOrder(event);
    }

    @FXML
    void getBtnPayWithBalance(ActionEvent event) {
    	txtProcessing.setText("PROCESSING...");
    	processOrder(event);
    }
	
	@FXML
	public void getBtnBack(ActionEvent event) {
		nextPage(event, "/gui/EktOrderSummary.fxml", "EKT cart");
	}
	
	private void nextPage(ActionEvent event, String fxmlAddress, String windowLabel) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), fxmlAddress, null, windowLabel);

		primaryStage.show();
		((Stage)((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
	}
	
	private void processOrder(ActionEvent event) {
		//insert to database, table: orders
		SCCP preparedMessage = new SCCP();
		
		preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
		//name of table, add many?, array of objects (to add),  
		//ArrayList<Object> fillArrayToOrder = new ArrayList<>();
		
		Object[] fillOrder = new Object[3];
		
		fillOrder[0] = "orders (total_price, total_quantity, machineID, date_received, deliveryTime, typeId, statusId)";
		fillOrder[1] = false;
		fillOrder[2] = new Object[] {"(" + ClientController.orderTotalPrice + "," + 
		ClientController.orderTotalQuantity + "," + getMachineId(ClientController.pickupPlace) + ",\"" + 
		ClientController.orderDateReceived + "\"" + ",\"" + ClientController.orderDeliveryTime + 
				"\"" + "," + getTypeId(ClientController.orderType) + "," + 1 + ")"};
		
		preparedMessage.setMessageSent(fillOrder); 
		ClientUI.clientController.accept(preparedMessage);
		
		//select from database for MAX orderID
		preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.SELECT);
		
		preparedMessage.setMessageSent(new Object[] {"orders", true, "MAX(orderID)", false, null, false, null}); 
		ClientUI.clientController.accept(preparedMessage);
		
		SCCP answer = ClientController.responseFromServer;
		
		ArrayList<ArrayList<Object>> preProcessedOutput = (ArrayList<ArrayList<Object>>)answer.getMessageSent();
		
		String temp = "";
		
		for(ArrayList<Object> lst : preProcessedOutput) {
			// we expect product to have 5 columns, and act accordion-ly
			Object[] arr = lst.toArray();
			System.out.println(Arrays.toString(arr));
			temp = arr[0].toString();
			System.out.println(temp);
		}
		
		Integer maxOrderId = Integer.parseInt(temp);
		
		////////////////
		/*
		 * Rotem: 1.12.23 -> adding an insert to customer_orders (associate a customer with an order in DB)
		 */
		
		ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.ADD, 
				new Object[] 
						{"customer_orders", 
								false, 
								new Object[] {
										new CustomerOrder(ClientController.getCurrentSystemUser().getId(), maxOrderId, 1)}}));
		SCCP rotemRes = ClientController.responseFromServer;
		if(rotemRes.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			System.out.println("Updated customer_orders successfully!");
		}
		else {
			System.out.println("Failed in updating customer_orders!");
		}
		
		/*
		 * End Rotem -> added insert to custoemr_orders
		 */
		////////////////

		
		//insert to database, table: order_contents
		preparedMessage = new SCCP();
		
		preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
		//name of table, add many?, array of objects (to add),  
		ArrayList<Object> fillArrayToOrderContents = new ArrayList<>();
		
		Object[] fillOrderContents = new Object[3];
		
		fillOrderContents[0] = "order_contents";
		fillOrderContents[1] = true;
		
		int i = 3, j = 4;
		for(ArrayList<?> order: ClientController.userOrders.values()) {
			while(j < order.size() - 1){
				fillArrayToOrderContents.add("(" + maxOrderId + ",\"" + 
				order.get(i) + "\"" + ",\"" + order.get(j) + "\")");
				i+=2; j+=2;
			}
		}

		fillOrderContents[2] = fillArrayToOrderContents.toArray();
		
		preparedMessage.setMessageSent(fillOrderContents); 
		ClientUI.clientController.accept(preparedMessage);
		ClientController.orderNumber++;
		long counter = 0;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EktProductFormController.itemsInCart = 0;
		ClientController.currentUserCart.keySet().clear();
		ClientController.getProductByID.keySet().clear();
		ClientController.cartPrice.keySet().clear();
		ClientController.userOrders.keySet().clear();
		nextPage(event, "/gui/OrderReceiptPage.fxml", "EKrut Order Receipt");
	}
 }
