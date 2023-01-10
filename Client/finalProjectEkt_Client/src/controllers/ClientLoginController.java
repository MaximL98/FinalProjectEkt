package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientController;
import client.ClientUI;
import client.Configuration;
import common.SCCP;
import common.ServerClientRequestTypes;
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
import logic.Machine;
import javafx.scene.control.ComboBox;

/**
 * Important: Read this - The תצורה is OL - OL = when you buy from home or somewhere - the administrative part of the shistem.
 * 										   EK = when you physically assault the machine
 * So, here, when we log in, to use the shit we made - we must choose OL - and this is the default תזורה
 * @author Rotem
 *
 */

public class ClientLoginController {
	@FXML
	private TextField txtIP;
	@FXML 
	private Button btnConnect;
	
	@FXML
	private Label hiddenLabel;
	
	@FXML ComboBox<String> cmbTezura;
	@FXML ComboBox<String> cmbMachines;
	@FXML Button btnFinish;
	
	@FXML
	void initialize() {
		cmbTezura.getItems().add("EK");
		cmbTezura.getItems().add("OL");
		cmbTezura.setValue("OL");
	}
	
	//start primary stage
		public void start(Stage primaryStage) throws Exception {
			WindowStarter.createWindow(primaryStage, this, "/gui/ClientLoginForm.fxml", "/gui/ClientLogin.css", "Login");
			primaryStage.show();	 	
		}
	
	
	
	public void getConnectToServer(ActionEvent event) {
		hiddenLabel.setVisible(false);
		
		try{
			ClientController.setLaunchConfig(Configuration.valueOf(cmbTezura.getValue()));
		}catch(IllegalArgumentException ex) {
			System.err.println("Invalid configuration!");
			return;
		}
		
		System.out.println("Client is connecting to server");
		String tmp = txtIP.getText();
		if(tmp.equals(""))
			tmp = "localhost";
		ClientUI.serverIP = tmp;
		try {
			ClientUI.connectToServer();
			
			if(cmbTezura.getValue().equals("EK")) {
				// expose the option to select a machine!
				cmbMachines.setVisible(true);
				btnFinish.setVisible(true);
				// get all machines:
				List<String> machines;
				try {
					machines = getExistingMachinesFromServer();
					// insert them to the box:
					for(String machine: machines) {
						cmbMachines.getItems().add(machine);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				hiddenLabel.setText("Please choose a machine and click Finish");
				hiddenLabel.setVisible(true);
				
				System.out.println("Client message: Please choose a machine and click Finish");
				return;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hiddenLabel.setText("Could not connect to " + tmp + " over port " + 5555);
			hiddenLabel.setVisible(true);
			return;
		}

		// this line is to add stuff to db in a mesudar fashion
		//WindowStarter.createWindow(primaryStage, this, "/gui/AddUserToDbForm.fxml", null, "dbg");

		
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login");

		
		/*switch(ClientController.getLaunchConfig()) {
		case OL:
			break;
		case EK:
			

			//throw new UnsupportedOperationException("EK Conf is not supported yet");
			break;
		default:
			throw new IllegalStateException("This should never happen");
		}*/
		
		System.out.println("Client is now connected to server");
		primaryStage.show();	 	

		/*Pane root = loader.load(getClass().getResource("/gui/EkrutUserLoginForm.fxml").openStream());

		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/EkrutUserLoginForm.css").toExternalForm());
		primaryStage.setTitle("Worker Page");

		primaryStage.setScene(scene);		
		primaryStage.show();*/
	}



	@FXML public void setTezura(ActionEvent event) {
		System.out.println("Switched to " + cmbTezura.getValue());
	}

	private List<String> getExistingMachinesFromServer() throws IOException {
		ClientUI.clientController.client.openConnection();
		ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.REQUEST_ALL_MACHINES, ""));
		ClientUI.clientController.client.closeConnection();

		// we want to check the response from server
		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)){
			// we want the List<String> where each string is the name of a machine
			@SuppressWarnings("unchecked")
			List<String> tmp = (List<String>)ClientController.responseFromServer.getMessageSent();
			return tmp;
		}
		else{
			System.out.println("FAILURE");
		}
		return null;
	}

	@FXML public void chooseMachine(ActionEvent event) {
		System.out.println("Chose machine " + cmbMachines.getValue());
		ClientController._EkCurrentMachineName = cmbMachines.getValue();
	}

	@FXML public void getFinishEkConfig(ActionEvent event) {
		// send query to get the machine ID (yeah yeah):
		SCCP msg = new SCCP(ServerClientRequestTypes.SELECT, 
				new Object[]{"machine", true, "machineId", true, "machineName = '" +ClientController._EkCurrentMachineName+ "'", false, null});
		ClientUI.clientController.accept(msg);
		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> tmp= (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
			System.out.println(tmp);
			ClientController._EkCurrentMachineID = (Integer.valueOf(tmp.get(0).get(0).toString()));
			System.out.println("Machine ID set to " + ClientController._EkCurrentMachineID);
		}
		else {
			throw new RuntimeException("Error getting machine ID from database");
		}
		
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationLoginFrame.fxml", null, "Login");
		primaryStage.show();
	}
}
