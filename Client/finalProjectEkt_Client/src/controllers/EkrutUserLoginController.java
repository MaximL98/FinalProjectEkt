package controllers;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public  class EkrutUserLoginController   {
	@FXML 
	private Button btnExit;
	
	@FXML
	private Button btnUpdateCustomer;
	
	@FXML
	private Button btnAddCustomer;
	
	@FXML
	private Button btnDisplayCustomer;

	@FXML Button addUserBtn;
	
	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("Worker Has Exited The Academic Tool");
		ClientUI.clientController.client.closeConnection();
		System.exit(0);
	}

	@FXML public void addUserToDB(ActionEvent event) throws OperationNotSupportedException {
		FXMLLoader loader = new FXMLLoader();

		
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root;
		try {
			
			root = loader.load(getClass().getResource("/gui/AddUserToDbForm.fxml").openStream());
			Scene scene = new Scene(root);			
			scene.getStylesheets().add(getClass().getResource("/gui/AddUserToDbForm.css").toExternalForm());
			primaryStage.setTitle("Worker Page");

			primaryStage.setScene(scene);		
			primaryStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}
