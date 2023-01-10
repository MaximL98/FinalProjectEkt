package controllers;

import java.sql.Date;
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

/**
 * Yeah I'm not the author here, but I am here:
 * We don't need the object "promotion", we need to show a table to the user and let them select products, and allow it to enter different percentages 
 * FOR EACH ONE.
 * Now after selecting all that they can enter a discount name and description (optional),
 * and the DB gets the following query:
 * for product in chosenProducts:
 * 	insert into table ProductInDiscount (null, *product, start, end, percentage)
 * this is where the addMany shit I did makes sense. 
 * The way it should work is this:
 * 
 * Show a table with all products, the original price, and the final price (after applying all discounts, according to some agreed-upon order)
 *  This should be done in initialize() after modifying the DB (again . . .) -->^ 
 * Let user select each row in the table, when selected (by right click) - open a new column with a percentage for the new discount (or new price)
 * When finished, grab text from text boxes "promotion name" "description" and send these, along each product, to be added to the ProductInDiscount table
 * Key is auto incremented, we never remove shit from there (as when grabbing discounts we should only use ones inside the date range)
 * 
 * 
 * TLDR: I didn't mess with this file any yet, but I suggest we change it
 * 
 * Never mind, I just did (not really important):
 * I fixed the initialize() call; 
 * jfx has a specific shinitialize function, without the bloat and the unneeded import, we use it elsewhere so I replaced it here.
 * 
 * @author Rotem
 *
 */

public class PromotionEditingController {
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
	private void initialize() {
		String[] items = { "North", "South", "United Arab Emirates" };
		cbLocation.getItems().addAll(items);
	}
	
	@FXML
	private void createPromotionHandler() {
		promotions = new Promotions();
		//ServerClientRequestTypes.ADD
		// Get the promotion details from the text fields and date pickers
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
		Object[] fillMessage = new Object[3];
		promotions.setDiscountPercentage(txtDiscountPercentage.getText());
		promotions.setPromotionName(txtPromotionName.getText());
		promotions.setPromotionDescription(txtPromotionDescription.getText());
		promotions.setstoreLocation(cbLocation.getSelectionModel().getSelectedItem());
		int locationNumber;
		switch (cbLocation.getSelectionModel().getSelectedItem()) {
		    case "North":
		        locationNumber =1;
		        break;
		    case "South":
		        locationNumber = 2;
		        break;
		    case "United Arab Emirates":
		        locationNumber = 3;
		        break;
		    default:
		        locationNumber = 0;
		        break;
		}
		promotions.setLocationID(locationNumber);
		promotions.setproductID(txtProductId.getText());
		String startDateString = dpPromotionStartDate.getValue().toString();
		promotions.setStartDate(Date.valueOf(startDateString));
		String endDateString = dpPromotionEndDate.getValue().toString();
		promotions.setEndDate(Date.valueOf(endDateString));


		fillMessage[0] =  "promotions";
		fillMessage[1] = false;
		fillMessage[2] = new Object[] {promotions};
		
		preparedMessage.setMessageSent(fillMessage);

		// send to server
		System.out.println("Client: Sending new promotion request to the server.");
		ClientUI.clientController.accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ADD))) {
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
		Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	    currentStage.close();
	    
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), "/gui/SalesManager.fxml", null, "Sales");
		primaryStage.show();
	}

}
