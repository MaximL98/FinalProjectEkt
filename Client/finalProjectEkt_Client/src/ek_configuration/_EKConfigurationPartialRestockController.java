package ek_configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import controllers.InventoryRestockWorkerPageController.InventoryTableData;
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
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Location;
import logic.Machine;
import logic.Product;
import logic.ProductInMachine;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;

public class _EKConfigurationPartialRestockController {
	
	// daniel code:
	private HashMap<Machine, ArrayList<InventoryTableData>> machineDataMap = new HashMap<>();
	ObservableList<Machine> machines = FXCollections
			.observableArrayList(getMachines(new Location[] {}));
	public class InventoryTableData {
		public InventoryTableData(ProductInMachine item) {
			this.item = item;
			setItemName(item.getProduct().getProductName());
			setItemStock(item.getStock());
			setNewStock(item.getStock());
			setMaxStock(item.getMaxStock());
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

		
		private final StringProperty itemNewStock = new SimpleStringProperty(this, "itemNewStock");;

		public final StringProperty itemNewStockProperty() {
			return itemNewStock;
		}

		public final int getItemNewStock() {
			return Integer.parseInt(itemNewStock.get());
		}

		public final void setNewStock(int newStock) {
			itemNewStock.set(String.valueOf(newStock));
		}
		
		
		private final IntegerProperty maxStock = new SimpleIntegerProperty(this, "maxStock");;

		public final IntegerProperty maxStockProperty() {
			return maxStock;
		}

		public final int getItemMaxStock() {
			return maxStock.get();
		}

		public final void setMaxStock(int stockToAdd) {
			maxStock.set(stockToAdd);
		}
		
		public String toString() {
			return getItemName() + ", " + getItemStock() + ", " + getItemNewStock() + ", " + getItemMaxStock();
		}

	}// daniel code
	
	ArrayList<ProductInMachine> productsInMachineData = new ArrayList<>();

	
	
	
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnFinishRestock;

    @FXML
    private Label statusLabel;

    @FXML
    private Text txtWelcomeText;

	@FXML TableView<InventoryTableData> tblProducts;

	@FXML TableColumn<InventoryTableData, String> colProdName;

	@FXML TableColumn<InventoryTableData, Number> colCurrentStock;

	@FXML TableColumn<InventoryTableData, String> colNewStock;
	
	@FXML TableColumn<InventoryTableData, Number> colMaxStock;
	
	@FXML
	Button change;
	@FXML
	TextField changeTextField;





    @FXML
    private void initialize() {
    	// initialize the table:
    	tblProducts.setEditable(true);
    	colProdName.setCellValueFactory(data -> data.getValue().itemNameProperty());
    	colCurrentStock.setCellValueFactory(data -> data.getValue().itemStockProperty());
    	colNewStock.setCellValueFactory(data -> data.getValue().itemNewStockProperty());
    	colMaxStock.setCellValueFactory(data -> data.getValue().maxStockProperty());

    	colNewStock.setCellFactory(TextFieldTableCell.forTableColumn());
    	colNewStock.setOnEditCommit(event -> {
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
			event.getRowValue().setNewStock(newValue);
			tblProducts.refresh();
		});
    	
    	setUpStuff();
		tblProducts.refresh();

    	
    }
    
    private void setUpStuff() {
		Machine selectedMachine = new Machine(ClientController._EkCurrentMachineID, ClientController._EkCurrentMachineName, null, 0);
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
		tblProducts.setItems(FXCollections.observableArrayList(inventoryList));
		System.out.println("Inventory list="+inventoryList);
    }
    
    // Daniel CODE:
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
    
    // daniel CODE Again;
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
    
    // and again
    private boolean updateProductsInMachine(Object[] productsToUpdate, ActionEvent event) {
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
	    	loadPreviousWindow(event);

			Alert successMessage = new Alert(AlertType.INFORMATION);
			successMessage.setTitle("Update Success");
			successMessage.setHeaderText("Update Success");
			successMessage.setContentText("Machines updated successfully!");
			successMessage.show();
		}
		return true;
	}
    
    @FXML
    void getBtnCancel(ActionEvent event) {
    	// return to previous window
    	loadPreviousWindow(event);
    }

    @FXML
    void getBtnFinishRestock(ActionEvent event) {
    	// update date in table and return
		ArrayList<ProductInMachine> productsToUpdate = new ArrayList<>();
		// iterate over the machines that were accessed
		machineDataMap.forEach((machine, data) -> {
			for (InventoryTableData item : data) {
				int newStock = item.getItemNewStock();
				/*
				 * if stock to add is more than 0, we set the updated stock to the object and
				 * add it to the list.
				 */
				if (newStock != item.getItemStock()) {
					item.setItemStock(newStock);
					// item.setStockToAdd(0);
					productsToUpdate.add(item.getItem());
				}
			}
		});
		if (productsToUpdate.size() > 0) {
			updateProductsInMachine(productsToUpdate.toArray(), event);
			// tblProducts.refresh();
		}
		
		

    }
    
    private void loadPreviousWindow(ActionEvent event) {
    	String nextScreenPath = "/gui/_EKConfigurationLogisticsEmployeeFrame.fxml";
    	// load table - insert all products in this machine
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, nextScreenPath, null, "Logistics Employee Frame", true);
		primaryStage.show();
    }



}
