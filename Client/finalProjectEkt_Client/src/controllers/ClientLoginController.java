package controllers;

import java.io.IOException;

import client.ClientUI;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientLoginController {
	@FXML
	private TextField txtIP;
	@FXML 
	private Button btnConnect;
	
	@FXML
	private Label hiddenLabel;
	
	//start primary stage
		public void start(Stage primaryStage) throws Exception {
			WindowStarter.createWindow(primaryStage, this, "/gui/ClientLoginForm.fxml", "/gui/ClientLogin.css", "Login");
			primaryStage.show();	 	

		}
	
	
	
	public void getConnectToServer(ActionEvent event) {
		hiddenLabel.setVisible(false);
		System.out.println("Client is connecting to server");
		String tmp = txtIP.getText();
		if(tmp.equals(""))
			tmp = "localhost";
		ClientUI.serverIP = tmp;
		try {
			ClientUI.connectToServer();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hiddenLabel.setText("Could not connect to " + tmp + " over port " + 5555);
			hiddenLabel.setVisible(true);
			return;
		}
			

		
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		// this line is to add stuff to db in a mesudar fashion
		//WindowStarter.createWindow(primaryStage, this, "/gui/AddUserToDbForm.fxml", null, "dbg");
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login");

		// this was done so that we can use this button
		primaryStage.setOnCloseRequest(we -> 
		{
			System.out.println("Pressed the X button."); 
			System.exit(0);
		}
		);
		primaryStage.show();	 	
		System.out.println("Client is now connected to server");

		/*Pane root = loader.load(getClass().getResource("/gui/EkrutUserLoginForm.fxml").openStream());

		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/EkrutUserLoginForm.css").toExternalForm());
		primaryStage.setTitle("Worker Page");

		primaryStage.setScene(scene);		
		primaryStage.show();*/
	}
}
