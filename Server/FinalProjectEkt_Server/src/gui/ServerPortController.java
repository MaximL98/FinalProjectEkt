package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Server.ServerUI;
import common.WindowStarter;
import database.DatabaseController;
import database.DatabaseOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ocsf.server.ConnectionToClient;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;

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

	@FXML TableView<String> clientsTable;

	@FXML PasswordField databasePasswdTxt;

	@FXML Button addUserToDB;

	@FXML Button btnDisconnect;

	private Thread threadForListeningToClients;

	@FXML TableColumn<Object, String> colClients;

	@FXML TextArea txtClients;
	
	@FXML
	private void initialize() {
		//colClients.setCellValueFactory(null);
		addUserToDB.setText("Import Simulation (one time use)");
	}
	
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
	
	@FXML
	public void onEnter(ActionEvent ae) throws Exception{
		if(ServerUI.getEktServerObject() != null && ServerUI.getEktServerObject().isListening()) {
			return;
		}
		else {
		clickConnectBtn(ae);
		}
	}
	
	@FXML
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
			
			// load clients
			threadForListeningToClients = new Thread() {
				private Thread[] oldClientList = new Thread[1];
				private Thread[] newClientList;

				/*
				 * Rotem: Added a custom listener that lists the active clients at any time
				 */
				@Override
				public void run() {
					System.out.println("Helper server thread for showing connected clients has been created (and started).");
					while(ServerUI.getEktServerObject()!=null && ServerUI.getEktServerObject().isListening()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(!oldClientList.equals(ServerUI.getEktServerObject().getClientConnections())) {
							oldClientList=ServerUI.getEktServerObject().getClientConnections();
							newClientList = ServerUI.getEktServerObject().getClientConnections();
							txtClients.setText("");
							for(Thread t : newClientList) {
								ConnectionToClient client = (ConnectionToClient)t; // please work
								txtClients.setText(txtClients.getText()
										+client.getInetAddress().toString().replace("/", "")
										+"\n");
							}
							
						}
					}
					System.out.println("Helper server thread for showing connected clients has been closed.");
				}
			};
			threadForListeningToClients.start();
			
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
		// what happens here:
		// we read the user-management table (one big table), an external table with all user related info
		// we read a table of the format: everything_in_systemuser + location (for managers)
		// origin schema.table=ektdb.external_users
		// we place every user in the appropriate tables
		// insert into: systemuser, manager_location, worker (maybe worker is not needed?)
		// For now, I only insert to systemuser and to manager_location (in case typeOfUser=regional_manager)
		Object res = DatabaseController.handleQuery(DatabaseOperation.IMPORT_SIMULATION, new Object[] {});
		if(res instanceof Boolean) {
			String popupTitle, pHeader, pText;
			popupTitle = "Import Simulation";
			pHeader = "Import simulation succeeded!";
			pText = "Click here to close this popup";
			Boolean bRes = (Boolean)res;
			if(bRes) {
				// alert success
				System.out.println("Succcessfully imported data from external table \"external_users\" in schema ektdb.");
			}
			else {
				// alert failure
				System.out.println("Failure: importing data from external table "
						+ "\"external_users\" in schema ektdb has failed, please check the table's state and entries.");
				pHeader = "Failure importing data!";
			}
    		Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle(popupTitle);
            alert.setHeaderText(pHeader);
            alert.setContentText(pText);
            alert.showAndWait();
            addUserToDB.setDisable(true);
//            if (result.get() == ButtonType.OK) {
		}
		
		// OLD VERSION:
		// start a new window with a selection tool for all the tables in the database.
		// when user selects a table, show it, and show all fields to fill	
//		System.out.println("Server is loading Database Control page");
//		
//		FXMLLoader loader = new FXMLLoader();
//
//		
//		Stage primaryStage = new Stage();
//		Pane root = loader.load(getClass().getResource("/gui/ServerDatabaseAdditionForm.fxml").openStream());
//		//UpdateCustomerController updateCustomerController = loader.getController();		
//		//UpdateCustomerController.loadStudent(ChatClient.s1);
//	
//		Scene scene = new Scene(root);			
//		primaryStage.setTitle("Database Control");
//
//		primaryStage.setScene(scene);		
//		primaryStage.show();
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