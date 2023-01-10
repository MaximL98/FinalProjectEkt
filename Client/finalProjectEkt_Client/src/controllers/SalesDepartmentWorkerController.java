package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.Promotions;

public class SalesDepartmentWorkerController implements Initializable {

	@FXML
	private Button btnActivePromotion;
	@FXML
	private Button btnLogout;
	@FXML
	private TableView<Promotions> promotionTable;
	@FXML
	private TableColumn<Promotions, Integer> promotionIDColumn;
	@FXML
	private TableColumn<Promotions, String> promotionNameColumn;
	@FXML
	private TableColumn<Promotions, String> promotionDescriptionColumn;
	@FXML
	private TableColumn<Promotions, String> productIDColumn;
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//promotionId, promotionName, promotionDescription, locationId, productID, discountPercentage, startDate, endDate, promotionStatus
		// Set the cell value factory for each TableColumn object
		promotionIDColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("promotionId"));
		promotionNameColumn.setCellValueFactory(new PropertyValueFactory<Promotions, String>("promotionName"));
		promotionDescriptionColumn
				.setCellValueFactory(new PropertyValueFactory<Promotions, String>("promotionDescription"));
		//locationColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("locationID"));
		productIDColumn.setCellValueFactory(new PropertyValueFactory<Promotions, String>("productID"));
		discountPercentageColumn
				.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("discountPercentage"));
		startDateColumn.setCellValueFactory(new PropertyValueFactory<Promotions, LocalDate>("startDate"));
		endDateColumn.setCellValueFactory(new PropertyValueFactory<Promotions, LocalDate>("endDate"));
		promotionStatusColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Boolean>("promotionStatus"));
		promotionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// Update the selectedOffer variable with the new selected Offer object
			selectedPromotion = newValue;
		});

		// Connect to the database and retrieve the promotion names
		// Return the retrieved promotion names in an ArrayList
		promotionNames = new ArrayList<>();
		promotions = new Promotions();
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.DISPLAY_PROMOTIONS_TO_ACTIVE);
		
		Integer idUser = ClientController.getCurrentSystemUser().getId();
		preparedMessage.setMessageSent(idUser);

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
		promotionNames = (ArrayList<String>) ClientController.responseFromServer.getMessageSent();

	}

	@FXML
	private void ActivePromotionHandler(ActionEvent event) {
		
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.UPDATE_PROMOTION_STATUS);
		String selectedPid = promotionTable.getSelectionModel().getSelectedItem().getPromotionId();
		preparedMessage.setMessageSent(selectedPid);

		// send to servers
		System.out.println("Client: Sending excisiting promotion request to the server.");
		ClientUI.clientController.accept(preparedMessage);
		
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.UPDATE_PROMOTION_STATUS)))
			throw new RuntimeException("Error with server communication: Non expected request type");	
		
		updateTablePro();
	}
	
	public void updateTablePro() {
		
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.DISPLAY_PROMOTIONS_TO_ACTIVE);
		Integer idUser = ClientController.getCurrentSystemUser().getId();
		preparedMessage.setMessageSent(idUser);
		
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
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.DISPLAY)))
			throw new RuntimeException("Error with server communication: Non expected request type");	
	}

	@FXML
	private void logoutHandler(ActionEvent event) throws Exception {
		System.out.println("Sales Worker Has Exited The Academic Tool");
		ClientUI.clientController.client.closeConnection();
		System.exit(0);
	}

}
