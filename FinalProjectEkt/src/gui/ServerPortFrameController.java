package gui;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Faculty;
import logic.Student;
import ocsf.server.ConnectionToClient;
import Server.DatabaseController;
import Server.EktServer;
import Server.ServerUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import javafx.scene.control.TableView;
import javafx.scene.control.PasswordField;

public class ServerPortFrameController  {
	//private StudentFormController sfc;	
	
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
	
	
	
	private String getport() {
		return portxt.getText();			
	}
	
	public void clickConnectBtn(ActionEvent event) throws Exception {
		String p;
		
		p=getport();
		if(p.trim().isEmpty()) {
			System.out.println("You must enter a port number");
					
		}
		else
		{
			
			// try asking database controller to log in using the text fields
			DatabaseController.setDatabaseUserName(databaseUsernameTxt.getText());
			DatabaseController.setDatabasePassword(databasePasswdTxt.getText());
			
			if(!DatabaseController.checkLoginCredentials()) {
				System.out.println("Database username or password is incorrect, disconnecting server!");
				return;
			}
			
			// start server (this starts connection to database too)
			ServerUI.runServer(p);
		}
	}

	public void start(Stage primaryStage) throws Exception {	
		// load server window
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));
				
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerPort.css").toExternalForm());
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		
		primaryStage.show();		
	}
	
	public void getExitBtn(ActionEvent event)  {
		System.out.println("exit Academic Tool");
		// refactored
		ServerUI.serverForcedShutdown();
		System.exit(0);			
	}

}