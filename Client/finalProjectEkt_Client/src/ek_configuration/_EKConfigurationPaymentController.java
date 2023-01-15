package ek_configuration;

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
import logic.Role;

public class _EKConfigurationPaymentController {
	
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
    
    @FXML
    private ComboBox<String> comboBoxBillingDate;

    
    public void initialize() {
    	if (ClientController.getCustomerIsSubsriber() == true) {
			btnPayUsingTheEktApp.setDisable(false);
			comboBoxBillingDate.setDisable(false);
			ObservableList<String> comboPayment = FXCollections.observableArrayList();
			String[] nowDate = java.time.LocalDate.now().toString().split("-");
			comboPayment.add("Now: " + nowDate[2] + "-" + nowDate[1] + "-" + nowDate[0]);
			ArrayList<String> date = new ArrayList<>();
			String[] splitDate = java.time.LocalDate.now().toString().split("-");
			date.add(splitDate[0]);
			date.add(splitDate[1]);
			date.add(splitDate[2]);
			//Set day to 01
			date.set(2, "01");
			//If current month is "12" set month = 01 and year = currentYear+1 
			if (date.get(1).equals("12")) {
				date.set(1, "01");
				date.set(0, (Integer.parseInt(date.get(0)) + 1) + "");
			} else {
				//Else do month = month+1
				int month = Integer.parseInt(date.get(1));
				
				if (month < 10) {
					date.set(1, "0" + (month + 1));
				} else {
					date.set(1, (month + 1) + "");
				}
				
			}
			
			comboPayment.add("Next month: " + date.get(2) + "-" + date.get(1) + "-" + date.get(0));
			
			comboBoxBillingDate.setItems(comboPayment);
		}
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
    
    /*
     * TODO: replace this with a proper query (maybe)
     */
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
    	String[] date = java.time.LocalDate.now().toString().split("-");
    	ClientController.billingDate = date[0] + "-" + date[1] + "-" + date[2];
    	txtProcessing.setText("PROCESSING...");
    	processOrder(event);
    }

    @FXML
    void getBtnPayUsingTheEktApp(ActionEvent event) {
    	String[] date = comboBoxBillingDate.getValue().split(" ");
    	if (date.length == 3) {
    		ClientController.billingDate = date[2];
    	} else {
    		ClientController.billingDate = date[1];
    	}
    	txtProcessing.setText("PROCESSING...");
    	processOrder(event);
    }

    @FXML
    void getBtnPayWithBalance(ActionEvent event) {
    	String[] date = java.time.LocalDate.now().toString().split("-");
    	System.out.println(date);
    	ClientController.billingDate = date[0] + "-" + date[1] + "-" + date[2];
    	txtProcessing.setText("PROCESSING...");
    	processOrder(event);
    }
	
	@FXML
	public void getBtnBack(ActionEvent event) {
		nextPage(event, "/gui/_EKConfigurationOrderSummary.fxml", "EKT cart");
	}
	
	private void nextPage(ActionEvent event, String fxmlAddress, String windowLabel) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), fxmlAddress, null, windowLabel, true);

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
				"\"" + "," + getTypeId(ClientController.orderType) + "," + 3 + ")"}; // ROTEM: I set it to 3, as in completed, immediately! (we@machine) 
		
		preparedMessage.setMessageSent(fillOrder); 
		ClientUI.clientController.accept(preparedMessage);
		
		//select from database for MAX orderID
		preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.SELECT);
		
		preparedMessage.setMessageSent(new Object[] {"orders", true, "MAX(orderID)", false, null, false, null}); 
		ClientUI.clientController.accept(preparedMessage);
		
		SCCP answer = ClientController.responseFromServer;
		
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<Object>> preProcessedOutput = (ArrayList<ArrayList<Object>>)answer.getMessageSent();
		
		String temp = "";
		
		for(ArrayList<Object> lst : preProcessedOutput) {
			// we expect product to have 5 columns, and act accordingly
			Object[] arr = lst.toArray();
			System.out.println(Arrays.toString(arr));
			temp = arr[0].toString();
			System.out.println(temp);
		}
		
		Integer maxOrderId = Integer.parseInt(temp);
		ClientController.orderNumber = maxOrderId;
		
		////////////////
		/*
		 * Rotem: 1.12.23 -> adding an insert to customer_orders (associate a customer with an order in DB)
		 */
		CustomerOrder toInsert = new CustomerOrder(ClientController.getCurrentSystemUser().getId(),
				maxOrderId,
				/* TODO: replace this with the expected machine to be used for the order (delivery or pickup)*/
				1, 
				/* Rotem added per Dima the billing date (it is stored in ClientController as static string)*/
				ClientController.billingDate);
		ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.ADD, 
				new Object[] 
						{"customer_orders", 
								false, 
								new Object[] {toInsert}}));

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
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		_EKConfigurationProductController.itemsInCart = 0;
		ClientController.currentUserCart.keySet().clear();
		ClientController.getProductByID.keySet().clear();
		ClientController.cartPrice.keySet().clear();
		ClientController.userOrders.keySet().clear();
		nextPage(event, "/gui/_EKConfigurationOrderReceiptPage.fxml", "EKrut Order Receipt");
	}

}
