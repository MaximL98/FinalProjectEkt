package controllers;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.record.Record;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import controllers.EktReportDisplayPageController.productInTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EktCancelOrderPageController {

	public class orderToCancel {
		private SimpleStringProperty id;
		private SimpleStringProperty totalPrice;
		private SimpleStringProperty date;

		public orderToCancel(int id, double totalPrice, String date) {
			this.id = new SimpleStringProperty(new Integer(id).toString());
			this.totalPrice = new SimpleStringProperty(new Double(totalPrice).toString());
			this.date = new SimpleStringProperty(date);
		}
		
		public SimpleStringProperty getTotalPrice() {
			return totalPrice;
		}

		public void setTotalPrice(SimpleStringProperty totalPrice) {
			this.totalPrice = totalPrice;
		}

		public SimpleStringProperty getDate() {
			return date;
		}

		public void setDate(SimpleStringProperty customerName) {
			this.date = customerName;
		}

		public SimpleStringProperty getId() {
			return id;
		}

		public void setId(SimpleStringProperty id) {
			this.id = id;
		}

	}
	
	@FXML
	private Text txtOrderCancelled;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnUpdate;

	@FXML
	private TableColumn<orderToCancel, String> columnOrderID;

	@FXML
	private TableColumn<orderToCancel, String> columnTotalPrice;

	@FXML
	private TableColumn<orderToCancel, String> columnDateReceived;

	@FXML
	private TableView<orderToCancel> tableUsers;
	
	@FXML

	@SuppressWarnings("unchecked")
	public void initialize() {
		final ObservableList<orderToCancel> data = FXCollections.observableArrayList();

		// Column 1
		columnOrderID.setCellValueFactory(cellData -> cellData.getValue().getId());
		columnOrderID.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400px,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");

		// Column 2
		columnTotalPrice.setCellValueFactory(cellData -> cellData.getValue().getTotalPrice());
		columnTotalPrice.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		
		// Column 3
		columnDateReceived.setCellValueFactory(cellData -> cellData.getValue().getDate());
		columnDateReceived.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		
		// Column 4
		TableColumn<orderToCancel, Button> columnAccept = new TableColumn<>("Cancel");
		columnAccept.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		columnAccept.setPrefWidth(99);
		columnAccept.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		columnAccept.setCellFactory(col -> {
			Button cancel = new Button("CANCEL");
			cancel.setTextFill(Color.WHITE);
			cancel.getStylesheets().add("/gui/buttonCSS.css");
			TableCell<orderToCancel, Button> cell = new TableCell<orderToCancel, Button>() {
				@Override
				public void updateItem(Button item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(cancel);
					}
				}
			};
			cancel.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Cancel Order");
				alert.setHeaderText("This action will cancel the order!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						
						
						// Set order as cancelled
						orderToCancel order = cell.getTableView().getItems().get(cell.getIndex());
						tableUsers.getSelectionModel().clearSelection();
						SimpleStringProperty id = order.getId();
						data.remove(cell.getIndex());
						SCCP removeOrder = new SCCP();
						removeOrder.setRequestType(ServerClientRequestTypes.UPDATE);
						//Update order to cancelled
						removeOrder.setMessageSent(
								new Object[] { "orders", "statusId = 2", "orderID = " + id.getValue() });
						
						ClientUI.clientController.accept(removeOrder);
						txtOrderCancelled.setText("ORDER #" + id.getValue() + " CANCELLED");
						
					} else {
						return;
					}
				});
			});
			return cell;
		});


		SCCP ordersForCancellation = new SCCP();
		ordersForCancellation.setRequestType(ServerClientRequestTypes.SELECT);
		ordersForCancellation.setMessageSent(new Object[] { "orders "
				+ "JOIN machine ON orders.machineID = machine.machineId "
				+ "JOIN locations ON machine.locationId = locations.locationID", true,
				"orders.orderID, orders.total_price, orders.date_received", true,
				"locations.locationName = 'north' AND orders.statusId = 4;", false, null});

		ClientUI.clientController.accept(ordersForCancellation);

		ArrayList<?> arrayOfCancelledOrders = ((ArrayList<?>) ClientController.responseFromServer.getMessageSent());

		for (ArrayList<Object> order : (ArrayList<ArrayList<Object>>) arrayOfCancelledOrders) {
			int orderID = (int) order.get(0);
			double totalPrice = (double) order.get(1);
			Date date = (Date) order.get(2);
			data.add(new orderToCancel(orderID, totalPrice, date.toString()));

		}

		tableUsers.setItems(data);
		tableUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableUsers.getColumns().add(columnAccept);

	}

	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktRegionalManagerHomePage.fxml", null, "Ekt Regional Manager Home Page");
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
}
