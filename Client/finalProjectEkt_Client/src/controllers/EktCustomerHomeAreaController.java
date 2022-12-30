package controllers;


import client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class EktCustomerHomeAreaController{

    @FXML
    private Button btnCreateOrder;

    @FXML
    private Button btnViewOrders;

    @FXML
    private Label statusLabel;

    @FXML
    private Text txtWelcomeText;

	@FXML
	public void initialize() {
		this.txtWelcomeText.setText("Welcome back " + ClientController.getCurrentSystemUser().getFirstName());
	}

	// TODO: DO!
	@FXML public void getBtnViewOrders(ActionEvent event) {
		
		assert 1 == 0;
	}

	@FXML public void getBtnCreateOrder(ActionEvent event) {
		
		assert 1 == 0;
	}

}