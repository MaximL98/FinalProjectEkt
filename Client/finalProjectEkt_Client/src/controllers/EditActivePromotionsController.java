package controllers;

import java.awt.Color;
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
import javafx.scene.paint.Paint;
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

	private Promotions selectedPromotion;
	// Other UI elements and variables here...

	@FXML
	public void editPromotionHandler() {
		String selectedPromotionName = cbPromotionNames.getSelectionModel().getSelectedItem();
		//Retrieve and display the table of promotions with the same name as selected
		displayPromotionsTable();
	}

	private void displayPromotionsTable() {
		// Connect to the database and retrieve the promotion names
		// Return the retrieved promotion names in an ArrayList
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.SELECT);
		preparedMessage.setMessageSent(new Object[] { "promotions", true, 
				"promotionId, promotionName, locationName, discountPercentage, startDate, promotionStatus"
				, false, null, true, 
				"LEFT JOIN locations on locations.locationID;"});
		
		ClientUI.clientController.accept(preparedMessage);

//		@SuppressWarnings("unchecked")
//		ArrayList<Promotions> arrayFromDatabase = (ArrayList<Promotions>) ClientController.responseFromServer
//				.getMessageSent();
//		listView.clear();
//		for (Promotions promotion : arrayFromDatabase) {
//			listView.add(promotion);
//		}
//		listView.forEach(promotion -> System.out.println(promotion));
//		promotionTable.setEditable(true);
//		//promotionTable.setItems(listView);
//		promotionTable.getItems().setAll(arrayFromDatabase);
//
//		// if the response is not the type we expect, something went wrong with server
//		// communication and we throw an exception.
//		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.DISPLAY))) {
//			throw new RuntimeException("Error with server communication: Non expected request type");
//
//		}

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
		WindowStarter.createWindow(primaryStage, new Object(), "/gui/SalesManager.fxml", null, "Sales");
		primaryStage.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// Set the cell value factory for each TableColumn object
		promotionIDColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("promotionId"));
		promotionIDColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		promotionNameColumn.setCellValueFactory(new PropertyValueFactory<Promotions, String>("promotionName"));
		promotionNameColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		promotionDescriptionColumn
				.setCellValueFactory(new PropertyValueFactory<Promotions, String>("promotionDescription"));
		promotionDescriptionColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		locationColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("locationID"));
		locationColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		discountPercentageColumn
				.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("discountPercentage"));
		discountPercentageColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		startDateColumn.setCellValueFactory(new PropertyValueFactory<Promotions, LocalDate>("startDate"));
		startDateColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		endDateColumn.setCellValueFactory(new PropertyValueFactory<Promotions, LocalDate>("endDate"));
		endDateColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		promotionStatusColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Boolean>("promotionStatus"));
		promotionStatusColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		promotionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// Update the selectedOffer variable with the new selected Offer object
			selectedPromotion = newValue;
		});
		
		TableColumn<Promotions, Button> columnButton = new TableColumn<>("Activate");
		columnButton.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		columnButton.setPrefWidth(122);
		columnButton.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		columnButton.setCellFactory(col -> {
			Button setPromoStatus = new Button("ACTIVATE");
			setPromoStatus.setTextFill(Paint.valueOf("WHITE"));
			setPromoStatus.getStylesheets().add("/gui/buttonCSS.css");
			TableCell<Promotions, Button> cell = new TableCell<Promotions, Button>() {
				@Override
				public void updateItem(Button item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(setPromoStatus);
					}
				}
			};
			setPromoStatus.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Activate Promotion");
				alert.setHeaderText("This action will activate a new promotion!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						SCCP updateCustomerToNewCustomer = new SCCP();
						// send the updateCustomerToNewCustomer to the server
						if (setPromoStatus.getText().equals("ACTIVATE")) {
							setPromoStatus.setText("DEACTIVATE");
						} else {
							setPromoStatus.setText("ACTIVATE");
						}
						
					} else {
						return;
					}
				});
			});
			return cell;
		});

		// Connect to the database and retrieve the promotion names
		// Return the retrieved promotion names in an ArrayList
		displayPromotionsTable();

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.DISPLAY))) {
			throw new RuntimeException("Error with server communication: Non expected request type");

		}
		
		ArrayList<?> promotionNames = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		// System.out.println(promotionNames.toString());
		// Add the promotion names to the combo box
		for (ArrayList<Object> promotion: (ArrayList<ArrayList<Object>>) promotionNames) {
			
			
			
			
//			listView;
		}
		
		promotionTable.getColumns().add(columnButton);
	}

}
