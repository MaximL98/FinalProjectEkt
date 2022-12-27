package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import logic.OnlineOrder;
import logic.SystemUser;
import logic.OnlineOrder.*;

public class DeliveryManagerPageController {

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
	private TableColumn<OnlineOrder, Status> tblStatusColumn;

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

	/*public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		WindowStarter.createWindow(primaryStage, this, "/gui/DeliveryManagerPage.fxml", "/gui/DeliveryManager.css",
				"Delivery Management");
		primaryStage.show();
	}*/

	@FXML
	public void initialize() {
		ObservableList<OnlineOrder> orders = getOnlineOrders();

		tblOrderNumberColumn.setCellValueFactory(new PropertyValueFactory<OnlineOrder, String>("orderID"));
		tblDateReceivedColumn.setCellValueFactory(new PropertyValueFactory<OnlineOrder, LocalDate>("dateReceived"));
		tblTimeColumn.setCellValueFactory(new PropertyValueFactory<OnlineOrder, LocalDateTime>("deliveryTime"));
		// tblStatusColumn.setCellValueFactory(new PropertyValueFactory<OnlineOrder,
		// ComboBox<String>>("statusCombo"));
		/*
		 * Status[] statusArr = Status.values(); ObservableList<String> statuses =
		 * FXCollections.observableArrayList(); for (Status s : statusArr) {
		 * statuses.add(s.toString()); }
		 */
		tblStatusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Status>(cellData.getValue().getStatus()));
		tblStatusColumn.setCellFactory(col -> {
			ComboBox<Status> combo = new ComboBox<>();
			combo.getItems().addAll(Status.values());
			TableCell<OnlineOrder, Status> cell = new TableCell<OnlineOrder, Status>() {
				@Override
				protected void updateItem(Status status, boolean empty) {
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
		// this.statusCombo = new
		// ComboBox<>(FXCollections.observableArrayList(Status.InProgress.toString(),
		// Status.Cancelled.toString(), Status.Complete.toString()));
		// this.statusCombo.setValue(status.name());

	}

	private ObservableList<OnlineOrder> getOnlineOrders() {
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.FETCH_ONLINE_ORDERS);
		preparedMessage.setMessageSent(new String[] { Status.InProgress.name() });

		// send to server
		System.out.println("Client: Sending online orders fetch request to server.");
		ClientUI.clientController.accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType()
				.equals(ServerClientRequestTypes.FETCH_ONLINE_ORDERS))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		}
		// otherwise we create an OnlineOrder arrayList and add the items from response
		// to it.
		Object response = ClientController.responseFromServer.getMessageSent();
		ArrayList<?> responseArr = (ArrayList<?>) response;
		if (responseArr.size() == 0)
			return null;
		// return new arrayList with the items from response casted to OnlineOrder.
		ArrayList<OnlineOrder> orders = responseArr.stream().map(x -> (OnlineOrder) x).collect(Collectors.toCollection(ArrayList::new));
		return FXCollections.observableArrayList(orders);
	}

	public ObservableList<OnlineOrder> getOrders() {
		ObservableList<OnlineOrder> orders = FXCollections.observableArrayList();
		orders.add(new OnlineOrder("1", 5, "test", "Somewhere", LocalDate.of(2022, Month.DECEMBER, 25),
				LocalDateTime.of(LocalDate.of(2023, Month.JANUARY, 15), LocalTime.of(12, 0)), Type.Delivery,
				Status.Complete));
		orders.add(new OnlineOrder("2", 2, "test2", "Somewhere", LocalDate.of(2022, Month.DECEMBER, 22),
				LocalDateTime.of(LocalDate.of(2023, Month.JANUARY, 10), LocalTime.of(10, 0)), Type.Pickup,
				Status.Cancelled));
		return orders;
	}

}
