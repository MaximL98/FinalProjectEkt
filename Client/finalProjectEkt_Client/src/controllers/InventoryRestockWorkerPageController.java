/**
 * ROTEM:
 * I had to comment it all out - it causes errors as the UPDATE_PRODUCTS_IN_MACHINE for example is defined here but does not exist in
 * ServerMessageHandler (there are other examples).
 *  
 */

package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Location;
import logic.Machine;
import logic.Order;
import logic.Product;
import logic.ProductInMachine;
import logic.Order.Status;

public class InventoryRestockWorkerPageController {

	public class InventoryTableData {
		public InventoryTableData(ProductInMachine item) {
			this.item = item;
			setItemName(item.getProduct().getProductName());
			setItemStock(item.getStock());
			setStockToAdd(0);
		}

		private final ProductInMachine item;

		public final ProductInMachine getItem() {
			return item;
		}

		private final StringProperty itemName = new SimpleStringProperty(this, "itemName");

		public final String getItemName() {
			return itemName.get();
		}

		public final void setItemName(String itemName) {
			this.itemName.set(itemName);
		}

		public final StringProperty itemNameProperty() {
			return itemName;
		}

		private final IntegerProperty itemStock = new SimpleIntegerProperty(this, "itemStock");

		public final int getItemStock() {
			return itemStock.get();
		}

		public final void setItemStock(int itemStock) {
			this.item.setStock(itemStock);
			this.itemStock.set(itemStock);
		}

		public final IntegerProperty itemStockProperty() {
			return itemStock;
		}

		private final StringProperty itemStockToAdd = new SimpleStringProperty(this, "itemStockToAdd");;

		public final StringProperty itemStockToAddProperty() {
			return itemStockToAdd;
		}

		public final int getItemStockToAdd() {
			return Integer.parseInt(itemStockToAdd.get());
		}

		public final void setStockToAdd(int stockToAdd) {
			itemStockToAdd.set(String.valueOf(stockToAdd));
		}

	}

	private HashMap<Machine, ArrayList<InventoryTableData>> machineDataMap = new HashMap<>();
	@FXML
	private Text WelcomeInventoryWorkerText;

	@FXML
	private TableView<InventoryTableData> tblInventory;

	@FXML
	private TableColumn<InventoryTableData, String> colItem;

	@FXML
	private TableColumn<InventoryTableData, Number> colQuantity;

	@FXML
	private TableColumn<InventoryTableData, String> colRestockAmount;

	@FXML
	private ComboBox<Machine> cmbChooseMachine;

	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnBack;

	/*
	 * public static void main(String[] args) { launch(args); }
	 * 
	 * @Override public void start(Stage primaryStage) throws Exception {
	 * WindowStarter.createWindow(primaryStage, this,
	 * "/gui/InventoryRestockWorkerPage.fxml", "/gui/InventoryRestock.css",
	 * "Delivery Management"); primaryStage.show(); }
	 */

	@FXML
	void getBtnBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktLogisticsManagerHomePage.fxml", null,
				"Logistics Manager Home Page", true);
		// this was done so that we can use this button
		primaryStage.setOnCloseRequest(we -> {
			System.out.println("Pressed the X button.");
			System.exit(0);
		});

		primaryStage.show();
	}

	@FXML
	void getBtnUpdate(ActionEvent event) {
		ArrayList<ProductInMachine> productsToUpdate = new ArrayList<>();
		// iterate over the machines that were accessed
		machineDataMap.forEach((machine, data) -> {
			for (InventoryTableData item : data) {
				int stockToAdd = item.getItemStockToAdd();
				/*
				 * if stock to add is more than 0, we set the updated stock to the object and
				 * add it to the list.
				 */
				if (stockToAdd > 0) {
					item.setItemStock(item.getItemStock() + stockToAdd);
					item.setStockToAdd(0);
					ProductInMachine product = item.getItem();
					item.getItem().setRestockFlag(product.getStock() < product.getMinStock());
					productsToUpdate.add(item.getItem());
				}
			}
		});
		if (productsToUpdate.size() > 0) {
			updateProductsInMachine(productsToUpdate.toArray());
			// clear the hash map and reset the combo selection and table so we get the up
			// to date data from database
			machineDataMap.clear();
			cmbChooseMachine.setValue(null);
			tblInventory.setItems(null);
			tblInventory.refresh();
		}
	}

	@FXML
	void getComboMachine(ActionEvent event) {
		Machine selectedMachine = cmbChooseMachine.getValue();
		if (selectedMachine == null)
			return;
		ArrayList<InventoryTableData> inventoryList = machineDataMap.get(selectedMachine);
		// no products list saved for this machine
		if (inventoryList == null) {
			inventoryList = new ArrayList<>();
			ArrayList<ProductInMachine> products = getProductsForMachine(selectedMachine);
			if (products != null) {
				selectedMachine.setProducts(products);
				for (ProductInMachine p : products) {
					inventoryList.add(new InventoryTableData(p));
				}
			}
			machineDataMap.put(selectedMachine, inventoryList);
		}
		tblInventory.setItems(FXCollections.observableArrayList(inventoryList));
	}

	@FXML
	void initialize() {
		ObservableList<Machine> machines = FXCollections
				.observableArrayList(getMachines(new Location[] { getManagerLocation() }));
		if (machines == null)
			return;
		cmbChooseMachine.setItems(machines);
		tblInventory.setEditable(true);
		colItem.setCellValueFactory(data -> data.getValue().itemNameProperty());
		colQuantity.setCellValueFactory(data -> data.getValue().itemStockProperty());
		colRestockAmount.setCellValueFactory(data -> data.getValue().itemStockToAddProperty());
		colRestockAmount.setCellFactory(TextFieldTableCell.forTableColumn());
		colRestockAmount.setOnEditCommit(event -> {
			String value = event.getNewValue();
			// the new value to set, initialized as the old value.
			int newValue = Integer.parseInt(event.getOldValue());
			try {
				/*
				 * try parsing the new value, if we don't get exception and the new value is
				 * non-negative set the value to set as the new value. Otherwise we set back the
				 * old value.
				 */
				int valueInt = Integer.parseInt(value);
				if (valueInt >= 0)
					newValue = valueInt;
			} catch (NumberFormatException exc) {
			}
			event.getRowValue().setStockToAdd(newValue);
			tblInventory.refresh();
		});
	}

	private ArrayList<Machine> getMachines(Location[] locations) {
		// if locations passed is null instantiate it to an empty array.
		if (locations == null)
			locations = new Location[] {};
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.FETCH_MACHINES_BY_LOCATION);
		preparedMessage.setMessageSent(locations);

		// send to server
		System.out.println("Client: Sending machines fetch request to server.");
		ClientUI.clientController.accept(preparedMessage);

		/*
		 * if the response is not the type we expect, something went wrong with server
		 * communication and we throw an exception.
		 */
		if (!(ClientController.responseFromServer.getRequestType()
				.equals(ServerClientRequestTypes.FETCH_MACHINES_BY_LOCATION))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		}
		/*
		 * otherwise we create a Machine arrayList and add the items from response to
		 * it.
		 */
		Object response = ClientController.responseFromServer.getMessageSent();
		ArrayList<?> responseArr = (ArrayList<?>) response;
		if (responseArr.size() == 0)
			return null;
		// return new arrayList with the items from response casted to Machine.

		return responseArr.stream().map(x -> (Machine) x).collect(Collectors.toCollection(ArrayList::new));
	}

	private ArrayList<ProductInMachine> getProductsForMachine(Machine machine) {
		// if machine wasn't passed we return null.
		if (machine == null)
			return null;
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.FETCH_PRODUCTS_IN_MACHINE);
		preparedMessage.setMessageSent(machine);

		// send to server
		System.out.println("Client: Sending machines fetch request to server.");
		ClientUI.clientController.accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType()
				.equals(ServerClientRequestTypes.FETCH_PRODUCTS_IN_MACHINE))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		}
		/*
		 * otherwise we create a Machine arrayList and add the items from response to
		 * it.
		 */
		Object response = ClientController.responseFromServer.getMessageSent();
		ArrayList<?> responseArr = (ArrayList<?>) response;
		if (responseArr.size() == 0)
			return null;
		// return new arrayList with the items from response casted to Machine.

		return responseArr.stream().map(x -> (ProductInMachine) x).collect(Collectors.toCollection(ArrayList::new));
	}

	private boolean updateProductsInMachine(Object[] productsToUpdate) {
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.UPDATE_PRODUCTS_IN_MACHINE);
		preparedMessage.setMessageSent(new Object[] { productsToUpdate });

		// send to server
		System.out.println("Client: Sending products in machine update request to server.");
		ClientUI.clientController.accept(preparedMessage);

		/*
		 * if the response is not the type we expect, something went wrong with server
		 * communication and we throw an exception.
		 */
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		} else {
			Alert successMessage = new Alert(AlertType.INFORMATION);
			successMessage.setTitle("Update Success");
			successMessage.setHeaderText("Update Success");
			successMessage.setContentText("Machines updated successfully!");
			successMessage.show();
		}
		return true;
	}

	private Location getManagerLocation() {
		int currentManagerID = ClientController.getCurrentSystemUser().getId();
		SCCP getCurrentManagerLocationNameRequestMessage = new SCCP();
		getCurrentManagerLocationNameRequestMessage.setRequestType(ServerClientRequestTypes.SELECT);
		getCurrentManagerLocationNameRequestMessage.setMessageSent(new Object[] { "manager_location", true,
				"locationId", true, "idRegionalManager = " + currentManagerID, false, null });
		System.out.println(currentManagerID);

		ClientUI.clientController.accept(getCurrentManagerLocationNameRequestMessage);

		ArrayList<?> currentManagerLocationName = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		Location location = Location.fromLocationId(
				Integer.parseInt(((ArrayList<Object>) currentManagerLocationName.get(0)).get(0).toString()));
		System.out.println(location);
		ClientController.setCurrentUserRegion(location.toString());
		return location;
	}

	/*
	 * for testing
	 * 
	 * Product p1 = new Product("1", "Coca Cola", "5", "Soft Drinks",
	 * "Sweet Drinks"); Product p2 = new Product("2", "Sprite", "7", "Soft Drinks",
	 * "Sweet Drinks"); Product p3 = new Product("3", "Fanta", "8", "Soft Drinks",
	 * "Sweet Drinks"); ArrayList<ProductInMachine> a1 = new ArrayList<>();
	 * ArrayList<ProductInMachine> a2 = new ArrayList<>(); a1.add(new
	 * ProductInMachine(p1, 5)); a1.add(new ProductInMachine(p2, 3)); a2.add(new
	 * ProductInMachine(p2, 7)); a2.add(new ProductInMachine(p3, 12));
	 * machines.add(new Machine(1, "Karmiel", a1, 10, Location.North));
	 * machines.add(new Machine(2, "Dubai", a2, 23, Location.UAE));
	 * 
	 * return machines;
	 */

}
