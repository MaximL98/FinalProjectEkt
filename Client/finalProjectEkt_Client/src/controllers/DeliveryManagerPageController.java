package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Order;
import logic.Order.*;

public class DeliveryManagerPageController {

	private ArrayList<Order> orders;

	@FXML
	private Text welcomeText;

	@FXML
	private TableView<Order> deliveryTable;

	@FXML
	private TableColumn<Order, String> tblOrderNumberColumn;

	@FXML
	private TableColumn<Order, LocalDate> tblDateReceivedColumn;

	@FXML
	private TableColumn<Order, LocalDateTime> tblTimeColumn;

	@FXML
	private TableColumn<Order, Order.Status> tblStatusColumn;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnUpdate;

	// Rotem 1.13 modified this to actually log-out (it used to go to product catalog)
	@FXML
	void getBtnBack(ActionEvent event) {
		ClientController.sendLogoutRequest();
    	// move to new window
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);

		primaryStage.show();	
	}

	@FXML
	void getBtnUpdate(ActionEvent event) {
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.UPDATE_ONLINE_ORDERS);
		preparedMessage.setMessageSent(new Object[] { orders.toArray() });

		// send to server
		System.out.println("Client: Sending online orders update request to server.");
		ClientUI.clientController.accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType()
				.equals(ServerClientRequestTypes.ACK))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		}
		else {
			Alert successMessage = new Alert(AlertType.INFORMATION);
			successMessage.setTitle("Update Success");
			successMessage.setHeaderText("Update Success");
			successMessage.setContentText("Orders updated successfully!");
			successMessage.show();
			// remove orders that left in progress status
			for(Order order : orders) {
				if(order.getStatus() != Order.Status.InProgress)
					deliveryTable.getItems().remove(order);
			}
		}
	}

	/*
	 * public static void main(String[] args) { launch(args); }
	 * 
	 * @Override public void start(Stage primaryStage) throws Exception {
	 * WindowStarter.createWindow(primaryStage, this,
	 * "/gui/DeliveryManagerPage.fxml", "/gui/DeliveryManager.css",
	 * "Delivery Management"); primaryStage.show(); }
	 */

	@FXML
	public void initialize() {
		orders = getOrders();
		// maybe show popup that says no orders?
		if (orders == null)
			return;
		tblOrderNumberColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
		tblDateReceivedColumn.setCellValueFactory(new PropertyValueFactory<Order, LocalDate>("dateReceived"));
		tblTimeColumn.setCellValueFactory(new PropertyValueFactory<Order, LocalDateTime>("deliveryTime"));
		tblStatusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Order.Status>(cellData.getValue().getStatus()));
		tblStatusColumn.setCellFactory(col -> {
			ComboBox<Order.Status> combo = new ComboBox<>();
			combo.getItems().addAll(Order.Status.values());
			TableCell<Order, Order.Status> cell = new TableCell<Order, Order.Status>() {
				@Override
				protected void updateItem(Order.Status status, boolean empty) {
					super.updateItem(status, empty);
					if (empty) {
						setGraphic(null);
					} else {
						combo.setValue(status);
						setGraphic(combo);
					}
				}
			};
			combo.setOnAction(e -> deliveryTable.getItems().get(cell.getIndex()).setStatus(combo.getValue()));
			return cell;
		});

		deliveryTable.setItems(FXCollections.observableArrayList(orders));

	}

	private ArrayList<Order> getOrders() {
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.FETCH_ORDERS);
		preparedMessage.setMessageSent(new int[] { Status.InProgress.getStatusId() });

		// send to server
		System.out.println("Client: Sending online orders fetch request to server.");
		ClientUI.clientController.accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType()
				.equals(ServerClientRequestTypes.FETCH_ORDERS))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		}
		// otherwise we create an Order arrayList and add the items from response
		// to it.
		Object response = ClientController.responseFromServer.getMessageSent();
		ArrayList<?> responseArr = (ArrayList<?>) response;
		if (responseArr.size() == 0)
			return null;
		// return new arrayList with the items from response casted to Order.
		return orders = responseArr.stream().map(x -> (Order) x).collect(Collectors.toCollection(ArrayList::new));
	}

	// for testing
	/*
	 * public ObservableList<Order> getOrders() { ObservableList<Order>
	 * orders = FXCollections.observableArrayList(); orders.add(new Order("1",
	 * 5, "test", "Somewhere", LocalDate.of(2022, Month.DECEMBER, 25),
	 * LocalDateTime.of(LocalDate.of(2023, Month.JANUARY, 15), LocalTime.of(12, 0)),
	 * Type.Delivery, Status.Complete)); orders.add(new Order("2", 2, "test2",
	 * "Somewhere", LocalDate.of(2022, Month.DECEMBER, 22),
	 * LocalDateTime.of(LocalDate.of(2023, Month.JANUARY, 10), LocalTime.of(10, 0)),
	 * Type.Pickup, Status.Cancelled)); return orders; }
	 */

}
