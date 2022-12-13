package controllers;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public  class EkrutUserLoginController   {
	@FXML 
	private Button btnExit;
	
	@FXML
	private Button btnUpdateCustomer;
	
	@FXML
	private Button btnAddCustomer;
	
	@FXML
	private Button btnDisplayCustomer;
	
	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("Worker Has Exited The Academic Tool");
		ClientUI.clientController.client.closeConnection();
		System.exit(0);
	}

	
}
