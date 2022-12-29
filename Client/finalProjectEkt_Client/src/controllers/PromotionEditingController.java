package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import logic.Promotions;
import javafx.fxml.Initializable;

public class PromotionEditingController implements Initializable {
	@FXML
	private TextField txtPromotionName;

	private Promotions promotions;

	@FXML
	private TextField txtPromotionDescription;

	@FXML
	private ComboBox<String> cbLocation;

	@FXML
	private TextField txtProductId;

	@FXML
	private TextField txtDiscountPercentage;

	@FXML
	private DatePicker dpPromotionStartDate;

	@FXML
	private DatePicker dpPromotionEndDate;

	@FXML
	private Button btnCreatePromotion;

	@FXML
	private Button btnGoBack;

	@FXML
	private void createPromotionHandler() {
		// Get the promotion details from the text fields and date pickers
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.ADD_PROMOTION);
		Object[] fillMessage = new Object[3];
		promotions.setDiscountPercentage(txtDiscountPercentage.getText());
		//....
		fillMessage[0] =  "promotions";
		fillMessage[1] = false;
		fillMessage[2] = new Object[] {promotions};
		
		preparedMessage.setMessageSent(fillMessage);

		// send to server
		System.out.println("Client: Sending new promotion request to the server.");
		ClientUI.clientController.accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ADD_PROMOTION))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		} else {
			Alert successMessage = new Alert(AlertType.INFORMATION);
			successMessage.setTitle("Update Success");
			successMessage.setHeaderText("Update Success");
			successMessage.setContentText("Promotion created successfully!");
			successMessage.show();
		}
	}

	public void goBackHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), "/gui/SalesManager.fxml", null, "Sales");
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String[] items = { "North", "South", "United Arab Emirates" };
		cbLocation.getItems().addAll(items);
	}
}
