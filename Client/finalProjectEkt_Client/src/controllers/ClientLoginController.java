package controllers;

import java.io.IOException;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientLoginController {
	@FXML
	private TextField txtIP;
	@FXML 
	private Button btnConnect;
	
	
	//start primary stage
		public void start(Stage primaryStage) throws Exception {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/ClientLoginForm.fxml"));

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/gui/ClientLogin.css").toExternalForm());
			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
			
			primaryStage.show();	 	   
		}
	
	
	
	public void getConnectToServer(ActionEvent event) throws IOException {
		System.out.println("Worker is in connect to server");
		String tmp = txtIP.getText();
		if(tmp.equals(""))
			tmp = "localhost";
		ClientUI.serverIP = tmp;
		ClientUI.connectToServer();
		FXMLLoader loader = new FXMLLoader();

		
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/EkrutUserLoginForm.fxml").openStream());
		//UpdateCustomerController updateCustomerController = loader.getController();		
		//updateCustomerController.loadCustomer(EKTClient.customer);
	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/EkrutUserLoginForm.css").toExternalForm());
		primaryStage.setTitle("Worker Page");

		primaryStage.setScene(scene);		
		primaryStage.show();
	}
}
