package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Order;

public class EktPaymentFormController {
	
	@FXML
	private TextField txtCreditCardNumber;
	
	@FXML
	private TextField txtCVV;
	
	@FXML
	private ComboBox<String> cmbMonth;
	
	@FXML
	private ComboBox<String> cmbYear;
	
	@FXML
	private Button btnPay;
	
	@FXML
	private Button btnBack;
	
	@FXML
	private Text txtEmptyFields;
	
	ObservableList<String> monthsList;
	ObservableList<String> yearList;
	
	private void setCombo() {
		ArrayList<String> monthArray = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
		monthsList = FXCollections.observableArrayList(monthArray);
		cmbMonth.setItems(monthsList);
		
		ArrayList<String> yearArray = new ArrayList<>(Arrays.asList("2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033"));
		yearList = FXCollections.observableArrayList(yearArray);
		cmbYear.setItems(yearList);
	}
	public void initialize() {
		setCombo();
	}
	
	@FXML
	public void getBtnPay(ActionEvent event) {
		String creditCard, cvv;
		SingleSelectionModel<String> year;
		SingleSelectionModel<String> month;
		creditCard = txtCreditCardNumber.getText();
		cvv = txtCVV.getText();
		month = cmbMonth.getSelectionModel();
		year = cmbYear.getSelectionModel();
		cmbMonth.setValue("1");
		cmbYear.setValue("2022");
		System.out.println(month.toString());
		System.out.println(year.toString());
		
		//////////////////////////////////Implement Input check//////////////////////////////////////////////////////
		//////////////////////////////////Implement check of online order////////////////////////////////////////////
		if(creditCard != null & cvv != null) {
			Integer orderCounter = (int) ClientController.orderCounter++;
			ClientController.orderCounter++;
			
			//Send order to DB
			SCCP insertOrderToDB = new SCCP();
			//Create array to fill the message according to the ServerClientRequestTypes.ADD template
			insertOrderToDB.setRequestType(ServerClientRequestTypes.ADD);
			Object[] fill = new Object[3];
			fill[0] = "orders";
			fill[1] = false;
			fill[2] = new Object[] {new Order(
					orderCounter.toString(), ClientController.currentUserCart.size(), ClientController.orderTotalPrice.toString(), 1)};
			
			insertOrderToDB.setMessageSent(fill);
			System.out.println(insertOrderToDB.getMessageSent().toString());
			ClientUI.clientController.accept(insertOrderToDB);
//			
//			// check comm for answer:
//    		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
//    			// add test that response.messageSent is the array we had in fill[2] (SAME OBJECT)
//    			System.out.println("success");
//    		}
//    		else {
//    			System.out.println("failed");
//    		}
			
			nextPage(event, "/gui/OrderReceiptPage.fxml", "Receipt");
			
			
		}
		else {
			txtEmptyFields.setText("Fields should not be empty!");
		}
		
	}
	
	@FXML
	public void getBtnBack(ActionEvent event) {
		nextPage(event, "/gui/EktOrderSummary.fxml", "EKT cart");
	}
	
	private void nextPage(ActionEvent event, String fxmlAddress, String windowLabel) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), fxmlAddress, null, windowLabel);
		primaryStage.setOnCloseRequest(we -> 
		{
			System.out.println("Pressed the X button."); 
			System.exit(0);
		}
		);
		primaryStage.show();
		((Stage)((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
	}
	
	
	
	
	
 }
