package controllers;

import common.WindowStarter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.ArrayList;

import client.ClientController;

public class EktCatalogFormController implements Serializable {
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
	private Button btnMyOrders;

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

	String productFormFXMLLocation = "/gui/EktProductForm.fxml";

	@FXML
	public void initialize() {
		txtWelcomeCustomer
				.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back!");
		txtWelcomeCustomer.setLayoutX(400 - (txtWelcomeCustomer.minWidth(0)) / 2);
	}

	// Category 1
	@FXML
	private void getBtnCatalog0_0(ActionEvent event) {
		Stage primaryStage = new Stage();
		// category.text"Healthy";
		ClientController.CurrentProductCategory.add(0, "HEALTHY");
		// ClientController.CurrentProductCategory = changeSt;
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				"Healthy");

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	// Category 2
	@FXML
	private void getBtnCatalog0_1(ActionEvent event) {
		Stage primaryStage = new Stage();
		String category = "SOFT DRINKS";
		ClientController.CurrentProductCategory.add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	// Category 3
	@FXML
	private void getBtnCatalog0_2(ActionEvent event) {
		Stage primaryStage = new Stage();
		String category = "FRUITS";
		ClientController.CurrentProductCategory.add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	// Category 4
	@FXML
	private void getBtnCatalog0_3(ActionEvent event) {
		Stage primaryStage = new Stage();
		String category = "VEGETABLES";
		ClientController.CurrentProductCategory.add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	// Category 5
	@FXML
	private void getBtnCatalog1_0(ActionEvent event) {
		Stage primaryStage = new Stage();
		String category = "SNACKS";
		ClientController.CurrentProductCategory.add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	// Category 6
	@FXML
	private void getBtnCatalog1_1(ActionEvent event) {
		Stage primaryStage = new Stage();
		String category = "SANDWICHES";
		ClientController.CurrentProductCategory.add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	// Category 7
	@FXML
	private void getBtnCatalog1_2(ActionEvent event) {
		Stage primaryStage = new Stage();
		String category = "CHEWING GUM";
		ClientController.CurrentProductCategory.add(0, category);
		System.out.println(ClientController.getCurrentSystemUser().getFirstName());
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	// Category 8
	@FXML
	private void getBtnCatalog1_3(ActionEvent event) {
		Stage primaryStage = new Stage();
		String category = "DAIRY";
		ClientController.CurrentProductCategory.add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	@FXML
	void getBtnLogout(ActionEvent event) {
		// actually log the user out
		ClientController.sendLogoutRequest();

		// move to new window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login");

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	@FXML
	void getBtnMyOrders(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktMyOrderFrom.fxml", null, "Ekt My Orders");
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	@FXML
	void getBtnCatalogAllItems(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		String category = "ALL ITEMS";
		ClientController.CurrentProductCategory.add(0, category);
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				category);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
}
