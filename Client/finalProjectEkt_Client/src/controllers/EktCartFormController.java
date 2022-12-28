package controllers;

import java.awt.Label;
import java.util.ArrayList;
import java.util.Optional;

import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Product;

public class EktCartFormController {
	
	@FXML
	private VBox vboxCart;
	
	@FXML
	private ScrollBar scrollBar;
	
	@FXML
	private Button btnCancelOrder;
	
	private GridPane gridpaneIntoVbox;
	
	@FXML
	public void initialize() {
		gridpaneIntoVbox  = new GridPane();
		gridpaneIntoVbox.setPrefSize(800, 100);
		gridpaneIntoVbox.setMinHeight(70);
		gridpaneIntoVbox.setMaxWidth(800 - scrollBar.getWidth());
		vboxCart.setMaxWidth(800);
		final int numCols = 5;
		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800/5);
			gridpaneIntoVbox.getColumnConstraints().add(colConst);
		}	
		int i = 0, j = 0;
		
		for (Product product: ClientController.currentUserCart.keySet()) {
			Text productName = new Text(((Product)product).getProductName());
			Text quantityLabel = new Text("Quantity: " + ClientController.currentUserCart.get(product));
			Button removeButton = new Button("remove");
			Button addButton = new Button("+");
			Button removeOneButton = new Button("-");
			
			j=0;
			gridpaneIntoVbox.add(productName, j, i);
			GridPane.setHalignment(productName, HPos.CENTER);
			j++;
			
			gridpaneIntoVbox.add(quantityLabel, j, i);
			GridPane.setHalignment(quantityLabel, HPos.CENTER);
			j++;
			
			gridpaneIntoVbox.add(removeButton, j, i);
			GridPane.setHalignment(removeButton, HPos.CENTER);
			j++;
			
			
			gridpaneIntoVbox.add(addButton, j, i);
			GridPane.setHalignment(addButton, HPos.CENTER);
			j++;
			
			gridpaneIntoVbox.add(removeOneButton, j, i);
			GridPane.setHalignment(removeOneButton, HPos.CENTER);
			i++;
			
			ClientController.arrayOfAddedProductsToGridpane.add(product);
			
			//Implement amount of items
			
		}
		
		vboxCart.getChildren().add(gridpaneIntoVbox);
	}
	
	@FXML
	public void getBtnBack(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktProductForm.fxml", null, 
				ClientController.CurrentProductCategory);
		vboxCart.getChildren().clear();
		primaryStage.setOnCloseRequest(we -> 
			{
				System.out.println("Pressed the X button."); 
				System.exit(0);
			});
		primaryStage.show();
		
		
	}
	
	
	//Upon cancelling order, a window prompt will ask the user if they are sure they want to cancel the order, upon pressing "yes"
	//The user will be disconnected and the login page will be displayed
	@FXML
	public void getBtnCancelOrder(ActionEvent event) {
		//Alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Clear Cart");
		alert.setHeaderText("This action will remove all items from the cart");
		alert.setContentText("Are you sure you want to continue?");
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK) {
			//Login window//
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktCatalogForm.fxml", null, 
					ClientController.CurrentProductCategory);

			ClientController.currentUserCart.keySet().clear();;

			primaryStage.setOnCloseRequest(we -> 
				{
					System.out.println("Pressed the X button."); 
					System.exit(0);
				});
			primaryStage.show();
			//////////////////////
			((Stage) ((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
		}
	}
	
	@FXML
	public void getBtnOrder(ActionEvent event){
		
	}
}
