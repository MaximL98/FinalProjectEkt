package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ResourceBundle;

import common.WindowStarter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.OnlineOrder;
import logic.OnlineOrder.*;

public class DeliveryManagerPageController extends Application implements Initializable{

	@FXML
	private Text welcomeText;

	@FXML
	private TableView<OnlineOrder> deliveryTable;

	@FXML
	private TableColumn<OnlineOrder, String> tblOrderNumberColumn;

	@FXML
	private TableColumn<OnlineOrder, LocalDate> tblDateReceivedColumn;

	@FXML
	private TableColumn<OnlineOrder, LocalDateTime> tblTimeColumn;

	@FXML
	private TableColumn<OnlineOrder, ComboBox<String>> tblStatusColumn;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnUpdate;

	@FXML
	void getBtnBack(ActionEvent event) {

	}

	@FXML
	void getBtnUpdate(ActionEvent event) {
		
	}


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		WindowStarter.createWindow(primaryStage, this, "/gui/DeliveryManagerPage.fxml", "/gui/DeliveryManager.css",
				"Delivery Management");
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tblOrderNumberColumn.setCellValueFactory(new PropertyValueFactory<OnlineOrder,String>("orderID"));
		tblDateReceivedColumn.setCellValueFactory(new PropertyValueFactory<OnlineOrder,LocalDate>("dateReceived"));
		tblTimeColumn.setCellValueFactory(new PropertyValueFactory<OnlineOrder,LocalDateTime>("deliveryTime"));
		tblStatusColumn.setCellValueFactory(new PropertyValueFactory<OnlineOrder, ComboBox<String>>("statusCombo"));
		
		tblStatusColumn.setStyle("-fx-alignment: CENTER;");
		deliveryTable.setItems(getOrders());
	}
	
	
	public ObservableList<OnlineOrder> getOrders(){
		ObservableList<OnlineOrder> orders = FXCollections.observableArrayList();
		orders.add(new OnlineOrder("1", 5, "test", "Somewhere", LocalDate.of(2022,Month.DECEMBER,25),LocalDateTime.of(LocalDate.of(2023,Month.JANUARY,15), LocalTime.of(12, 0)) ,Type.DELIVERY, Status.InProgress));
		orders.add(new OnlineOrder("2", 2, "test2", "Somewhere", LocalDate.of(2022,Month.DECEMBER,22),LocalDateTime.of(LocalDate.of(2023,Month.JANUARY,10), LocalTime.of(10, 0)), Type.PICKUP, Status.InProgress));
		return orders;
	}

}
