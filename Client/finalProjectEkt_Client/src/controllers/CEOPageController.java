package controllers;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CEOPageController extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
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
	
	//Setup CEOpage before launch
	public void setUpCEOPage() {
		//Setup orderReports combo box
		ObservableList<String> comboOrders = FXCollections.observableArrayList("Kiryat Motzkin, Hahagana 52",
				"Karmiel, Kramim 5", "Jerusalem, Rashi 3", "Haifa, Horev 15", "Tel Aviv, Rothschild 60");
		comboBoxOrderReports.setItems(comboOrders);
		
		//Setup InventoryReports combo box
		ObservableList<String> comboInventory = FXCollections.observableArrayList("Kiryat Motzkin, Hahagana 52",
				"Karmiel, Kramim 5", "Jerusalem, Rashi 3", "Haifa, Horev 15", "Tel Aviv, Rothschild 60");
		comboBoxOrderReports.setItems(comboInventory);
		
		//Setup CustomerReports combo box
		ObservableList<String> comboCustomers = FXCollections.observableArrayList("Kiryat Motzkin, Hahagana 52",
				"Karmiel, Kramim 5", "Jerusalem, Rashi 3", "Haifa, Horev 15", "Tel Aviv, Rothschild 60");
		comboBoxOrderReports.setItems(comboCustomers);
	}
	
	
	public void getBtnOrderReports(ActionEvent event) throws Exception {
		setUpCEOPage();
		LocalDate startLocalDate = startDateOrderReports.getValue();
		LocalDate endLocalDate = endDateOrderReports.getValue();
		
		if (startLocalDate == null || endLocalDate == null || comboBoxOrderReports.getValue() == null) {
			orderErrorMessage.setText("Please fill in all empty fields");
			return;
		}

		//Implement Order reports
	}
	
	public void getBtnInventoryReports(ActionEvent event) throws Exception {
		
		if (comboBoxInventoryReports.getValue() == null) {
			inventoryErrorMessage.setText("Please fill in all empty fields");
			return;
		}
		
		//Implement Inventory reports
	}
	
	public void getBtnCustomerReports(ActionEvent event) throws Exception {
		LocalDate startLocalDate = startDateCustomerReports.getValue();
		LocalDate endLocalDate = endDateCustomerReports.getValue();
		
		if (startLocalDate == null || endLocalDate == null || comboBoxOrderReports.getValue() == null) {
			customerErrorMessage.setText("Please fill in all empty fields");
			return;
		}
		
		//Implement Customer reports
	}
	
	
	
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/CEOPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public void getBtnLogout(ActionEvent event) throws Exception {
		
		//Implelent logout
	}
	
}
