package ek_configuration;

import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import entityControllers.OrderController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Role;

import java.io.Serializable;
import java.util.ArrayList;

import client.ClientController;
import client.ClientUI;

public class _EKConfigurationCustomerLocalOrderController implements Serializable {
	
	
	/**
	 * Advanced TODO after we're done: add support for variable categories.
	 * Implementation idea: Remove the buttons from fxml, leave just a pane with an
	 * HBox or something, and in initialize() do: query all categories from a
	 * dedicated table, run in a loop: i = 1 for category in categories:
	 * insertToHBoxInPane(category) if (i++) % 5 == 0 createNewHBox()
	 * moveToNextRow()
	 * 
	 * Something like that.
	 */
	private static final long serialVersionUID = 1L;

	@FXML
	private Button btnCatalog1;

	@FXML
	private Button btnCatalog2;

	@FXML
	private Button btnCatalog3;

	@FXML
	private Button btnCatalog4;

	@FXML
	private Button btnCatalog5;

	@FXML
	private Button btnCatalog6;

	@FXML
	private Button btnCatalog7;

	@FXML
	private Button btnCatalog8;

	@FXML
	private Button btnLogout;

	@FXML
	private ImageView imgClatalog0_0;

	@FXML
	private ImageView imgClatalog0_1;

	@FXML
	private ImageView imgClatalog0_2;

	@FXML
	private ImageView imgClatalog0_3;

	@FXML
	private ImageView imgClatalog1_0;

	@FXML
	private ImageView imgClatalog1_1;

	@FXML
	private ImageView imgClatalog1_2;

	@FXML
	private ImageView imgClatalog1_3;

	@FXML
	private Text txtWelcomeCustomer;
	
	@FXML
	private Text txtDiscountOnFirstOrder;
	
	
	String productFormFXMLLocation = "/gui/_EKConfigurationProductForm.fxml";

	
	@FXML
	public void initialize() {
		txtWelcomeCustomer.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back");
		txtWelcomeCustomer.setLayoutX(400 - (txtWelcomeCustomer.minWidth(0) / 2));
		if(_EKConfigurationLoginFrameController.firstOrderForSubscriber()) {
			txtDiscountOnFirstOrder.setVisible(true);
		}
	}

	// Category 1
	@FXML
	private void getBtnCatalog0_0(ActionEvent event) {
		loadCategoryPage(event,"HEALTHY");
	}

	// Category 2
	@FXML
	private void getBtnCatalog0_1(ActionEvent event) {
		loadCategoryPage(event, "SOFT DRINKS");
	}

	// Category 3
	@FXML
	private void getBtnCatalog0_2(ActionEvent event) {
		loadCategoryPage(event, "FRUITS");
	}

	// Category 4
	@FXML
	private void getBtnCatalog0_3(ActionEvent event) {
		loadCategoryPage(event, "VEGETABLES");
	}

	// Category 5
	@FXML
	private void getBtnCatalog1_0(ActionEvent event) {
		loadCategoryPage(event, "SNACKS");
	}

	// Category 6
	@FXML
	private void getBtnCatalog1_1(ActionEvent event) {
		loadCategoryPage(event, "SANDWICHES");
	}

	// Category 7
	@FXML
	private void getBtnCatalog1_2(ActionEvent event) {
		loadCategoryPage(event, "CHEWING GUM");
	}

	// Category 8
	@FXML
	private void getBtnCatalog1_3(ActionEvent event) {
		loadCategoryPage(event, "DAIRY");
	}

	private void loadCategoryPage(ActionEvent event, String category) {
		Stage primaryStage = new Stage();
		OrderController.getCurrentProductCategory().add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category, true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	
	@FXML
	void getBtnLogout(ActionEvent event) {
		// actually log the user out
		ClientController.sendLogoutRequest();

		// move to new window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationLoginFrame.fxml", null, "Login", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	@FXML
	void getBtnCatalogAllItems(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		String category = "ALL ITEMS";
		OrderController.getCurrentProductCategory().add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category, true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
	
	public void setDisableCatalog(boolean value) {
		btnCatalog1.setDisable(value);btnCatalog2.setDisable(value);
		btnCatalog3.setDisable(value);btnCatalog4.setDisable(value);
		btnCatalog5.setDisable(value);btnCatalog6.setDisable(value);
		btnCatalog7.setDisable(value);btnCatalog8.setDisable(value);
	}
	
}
