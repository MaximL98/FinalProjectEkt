package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import client.ClientController;

import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Machine;


// Rotem - renamed this from CEOPageController (was requested here)
// 30.12 de(b)t maroz (nik)

public class EktReportSelectFormController extends Application{
	private ArrayList<?> machinesList;
	
	@FXML
	private Button buttonViewOrderReports;
	
	@FXML
	private Button buttonViewInventoryReports;
	
	@FXML
	private Button buttonViewCustomerReports;
	
	@FXML 
	private DatePicker startDateOrderReports;
	
	@FXML 
	private DatePicker endDateOrderReports;
	
	@FXML 
	private DatePicker startDateCustomerReports;
	
	@FXML 
	private DatePicker endDateCustomerReports;
	
	@FXML
	private Text orderErrorMessage;
	
	@FXML
	private Text inventoryErrorMessage;
	
	@FXML
	private Text customerErrorMessage;
	
	@FXML 
	private ComboBox<String> comboBoxOrderReports;
	
	@FXML 
	private ComboBox<String> comboBoxInventoryReports;
	
	@FXML 
	private ComboBox<String> comboBoxCustomerReports;

	@FXML VBox vboxCEO;
	
	@FXML
	public void initialize() {
		setUpCEOPage();
	}
	
	//Setup CEOpage before launch
	public void setUpCEOPage() {
		//Setup orderReports combo box
		ObservableList<String> comboOrders = FXCollections.observableArrayList();
		
		//Setup InventoryReports combo box
		ObservableList<String> comboInventory = FXCollections.observableArrayList();
		
		
		//Setup CustomerReports combo box
		ObservableList<String> comboCustomers = FXCollections.observableArrayList();
		
		startDateCustomerReports.setStyle("-fx-primary-color: crimson;");
		endDateCustomerReports.setStyle("-fx-background: crimson;");
		startDateOrderReports.setStyle("-fx-background: crimson;");
		endDateOrderReports.setStyle("-fx-background: crimson;");
		
		SCCP fetchMachines = new SCCP();
		fetchMachines.setRequestType(ServerClientRequestTypes.SELECT);
		fetchMachines.setMessageSent(new Object[] {"machine", false, null, false, null, true, "LEFT JOIN locations on machine.locationId = locations.locationID ORDER BY locationName;"});
		
		ClientUI.clientController.accept(fetchMachines);
		
		//ClientController.getMessageSent() -> returns ArrayList of ArrayListst of objects
		machinesList = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		
		for (ArrayList<Object> machine : (ArrayList<ArrayList<Object>>)machinesList) {
			comboOrders.add((String) machine.get(5) + "-" + (String) machine.get(3));
			comboInventory.add((String) machine.get(5)+ "-" + (String) machine.get(3));
			comboCustomers.add((String) machine.get(5) + "-" + (String) machine.get(3));
		}
		//Set combo boxes of 
		comboBoxOrderReports.setItems(comboOrders);
		comboBoxInventoryReports.setItems(comboInventory);
		comboBoxCustomerReports.setItems(comboCustomers);
		
	}
	
	
	public void getBtnOrderReports(ActionEvent event) throws Exception {
		
		LocalDate startLocalDate = startDateOrderReports.getValue();
		LocalDate endLocalDate = endDateOrderReports.getValue();
		
		if (startLocalDate == null || endLocalDate == null || comboBoxOrderReports.getSelectionModel().isEmpty() == true) {
			orderErrorMessage.setText("Please fill in all empty fields");
			return;
		}
		
		String[] locationAndMachineName = comboBoxOrderReports.getValue().split("-");
		
		ClientController.getMachineID_AndReportType().add("Orders"); //Add type of report to view to the array
		ClientController.getMachineID_AndReportType().add(locationAndMachineName[0]); //Add chosen location to the array
		ClientController.getMachineID_AndReportType().add(locationAndMachineName[1]); //Add chosen machineName to the array
		//Add the chosen dates to view reports
		ClientController.getRequestedOrderDates().add(startDateOrderReports.getValue());
		ClientController.getRequestedOrderDates().add(endDateOrderReports.getValue());


		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form");
		primaryStage.setOnCloseRequest(we -> {
			System.out.println("Pressed the X button.");
			System.exit(0);
		});
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window

		//Implement Order reports
	}
	
	
	
	public void getBtnInventoryReports(ActionEvent event) throws Exception {
		
		if (comboBoxInventoryReports.getSelectionModel().isEmpty() == true) {
			inventoryErrorMessage.setText("Please fill in all empty fields");
			return;
		}
		
		String[] locationAndMachineName = comboBoxInventoryReports.getValue().split("-");
		
		ClientController.getMachineID_AndReportType().add("Inventory"); //Add type of report to view to the array
		ClientController.getMachineID_AndReportType().add(locationAndMachineName[0]); //Add chosen location to the array
		ClientController.getMachineID_AndReportType().add(locationAndMachineName[1]); //Add chosen machineName to the array

		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form");
		primaryStage.setOnCloseRequest(we -> {
			System.out.println("Pressed the X button.");
			System.exit(0);
		});
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
		//Implement Inventory reports
	}
	
	public void getBtnCustomerReports(ActionEvent event) throws Exception {
		LocalDate startLocalDate = startDateCustomerReports.getValue();
		LocalDate endLocalDate = endDateCustomerReports.getValue();
		
		if (startLocalDate == null || endLocalDate == null || comboBoxCustomerReports.getSelectionModel().isEmpty() == true) {
			customerErrorMessage.setText("Please fill in all empty fields");
			return;
		}
		
		String[] locationAndMachineName = comboBoxCustomerReports.getValue().split("-");
		
		ClientController.getMachineID_AndReportType().add("Customer"); //Add type of report to view to the array
		ClientController.getMachineID_AndReportType().add(locationAndMachineName[0]); //Add chosen location to the array
		ClientController.getMachineID_AndReportType().add(locationAndMachineName[1]); //Add chosen machineName to the array
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form");
		primaryStage.setOnCloseRequest(we -> {
			System.out.println("Pressed the X button.");
			System.exit(0);
		});
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
		//Implement Customer reports
	}
	
	//May not be needed
	public void start(Stage primaryStage) throws Exception {
		// Rotem - changed this line - interesting that no one noticed I broke this previously
		Parent root = FXMLLoader.load(getClass().getResource("/gui/EktReportSelectForm.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/*
	 * Rotem: merge master into rotem - I chose Dima version here, I used logout function but this is just a back-button.
	 */
	public void getBtnLogout(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktRegionalManagerHomePage.fxml", null, "Regional Manager Home Page");
		primaryStage.setOnCloseRequest(we -> {
			System.out.println("Pressed the X button."); 
			System.exit(0);
		});
		primaryStage.show();  
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
	}

	
}
