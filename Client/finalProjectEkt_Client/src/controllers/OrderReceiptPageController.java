package controllers;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import logic.Customer;
import logic.Order;

public class OrderReceiptPageController {

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnMainMenu;

    @FXML
    private Text txtEmail;

    @FXML
    private Text txtOrderNumber;

    @FXML
    private Text txtTotalAmount;

    @FXML
    void getBtnLogout(ActionEvent event) {
    	SCCP preparedMessage = new SCCP();
    	preparedMessage.setRequestType(ServerClientRequestTypes.LOGOUT);
    	ClientUI.clientController.accept(preparedMessage);
    }

    @FXML
    void getBtnMainMenu(ActionEvent event) {
    	//ClientUI.start();
    }
    
    public void setReceipt(Order order, Customer customer) {
    	txtOrderNumber.setText("Order number " + order.getOrderID() + " complete.");
    	txtTotalAmount.setText("Total Amount paid: " + order.getTotalAmount());
    	txtEmail.setText("Order information was sent to " + customer.getEmailAddress());
    }

}
