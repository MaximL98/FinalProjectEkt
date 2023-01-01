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
import logic.Order;

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
		primaryStage.setOnCloseRequest(we -> 
		{
			System.out.println("Pressed the X button."); 
			System.exit(0);
		}
		);
		primaryStage.show();
		((Stage)((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
	}
	
	private void processOrder(ActionEvent event) {
		long counter = 0;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nextPage(event, "/gui/OrderReceiptPage.fxml", "EKrut Order Summary");
	}
 }
