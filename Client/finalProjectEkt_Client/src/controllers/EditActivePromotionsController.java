package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import controllers.EktRegionalManagerAcceptNewCustomerController.customerToAccept;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import logic.Promotions;

public class EditActivePromotionsController implements Initializable {
	@FXML
	VBox vboxTest;
	@FXML
	private ComboBox<String> cbPromotionNames = null;
	@FXML
	private TableView<Promotions> promotionTable;
	@FXML
	private TableColumn<Promotions, Integer> promotionIDColumn;
	@FXML
	private TableColumn<Promotions, String> promotionNameColumn;
	@FXML
	private TableColumn<Promotions, String> promotionDescriptionColumn;
	//@FXML
	//private TableColumn<Promotions, String> productIdColumn;
	@FXML
	private TableColumn<Promotions, Integer> locationColumn;
	@FXML
	private TableColumn<Promotions, Integer> discountPercentageColumn;
	@FXML
	private TableColumn<Promotions, LocalDate> startDateColumn;
	@FXML
	private TableColumn<Promotions, LocalDate> endDateColumn;
	@FXML
	private TableColumn<Promotions, Boolean> promotionStatusColumn;
	private ObservableList<Promotions> listView = FXCollections.observableArrayList();
	private ArrayList<String> promotionNames;
	private Promotions promotions;
	private Promotions selectedPromotion;
	// Other UI elements and variables here...

	@FXML
	public void editPromotionHandler() {
		String selectedPromotionName = cbPromotionNames.getSelectionModel().getSelectedItem();
//	     retrieve and display the table of promotions with the same name as selected
		displayPromotionsTable(selectedPromotionName);
	}

	private void displayPromotionsTable(String selectedPromotionName) {
		// Connect to the database and retrieve the promotion names
		// Return the retrieved promotion names in an ArrayList
		promotions = new Promotions();
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.DISPLAY_SELECTED_PROMOTIONS);
		preparedMessage.setMessageSent(selectedPromotionName);
		// send to servers
		System.out.println("Client: Sending excisiting promotion request to the server.");
		ClientUI.clientController.accept(preparedMessage);

		@SuppressWarnings("unchecked")
		ArrayList<Promotions> arrayFromDatabase = (ArrayList<Promotions>) ClientController.responseFromServer
				.getMessageSent();
		listView.clear();
		for (Promotions promotion : arrayFromDatabase) {
			listView.add(promotion);
		}
		listView.forEach(promotion -> System.out.println(promotion));
		promotionTable.setEditable(true);
		//promotionTable.setItems(listView);
		promotionTable.getItems().setAll(arrayFromDatabase);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.DISPLAY))) {
			throw new RuntimeException("Error with server communication: Non expected request type");

		}

	}

	@FXML
	public void saveChangesHandler() {
		// Update the promotion data in the database with the current values in the UI
		// elements
	}

	public void goBackHandler(ActionEvent event) {
		cbPromotionNames.getItems().clear();
		Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
		currentStage.close();

		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), "/gui/SalesManager.fxml", null, "Sales", true);
		primaryStage.show();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// Set the cell value factory for each TableColumn object
		promotionIDColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("promotionId"));
		promotionNameColumn.setCellValueFactory(new PropertyValueFactory<Promotions, String>("promotionName"));
		promotionDescriptionColumn
				.setCellValueFactory(new PropertyValueFactory<Promotions, String>("promotionDescription"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("locationID"));
		//productIdColumn.setCellValueFactory(new PropertyValueFactory<Promotions, String>("productID"));

		//productIdColumn.setCellValueFactory(new PropertyValueFactory<Promotions, String>("productID"));
		discountPercentageColumn
				.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("discountPercentage"));
		startDateColumn.setCellValueFactory(new PropertyValueFactory<Promotions, LocalDate>("startDate"));
		endDateColumn.setCellValueFactory(new PropertyValueFactory<Promotions, LocalDate>("endDate"));
		promotionStatusColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Boolean>("promotionStatus"));
		promotionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// Update the selectedOffer variable with the new selected Offer object
			selectedPromotion = newValue;
		});
		
		TableColumn<customerToAccept, Button> columnAccept = new TableColumn<>("Activate");
		columnAccept.setPrefWidth(65);
		columnAccept.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		columnAccept.setCellFactory(col -> {
			Button accept = new Button("Activate");
			TableCell<customerToAccept, Button> cell = new TableCell<customerToAccept, Button>() {
				@Override
				public void updateItem(Button item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(accept);
					}
				}
			};
			accept.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Activate Promotion");
				alert.setHeaderText("This action will Activate A New Promotion!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						customerToAccept customer = cell.getTableView().getItems().get(cell.getIndex());
						SimpleStringProperty userType = customer.getUserType();
						SimpleStringProperty id = customer.getId();

						SCCP updateCustomerToNewCustomer = new SCCP();
						updateCustomerToNewCustomer.setRequestType(ServerClientRequestTypes.UPDATE);
						updateCustomerToNewCustomer.setMessageSent(
								new Object[] { "system_user", "typeOfUser = \"" + userType + "\"", "id = " + id });
						// send the updateCustomerToNewCustomer to the server

					} else {
						return;
					}
				});
				customerToAccept customer = cell.getTableView().getItems().get(cell.getIndex());
				// call a function that updates the database, instead of data.remove(customer)
			});
			return cell;
		});

		// Connect to the database and retrieve the promotion names
		// Return the retrieved promotion names in an ArrayList
		promotionNames = new ArrayList<>();
		promotions = new Promotions();
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.DISPLAY_PROMOTIONS);

		Object[] fillMessage = new Object[3];
		fillMessage[0] = "promotions";
		fillMessage[1] = false;
		fillMessage[2] = null;
		preparedMessage.setMessageSent(fillMessage);

		// send to servers
		System.out.println("Client: Sending excisiting promotion request to the server.");
		ClientUI.clientController.accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.DISPLAY))) {
			throw new RuntimeException("Error with server communication: Non expected request type");

		}
		promotionNames = (ArrayList<String>) ClientController.responseFromServer.getMessageSent();
		// System.out.println(promotionNames.toString());
		// Add the promotion names to the combo box
		cbPromotionNames.setItems(FXCollections.observableArrayList(promotionNames));
	}

}
