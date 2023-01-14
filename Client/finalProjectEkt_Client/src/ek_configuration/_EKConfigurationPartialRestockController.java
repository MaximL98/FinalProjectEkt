package ek_configuration;

import java.util.ArrayList;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Machine;
import logic.Product;
import logic.ProductInMachine;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;

public class _EKConfigurationPartialRestockController {
	ArrayList<ProductInMachine> productsInMachineData = new ArrayList<>();

	
	
	
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnFinishRestock;

    @FXML
    private Label statusLabel;

    @FXML
    private Text txtWelcomeText;

	@FXML TableView<ProductTableClass> tblProducts;

	@FXML TableColumn<ProductTableClass, Integer> colProdID;

	@FXML TableColumn<ProductTableClass, String> colProdName;

	@FXML TableColumn<ProductTableClass, Integer> colCurrentStock;

	@FXML TableColumn<ProductTableClass, Integer> colMaxStock;
	
	@FXML
	Button change;
	@FXML
	TextField changeTextField;

    @FXML
    private void initialize() {
    	// initialize the table:
    	colProdID.setCellValueFactory(new PropertyValueFactory<ProductTableClass, Integer>("id"));
    	colProdName.setCellValueFactory(new PropertyValueFactory<ProductTableClass, String>("name"));
    	colCurrentStock.setCellValueFactory(new PropertyValueFactory<ProductTableClass, Integer>("stock"));
    	colMaxStock.setCellValueFactory(new PropertyValueFactory<ProductTableClass, Integer>("maxStock"));
    	// load products in current machine to the table
    	ArrayList<ProductTableClass> productsInMachineTable = new ArrayList<>();
    	
    	// fuck
    	tblProducts.setEditable(true);
    	tblProducts.getSelectionModel().setCellSelectionEnabled(true);
    	//tblProducts.getColumns().setAll(firstDataColumn, secondDataColumn);
        
    	//colMaxStock.setCellFactory(TextFieldTableCell.forTableColumn());
    	
		// send this query:
		// SELECT productID, productName, stock, max_stock FROM (product JOIN products_in_machine USING(productID)) WHERE machineID=1;
    	Object[] preparedMessage = new Object[] {
    			"(product JOIN products_in_machine USING(productID))", 
    			true, "productID, productName, stock, max_stock",
    			true, " machineID="+ClientController._EkCurrentMachineID,
    			false, null};
    	SCCP msg = new SCCP(ServerClientRequestTypes.SELECT, preparedMessage);
    	ClientUI.clientController.accept(msg);
    	if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
    		@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>)ClientController.responseFromServer.getMessageSent();
    		// set the table accordionly
    		for(ArrayList<Object> row : res) {
    			if(row.size() != 4) {
    				throw new IllegalStateException("Server returned client false values (client threw it)");
    			}
    			Integer prodId = Integer.valueOf(row.get(0).toString());
    			String prodName = row.get(1).toString();
    			Integer stock = Integer.valueOf(row.get(2).toString());
    			Integer maxStock = Integer.valueOf(row.get(3).toString());
    			
    			productsInMachineData.add(new ProductInMachine(new Product(prodId.toString(), prodName, null, null, null),
    					new Machine(ClientController._EkCurrentMachineID, ClientController._EkCurrentMachineName, null, 10),
    					stock, 
    					0, maxStock, false));
    			productsInMachineTable.add(new ProductTableClass(prodId, prodName, stock, maxStock));
    			System.out.println(productsInMachineTable);
    		}
			tblProducts.getItems().setAll(productsInMachineTable);

    		
    	}
    	else {
    		throw new RuntimeException("Query to get products in machine "+ClientController._EkCurrentMachineID+" failed!");
    	}
    }
    
    @FXML
    void getBtnCancel(ActionEvent event) {
    	// return to previous window
    	loadPreviousWindow(event);
    }

    @FXML
    void getBtnFinishRestock(ActionEvent event) {
    	// update date in table and return
    	
    	loadPreviousWindow(event);

    }
    
    private void loadPreviousWindow(ActionEvent event) {
    	String nextScreenPath = "/gui/_EKConfigurationLogisticsEmployeeFrame.fxml";
    	// load table - insert all products in this machine
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, nextScreenPath, null, "Logistics Employee Frame");
		primaryStage.show();
    }

	@FXML public void onEditChanged(TableColumn.CellEditEvent<ProductTableClass, Integer> event) {
		System.out.println("COCKSUCK I NEED THIS PAJEET LIKE A HOLE FUCKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
	}

	@FXML public void onEditChanged() {}

}
