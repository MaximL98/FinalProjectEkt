package controllers;

import java.util.ArrayList;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.record.Record;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import controllers.EktReportDisplayPageController.productInTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EktRegionalManagerAcceptNewCustomerController {

	public class customerToAccept {
		private SimpleStringProperty name;
		private SimpleStringProperty userType;
		private SimpleStringProperty id;
		private SimpleStringProperty phoneNumber;
		Button accept = new Button();

		public Button getAccept() {
			return accept;
		}

		public void setAccept(Button accept) {
			this.accept = accept;
		}

		public Button getDecline() {
			return decline;
		}

		public void setDecline(Button decline) {
			this.decline = decline;
		}

		Button decline = new Button();

		public customerToAccept(String name, String userType, String id, String phoneNumber) {
			this.name = new SimpleStringProperty(name);
			this.userType = new SimpleStringProperty(userType);
			this.id = new SimpleStringProperty(id);
			this.phoneNumber = new SimpleStringProperty(phoneNumber);
			// accept.setGraphic(new ImageView(new Image("controllers/Images/v.png")));
			accept.setOnAction(ae -> {

				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Accept Customer");
				alert.setHeaderText("This action will create a new customer!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						SCCP updateCustomerToNewCustomer = new SCCP();
						updateCustomerToNewCustomer.setRequestType(ServerClientRequestTypes.UPDATE);
						updateCustomerToNewCustomer.setMessageSent(
								new Object[] { "system_user", "typeOfUser = \"" + userType + "\"", "id = " + id });

					} else {
						return;
					}
				});
			});
			// decline.setGraphic(new ImageView(new Image("controllers/Images/x.png")));
			decline.setOnAction(ae -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Decline Customer");
				alert.setHeaderText("This action will remove the customer request!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						SCCP updateCustomerToNewCustomer = new SCCP();
						updateCustomerToNewCustomer.setRequestType(ServerClientRequestTypes.UPDATE);
						updateCustomerToNewCustomer.setMessageSent(
								new Object[] { "system_user", "typeOfUser = \"" + userType + "\"", "id = " + id });
					} else {
						return;
					}
				});
			});
		}

		public SimpleStringProperty getUserType() {
			return userType;
		}

		public void setuserType(SimpleStringProperty lastName) {
			this.userType = lastName;
		}

		public SimpleStringProperty getName() {
			return name;
		}

		public void setName(SimpleStringProperty firstName) {
			this.name = firstName;
		}

		public SimpleStringProperty getId() {
			return id;
		}

		public void setId(SimpleStringProperty id) {
			this.id = id;
		}

		public SimpleStringProperty getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(SimpleStringProperty phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

	}

	@FXML
	private Button btnBack;

	@FXML
	private Button btnUpdate;

	@FXML
	private TableColumn<customerToAccept, String> columnID;

	@FXML
	private TableColumn<customerToAccept, String> columnName;

	@FXML
	private TableColumn<customerToAccept, String> columnUserType;

	@FXML
	private TableColumn<customerToAccept, String> columnPhoneNumber;

	@FXML
	private TableView<customerToAccept> tableUsers;
	
	@FXML

	@SuppressWarnings("unchecked")
	public void initialize() {
		final ObservableList<customerToAccept> data = FXCollections.observableArrayList();

		// Column 1
		columnName.setCellValueFactory(cellData -> cellData.getValue().getName());

		// Column 2
		columnUserType.setCellValueFactory(cellData -> cellData.getValue().getUserType());

		// Column 3
		columnID.setCellValueFactory(cellData -> cellData.getValue().getId());

		// Column 4
		columnPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumber());
		
		// Column 5
		TableColumn<customerToAccept, Button> columnAccept = new TableColumn<>("Accept");
		columnAccept.setPrefWidth(64);
		columnAccept.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		columnAccept.setCellFactory(col -> {
			Button accept = new Button("Accept");
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
				alert.setTitle("Accept Customer");
				alert.setHeaderText("This action will create a new customer!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						customerToAccept customer = cell.getTableView().getItems().get(cell.getIndex());
						SimpleStringProperty userType = customer.getUserType();
						tableUsers.getSelectionModel().clearSelection();
						SimpleStringProperty id = customer.getId();
						data.remove(cell.getIndex());
						SCCP updateCustomerToNewCustomer = new SCCP();
						updateCustomerToNewCustomer.setRequestType(ServerClientRequestTypes.UPDATE);
						if (customer.getUserType().getValue().equals("unapproved_customer")) {
							updateCustomerToNewCustomer.setMessageSent(
									new Object[] { "systemuser", "typeOfUser = \"customer\"", "id = " + id.getValue() });
						} else {
							updateCustomerToNewCustomer.setMessageSent(
									new Object[] { "systemuser", "typeOfUser = \"subscriber\"", "id = " + id.getValue() });
						}
						
						ClientUI.clientController.accept(updateCustomerToNewCustomer);
						// send the updateCustomerToNewCustomer to the server
					} else {
						return;
					}
				});
//				customerToAccept customer = cell.getTableView().getItems().get(cell.getIndex());
//				// call a function that updates the database, instead of data.remove(customer)
			});
			return cell;
		});

		// Column 6
		TableColumn<customerToAccept, Button> columnDecline = new TableColumn<>("Decline");
		columnDecline.setPrefWidth(64);
		columnDecline.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		columnDecline.setCellFactory(col -> {
			Button decline = new Button("Decline");
			TableCell<customerToAccept, Button> cell = new TableCell<customerToAccept, Button>() {
				@Override
				public void updateItem(Button item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(decline);
					}
				}
			};
			decline.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Decline Customer");
				alert.setHeaderText("This action will delete this customer!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						customerToAccept customer = cell.getTableView().getItems().get(cell.getIndex());
						SimpleStringProperty userType = customer.getUserType();
						tableUsers.getSelectionModel().clearSelection();
						SimpleStringProperty id = customer.getId();
						data.remove(cell.getIndex());
						SCCP updateCustomerToNewCustomer = new SCCP();
						updateCustomerToNewCustomer.setRequestType(ServerClientRequestTypes.REMOVE);
						updateCustomerToNewCustomer.setMessageSent(
								new Object[] { "systemuser", "", "id = " + id.getValue() });
						ClientUI.clientController.accept(updateCustomerToNewCustomer);
						// send the updateCustomerToNewCustomer to the server
					} else {
						return;
					}
				});
			});
			return cell;
		});

		SCCP unapprovedCustomers = new SCCP();
		unapprovedCustomers.setRequestType(ServerClientRequestTypes.SELECT);
		unapprovedCustomers.setMessageSent(new Object[] { "systemuser", true,
				"firstName, lastName, typeOfUser, id, phoneNumber", true,
				"typeOfUser = \"unapproved_customer\" OR typeOfUser = \"unapproved_subscriber\";", false, null });

		ClientUI.clientController.accept(unapprovedCustomers);

		ArrayList<?> arrayOfUnapprovedCustomers = ((ArrayList<?>) ClientController.responseFromServer.getMessageSent());

		for (ArrayList<Object> customer : (ArrayList<ArrayList<Object>>) arrayOfUnapprovedCustomers) {
			String name = customer.get(0) + " " + customer.get(1);
			String userType = (String) customer.get(2);
			String id = customer.get(3) + "";
			String phoneNumber = (String) customer.get(4);
			data.add(new customerToAccept(name, userType, id, phoneNumber));

		}

		tableUsers.setItems(data);
		tableUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableUsers.getColumns().addAll(columnAccept, columnDecline);

	}

	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktRegionalManagerHomePage.fxml", null, "Ekt Regional Manager Home Page");
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	@FXML
	void getBtnUpdate(ActionEvent event) {

	}

}
