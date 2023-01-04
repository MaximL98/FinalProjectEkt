package gui;

import java.io.IOException;

import Server.ServerUI;
import common.WindowStarter;
import database.DatabaseController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;

public class ServerPortController  {
	
	String temp="";
	
	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnConnect = null;
	@FXML
	private Label lbllist;
	
	@FXML
	private TextField portxt;
	ObservableList<String> list;

	// additions:
	
	@FXML TextField databaseUsernameTxt;

	@FXML TableView<ConnectionToClient> clientsTable;

	@FXML PasswordField databasePasswdTxt;

	@FXML Button addUserToDB;

	@FXML Button btnDisconnect;
	
	public void start(Stage primaryStage) throws Exception {	
		// load server window
		WindowStarter.createWindow(primaryStage, this, "/gui/ServerPort.fxml", "/gui/ServerPort.css", "Server");
		
		/*Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));
				
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerPort.css").toExternalForm());
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		*/
		// here, we don't catch the X button, since we already did it in ServerUI's main
		primaryStage.show();		
	}
	
	private String getport() {
		return portxt.getText();			
	}

	
	private String getDbUser() {
		return databaseUsernameTxt.getText();			
	}
	
	private String getDbPass() {
		return databasePasswdTxt.getText();			
	}
	
	public void clickConnectBtn(ActionEvent event) throws Exception {		
		// try asking database controller to log in using the text fields
		String p = getport();
		DatabaseController.setDatabaseUserName(getDbUser());
		DatabaseController.setDatabasePassword(getDbPass());
		
		
		if(p.trim().isEmpty()) {
			System.out.println("You must enter a port number");
					
		}
		else if(!DatabaseController.checkLoginCredentials()) {
			System.out.println("Database username or password is incorrect, disconnecting server!");
		}
		else
		{
			// start server (this starts connection to database too)
			ServerUI.runServer(p);
			
			// allow the user of the server to insert users to the database:
			addUserToDB.setDisable(false);
			
			// toggle disconnect button
			btnDisconnect.setVisible(true);
			btnDisconnect.setDisable(false);
			
			// toggle connect button
			btnConnect.setVisible(false);
			btnConnect.setDisable(true);
		}
	}
	
	public void getExitBtn(ActionEvent event)  {
		System.out.println("exit Academic Tool");
		ServerUI.serverForcedShutdown();
		// TODO:
		// find a better way to do this
		System.exit(0);			
	}

	@FXML public void getAddUserToDbBtn(ActionEvent event) throws IOException {
		// start a new window with a selection tool for all the tables in the database.
		// when user selects a table, show it, and show all fields to fill
		System.out.println("Server is loading Database Control page");
		
		FXMLLoader loader = new FXMLLoader();

		
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/ServerDatabaseAdditionForm.fxml").openStream());
		//UpdateCustomerController updateCustomerController = loader.getController();		
		//UpdateCustomerController.loadStudent(ChatClient.s1);
	
		Scene scene = new Scene(root);			
		primaryStage.setTitle("Database Control");

		primaryStage.setScene(scene);		
		primaryStage.show();
	}

	@FXML public void clickDisconnectBtn(ActionEvent event) {
		// similar to exit, we stop the connection to the database, and shut down the server:
		ServerUI.serverForcedShutdown();
		// but we don't close the program, allowing re-connection
		// NOT System.exit(0)
		
		// now, re-allow connect button and disable disconnect button
		
		// toggle disconnect button
		btnConnect.setVisible(true);
		btnConnect.setDisable(false);
		
		// toggle connect button
		btnDisconnect.setVisible(false);
		btnDisconnect.setDisable(true);
		
		// and disable "add user to db" button
		addUserToDB.setDisable(true);

	}

}