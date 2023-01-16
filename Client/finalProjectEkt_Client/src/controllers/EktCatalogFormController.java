package controllers;

import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
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

public class EktCatalogFormController implements Serializable {
	
	private static boolean bUserSwitchedConfigurations=true;

	
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
	
	@FXML
	private ComboBox<String> cmbMachineName;
	
	@FXML
	private Text txtDiscountFirstOrder;

	
	String productFormFXMLLocation = "/gui/EktProductForm.fxml";

	
	@FXML
	public void initialize() {
		if(EktSystemUserLoginController.firstOrderForSubscriber()) {
			txtDiscountFirstOrder.setVisible(true);
		}
		if(ClientController.OLCurrentMachineName == null)
			setDisableCatalog(true);
		if(ClientController.OLCurrentMachineName != null)
			cmbMachineName.setValue(ClientController.OLCurrentMachineName);
		
		txtWelcomeCustomer
				.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back!");
		txtWelcomeCustomer.setLayoutX(400 - (txtWelcomeCustomer.minWidth(0)) / 2);
		
		SCCP getMachines = new SCCP();
		getMachines.setRequestType(ServerClientRequestTypes.SELECT);
		getMachines.setMessageSent(new Object[] {"machine", true, "machineName, machineId", false, null, false, null});
		ClientUI.clientController.accept(getMachines);
		
		ArrayList<?> arrayOfMachines = new ArrayList<>();
		ArrayList<String> machinesNames = new ArrayList<String>();
		ArrayList<Integer> machinesIds = new ArrayList<Integer>();
		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			arrayOfMachines = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			for(Object machine : arrayOfMachines) {
				machinesNames.add((String) ((ArrayList<?>)machine).get(0));
				machinesIds.add((Integer) ((ArrayList<?>)machine).get(1));
			}
		}

		System.out.println("arrayOfMachines = " + arrayOfMachines);
		cmbMachineName.getItems().setAll(machinesNames);
		
		cmbMachineName.setOnAction(event ->{
			if(!cmbMachineName.getValue().equals(ClientController.OLCurrentMachineName)) {
				if(ClientController.OLCurrentMachineName != null) {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("WARNING");
					alert.setContentText("Changing a machine will clear your cart! Are you sure?");

					alert.showAndWait().ifPresent(type -> {
                        if (type == ButtonType.OK) {
                        	EktProductFormController.setMachineSwitchedFlag(true);
							ClientController.OLCurrentMachineName = cmbMachineName.getValue();
							bUserSwitchedConfigurations = true;
							System.out.println("SET MACHINE TO " + ClientController.OLCurrentMachineName);
                        } else if (type == ButtonType.NO) {
                        } else {
                        }
                });
					
				}
				else {
					// don't inform user first time
					EktProductFormController.setMachineSwitchedFlag(true);
					ClientController.OLCurrentMachineName = cmbMachineName.getValue();
					bUserSwitchedConfigurations = true;
				}

			}
			
			if(ClientController.OLCurrentMachineName != null) {
				// first, drop the flag!
				bUserSwitchedConfigurations = false;
				
				SCCP msg = new SCCP(ServerClientRequestTypes.SELECT, 
						new Object[]{"machine", true, "machineId", true,
								"machineName = '" +ClientController.OLCurrentMachineName+ "'", false, null});
				ClientUI.clientController.accept(msg);
				if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
					@SuppressWarnings("unchecked")
					ArrayList<ArrayList<Object>> tmp= (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
					System.out.println(tmp);
					ClientController.OLCurrentMachineID = (Integer.valueOf(tmp.get(0).get(0).toString()));
					System.out.println("Machine ID set to " + ClientController.OLCurrentMachineID);
				}
			}
			
			setDisableCatalog(false);
		;});
		
		
		
			
		
	}

	// Category 1
	@FXML
	private void getBtnCatalog0_0(ActionEvent event) {
		Stage primaryStage = new Stage();
		// category.text"Healthy";
		ClientController.CurrentProductCategory.add(0, "HEALTHY");
		// ClientController.CurrentProductCategory = changeSt;
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null,
				"Healthy", true);

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
				category, true);
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
				category, true);
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
				category, true);
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
				category, true);
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
				category, true);
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
				category, true);
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
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	@FXML
	void getBtnMyOrders(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktMyOrderFrom.fxml", null, "Ekt My Orders", true);
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
