package controllers;

import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class OrderReceiptPageController {
	@FXML
	private Button btnMainMenu;
	
	@FXML 
	private Button btnLogout;
	
	public void initialize() {
		//Implement email and SMS sending
	}

	public void getBtnMainMenu(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktCatalogForm.fxml", null, "Ekt Catalog");
		primaryStage.setOnCloseRequest(we -> 
		{
			System.out.println("Pressed the X button."); 
			System.exit(0);
		}
		);
		primaryStage.show();
		((Stage)((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
	}
	
	public void getBtnLogout(ActionEvent event) {
		//Implement set user to logged out in database
	}
	
}
