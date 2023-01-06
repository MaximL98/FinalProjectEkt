package controllers;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.Product;

public class TestSelectFromDBProductController {

	//private ObservableList<ObservableList> data;
	
    @FXML
    private Text lblStatus;

    @FXML
    private TableView<Product> tblProducts;
	@FXML TableColumn<Product, String> pid;
	@FXML TableColumn<Product, String> pname;
	@FXML TableColumn<Product, String> pprice;
	@FXML TableColumn<Product, String> pcat;
	@FXML TableColumn<Product, String> psubcat;
    
    @FXML
    private void initialize() {
    	// prep

    	pid.setCellValueFactory(new PropertyValueFactory<Product, String>("productID"));
    	pname.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
    	pprice.setCellValueFactory(new PropertyValueFactory<Product, String>("costPerUnit"));
    	pcat.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
    	psubcat.setCellValueFactory(new PropertyValueFactory<Product, String>("subCategory"));

        
    	SCCP output = new SCCP();
    	output.setRequestType(ServerClientRequestTypes.SELECT);
    	// Arguments passed: {tableName, filterColumns, what, filterRows, where, useSpecial, special}
    	output.setMessageSent(new Object[] {"product", false, null, false, null, false, null});
    	ClientUI.clientController.accept(output);
    	if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.SELECT)) {
    		SCCP answer = ClientController.responseFromServer;
    		ArrayList<Product> productArrayList = new ArrayList<>();
    		if(answer.getMessageSent() instanceof ArrayList) {
    			@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Object>> preProcessedOutput = (ArrayList<ArrayList<Object>>)answer.getMessageSent();
    			for(ArrayList<Object> lst : preProcessedOutput) {
    				// we expect product to have 5 columns, and act accordion-ly
    				Object[] arr = lst.toArray();
    				System.out.println(Arrays.toString(arr));
    				// well...
    				if(arr[4] == null) {
    					arr[4] = "This is what happens when your ";
    				}
    				productArrayList.add(new Product(arr[0].toString(), arr[1].toString(), arr[2].toString(), arr[3].toString(), arr[4].toString()));
    			}
				tblProducts.getItems().setAll(productArrayList);
    		}
    		else {
    			System.out.println("0 -> 1 implies 1 -> 0");
    		}
    		
    	}
    	
    }
    
    /*private void buildData(ResultSet rs) throws Exception{
    	data = FXCollections.observableArrayList();
    	for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
    		final int j = i;
    		TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
    		col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>(){
				@Override
				public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
					// TODO Auto-generated method stub
					return new SimpleStringProperty(param.getValue().get(j).toString());
				}
    		});
    		tblProducts.getColumns().addAll(col);
    		System.out.println("Column [" + i + "] ");
    	}
    	while (rs.next()) {
    		ObservableList<String> row = FXCollections.observableArrayList();
    		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
    			row.add(rs.getString(i));
    		}
    		System.out.println("Row [1] added " + row);
    		data.add(row);
    	}
    		
    	tblProducts.setItems(data);

    }*/
    	
    
}
