package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
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
import logic.Role;


// Rotem - renamed this from CEOPageController (was requested here)
// 30.12 de(b)t maroz (nik)

public class EktReportSelectFormController extends Application{
	private ArrayList<?> machinesList;
	
	@FXML
	private Button btnViewOrderReportSingleRegion;
	
	@FXML
	private Button btnViewCustomerReportsSingleRegion;
	
    @FXML
    private Button btnBack;

    @FXML
    private Button buttonViewCustomerReports;

    @FXML
    private Button buttonViewInventoryReports;

    @FXML
    private Button buttonViewOrderReports;

    @FXML
    public ComboBox<String> comboBoxCustomerReports;

    @FXML
    public ComboBox<String> comboBoxInventoryReports;

    @FXML
    public ComboBox<String> comboBoxMonthCustomerReports;

    @FXML
    public ComboBox<String> comboBoxMonthOrderReports;

    @FXML
    public ComboBox<String> comboBoxOrderReports;

    @FXML
    public ComboBox<String> comboBoxYearCustomerReports;

    @FXML
    public ComboBox<String> comboBoxYearOrderReports;

    @FXML
    private Text customerErrorMessage;

    @FXML
    private Text inventoryErrorMessage;

    @FXML
    private Text orderErrorMessage;

    @FXML
    private Text txtRegion;

    @FXML
    private VBox vboxCEO;

	
	@FXML
	public void initialize() {
		if (ClientUI.clientController.getCurrentSystemUser().getRole().equals(Role.REGIONAL_MANAGER)) {
			setUpRegionalManagerPage();
		} else {
			setUpDivisionManagerPage();
		}
		
	}
	
	
	private void setUpDivisionManagerPage() {
		//Setup orderReports combo box
		ObservableList<String> comboOrders = FXCollections.observableArrayList();
		
		//Setup InventoryReports combo box
		ObservableList<String> comboInventory = FXCollections.observableArrayList();
		
		//Setup CustomerReports combo box
		ObservableList<String> comboCustomers = FXCollections.observableArrayList();
		
		ObservableList<String> comboMonths = FXCollections.observableArrayList(
			"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		
		ObservableList<String> comboYears = FXCollections.observableArrayList();
		
		int year = Year.now().getValue();
		for (int i = 0; i < 10; i++) {
			comboYears.add("" + year--);
		}
		//Set months and years for the combo boxes
		comboBoxMonthOrderReports.setItems(comboMonths);
		comboBoxYearOrderReports.setItems(comboYears);
		comboBoxMonthCustomerReports.setItems(comboMonths);
		comboBoxYearCustomerReports.setItems(comboYears);
		
		SCCP fetchMachines = new SCCP();
		fetchMachines.setRequestType(ServerClientRequestTypes.SELECT);
		fetchMachines.setMessageSent(new Object[] {"machine", false, null, false, null, true, "LEFT JOIN locations on machine.locationId = locations.locationID"});
		
		ClientUI.clientController.accept(fetchMachines);
		
		//ClientController.getMessageSent() -> returns ArrayList of ArrayListst of objects
		machinesList = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		
		for (ArrayList<Object> machine : (ArrayList<ArrayList<Object>>)machinesList) {
			comboOrders.add((String) machine.get(5) + "-" + (String) machine.get(2));
			comboInventory.add((String) machine.get(5)+ "-" + (String) machine.get(2));
			comboCustomers.add((String) machine.get(5) + "-" + (String) machine.get(2));
		}
		
		//Set combo boxes of 
		comboBoxOrderReports.setItems(comboOrders);
		comboBoxInventoryReports.setItems(comboInventory);
		comboBoxCustomerReports.setItems(comboCustomers);

	}


	//Setup CEOpage before launch
	public void setUpRegionalManagerPage() {
		//Setup orderReports combo box
		ObservableList<String> comboOrders = FXCollections.observableArrayList();
		
		//Setup InventoryReports combo box
		ObservableList<String> comboInventory = FXCollections.observableArrayList();
		
		//Setup CustomerReports combo box
		ObservableList<String> comboCustomers = FXCollections.observableArrayList();
		
		ObservableList<String> comboMonths = FXCollections.observableArrayList(
			"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		
		ObservableList<String> comboYears = FXCollections.observableArrayList();
		
		int year = Year.now().getValue();
		for (int i = 0; i < 10; i++) {
			comboYears.add("" + year--);
		}
		//Set months and years for the combo boxes
		comboBoxMonthOrderReports.setItems(comboMonths);
		comboBoxYearOrderReports.setItems(comboYears);
		comboBoxMonthCustomerReports.setItems(comboMonths);
		comboBoxYearCustomerReports.setItems(comboYears);
		
		txtRegion.setText(ClientController.getCurrentUserRegion() + " Region");
		txtRegion.setLayoutX(400 - (txtRegion.minWidth(0))/2);
		
		SCCP fetchMachines = new SCCP();
		fetchMachines.setRequestType(ServerClientRequestTypes.SELECT);
		fetchMachines.setMessageSent(new Object[] {"machine", false, null, false, null, true, "LEFT JOIN locations on machine.locationId = locations.locationID WHERE locations.locationName = \""
				+ ClientController.getCurrentUserRegion() + "\" ORDER BY locationName;"});
		
		ClientUI.clientController.accept(fetchMachines);
		
		//ClientController.getMessageSent() -> returns ArrayList of ArrayListst of objects
		machinesList = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		
		for (ArrayList<Object> machine : (ArrayList<ArrayList<Object>>)machinesList) {
			comboOrders.add((String) machine.get(5) + "-" + (String) machine.get(2));
			comboInventory.add((String) machine.get(5)+ "-" + (String) machine.get(2));
			comboCustomers.add((String) machine.get(5) + "-" + (String) machine.get(2));
		}
		
		//Set combo boxes of 
		comboBoxOrderReports.setItems(comboOrders);
		comboBoxInventoryReports.setItems(comboInventory);
		comboBoxCustomerReports.setItems(comboCustomers);
	}
	
	public void getBtnOrderReports(ActionEvent event) throws Exception {
		
		String month = (String) comboBoxMonthOrderReports.getSelectionModel().getSelectedItem();
		String year = (String) comboBoxYearOrderReports.getSelectionModel().getSelectedItem();
		
		if (comboBoxMonthOrderReports.getSelectionModel().isEmpty() || 
				comboBoxYearOrderReports.getSelectionModel().isEmpty() || comboBoxOrderReports.getSelectionModel().isEmpty() ){
			orderErrorMessage.setText("Please fill in all required fields");
			return;
		}
		
		String[] locationAndMachineName = comboBoxOrderReports.getValue().split("-");
		
		ClientController.getMachineID_TypeOfReport_Dates().add("Orders"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[0]); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[1]); //Add chosen machineName to the array
		//Add the chosen date to view reports
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxMonthOrderReports.getValue());
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxYearOrderReports.getValue());
		
		for (String s : ClientController.getMachineID_TypeOfReport_Dates()) {
			System.out.println(s);
		}


		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
	
	public void getBtnInventoryReports(ActionEvent event) throws Exception {
		
		if (comboBoxInventoryReports.getSelectionModel().isEmpty() == true) {
			inventoryErrorMessage.setText("Please fill in all required fields");
			return;
		}
		
		String[] locationAndMachineName = comboBoxInventoryReports.getValue().split("-");
		
		ClientController.getMachineID_TypeOfReport_Dates().add("Inventory"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[0]); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[1]); //Add chosen machineName to the array
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
		//Implement Inventory reports
	}
	
	public void getBtnCustomerReports(ActionEvent event) throws Exception {
		String month = comboBoxMonthCustomerReports.getValue();
		String year = comboBoxYearCustomerReports.getValue();
		
		if (month == null || year == null || comboBoxCustomerReports.getSelectionModel().isEmpty() == true) {
			customerErrorMessage.setText("Please fill in all required fields");
			return;
		}
		
		String[] locationAndMachineName = comboBoxCustomerReports.getValue().split("-");
		
		ClientController.getMachineID_TypeOfReport_Dates().add("Customer"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[0]); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[1]); //Add chosen machineName to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxMonthCustomerReports.getValue());
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxYearCustomerReports.getValue());
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
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
		if (ClientController.getCurrentSystemUser().getRole().equals(Role.REGIONAL_MANAGER)) {
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktRegionalManagerHomePage.fxml", null, "Regional Manager Home Page", true);
		} else {
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktDivisionManagerHomePage.fxml", null, "Regional Manager Home Page", true);

		}
		primaryStage.show();  
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
	}

	
	//Manager pressed on all reports button
    @FXML
    void getBtnViewCustomerReportsSingleRegion(ActionEvent event) {
    	if (comboBoxMonthCustomerReports.getSelectionModel().isEmpty() || comboBoxYearCustomerReports.getSelectionModel().isEmpty()) {
    		customerErrorMessage.setText("Please fill in all required fields");
			return;
    	}
    	ClientController.getMachineID_TypeOfReport_Dates().add("Customer"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(ClientController.getCurrentUserRegion()); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add("ALL_MACHINES"); //Add chosen machineName to the array
		//Add the chosen date to view reports
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxMonthCustomerReports.getValue());
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxYearCustomerReports.getValue());
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    }
    
    @FXML
    void getBtnViewOrderReportSingleRegion(ActionEvent event) {
    	if (comboBoxMonthOrderReports.getSelectionModel().isEmpty() || comboBoxYearOrderReports.getSelectionModel().isEmpty()) {
    		orderErrorMessage.setText("Please fill in all required fields");
    		return;
    	}	
		ClientController.getMachineID_TypeOfReport_Dates().add("Orders"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(ClientController.getCurrentUserRegion()); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add("ALL_MACHINES"); //Add chosen machineName to the array
		//Add the chosen date to view reports
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxMonthOrderReports.getValue());
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxYearOrderReports.getValue());
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    }
}
