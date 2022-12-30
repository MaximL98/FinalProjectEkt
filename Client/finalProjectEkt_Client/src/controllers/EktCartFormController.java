package controllers;

import java.text.DecimalFormat;

import java.util.Optional;

import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import javafx.scene.control.Label;

import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Product;

public class EktCartFormController {
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private Button btnCancelOrder;

    @FXML
    private Label lblTotalPrice;
	
	private GridPane gridpaneIntoVbox;
	
	private boolean emptyCart = true;
		
	private VBox vboxCart;
	
	private Double priceToAdd = 0.0;
	
	private Double totalPrice = 0.0;
	
	private void calculatePriceToAdd(Double costPerUnit, Integer quantityNum, Product product) {
		quantityNum = ClientController.currentUserCart.get(product.getProductID());
		costPerUnit = Double.valueOf(product.getCostPerUnit());
		priceToAdd = quantityNum * costPerUnit;
	}
	
	private void calculateTotalPrice() {
		totalPrice = 0.0;
		for (Product product: ClientController.cartPrice.keySet()) {
			System.out.println("Adding the price of " + product.getProductName()+ "in");
			totalPrice += ClientController.cartPrice.get(product);
			System.out.println("Total price now is = " + totalPrice);
		}
	}

	@FXML
	public void initialize() {
		vboxCart = new VBox();
		gridpaneIntoVbox  = new GridPane();
		lblTotalPrice.setText("0$");

		final int numCols = 5;
		Double costPerUnit = 0.0;

		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800/5);
			gridpaneIntoVbox.getColumnConstraints().add(colConst);
		}	
		int i = 0, j = 0;
		for (Product product: ClientController.getProductByID.values()) {
			String currentProductID = product.getProductID();
			calculatePriceToAdd(costPerUnit, ClientController.currentUserCart.get(currentProductID), product);
			ClientController.cartPrice.put(product,priceToAdd);
			calculateTotalPrice();
			lblTotalPrice.setText((new DecimalFormat("##.##").format(totalPrice)).toString() + "$");

			emptyCart = false;
			Text productName = new Text(product.getProductName());
			Text quantityLabel = new Text("Quantity: " + ClientController.currentUserCart.get(currentProductID));
			
			productName.setFont(new Font(18));
			quantityLabel.setFont(new Font(18));
			
			Button removeButton = new Button("remove");
			Button addButton = new Button("+");
			Button removeOneButton = new Button("-");
			removeButton.setFont(new Font(18));
			addButton.setFont(new Font(18));
			removeOneButton.setFont(new Font(18));
			
			

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
			
			removeButton.setOnAction(action -> {
				System.out.println("item" + product.getProductName() + " was removed");
				gridpaneIntoVbox.getChildren().remove(productName);
				gridpaneIntoVbox.getChildren().remove(quantityLabel);
				gridpaneIntoVbox.getChildren().remove(removeButton);
				gridpaneIntoVbox.getChildren().remove(addButton);
				gridpaneIntoVbox.getChildren().remove(removeOneButton);

				//removeProduct = true;
				EktProductFormController.itemsInCart -= ClientController.currentUserCart.get(currentProductID);
				ClientController.currentUserCart.put(currentProductID, 0);
				calculatePriceToAdd(costPerUnit, ClientController.currentUserCart.get(currentProductID), product);
				ClientController.cartPrice.put(product, 0.0);
				calculateTotalPrice();
				lblTotalPrice.setText((new DecimalFormat("##.##").format(totalPrice)).toString() + "$");

			});
			

			addButton.setOnAction(action -> {
				EktProductFormController.itemsInCart++;
				ClientController.currentUserCart.put(currentProductID, ClientController.currentUserCart.get(currentProductID) + 1);
				quantityLabel.setText("Quantity: " + (ClientController.currentUserCart.get(currentProductID).toString()));
				calculatePriceToAdd(costPerUnit, ClientController.currentUserCart.get(currentProductID), product);
				ClientController.cartPrice.put(product, priceToAdd);
				calculateTotalPrice();
				lblTotalPrice.setText((new DecimalFormat("##.##").format(totalPrice)).toString() + "$");

			});
			

			removeOneButton.setOnAction(action -> {
				EktProductFormController.itemsInCart--;
				ClientController.currentUserCart.put(currentProductID, ClientController.currentUserCart.get(currentProductID) - 1);
				quantityLabel.setText("Quantity: " + (ClientController.currentUserCart.get(currentProductID).toString()));
				calculatePriceToAdd(costPerUnit, ClientController.currentUserCart.get(currentProductID), product);
				ClientController.cartPrice.put(product, priceToAdd);
				calculateTotalPrice();
				lblTotalPrice.setText((new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				if (ClientController.currentUserCart.get(currentProductID) < 1) {
					System.out.println("item" + product.getProductName() + " was removed");
					gridpaneIntoVbox.getChildren().remove(productName);
					gridpaneIntoVbox.getChildren().remove(quantityLabel);
					gridpaneIntoVbox.getChildren().remove(removeButton);
					gridpaneIntoVbox.getChildren().remove(addButton);
					gridpaneIntoVbox.getChildren().remove(removeOneButton);
				}

			});
			

			if(!ClientController.currentUserCart.get(currentProductID).equals(0))
				ClientController.arrayOfAddedProductsToGridpane.add(product);
			
			if(ClientController.currentUserCart.get(currentProductID).equals(0)) {
				ClientController.cartPrice.put(product, 0.0);
				gridpaneIntoVbox.getChildren().remove(productName);
				gridpaneIntoVbox.getChildren().remove(quantityLabel);
				gridpaneIntoVbox.getChildren().remove(removeButton);
				gridpaneIntoVbox.getChildren().remove(addButton);
				gridpaneIntoVbox.getChildren().remove(removeOneButton);
			}
			System.out.println("The total price in the cart right now is = " + totalPrice);
			//Implement amount of items
		}
		ClientController.orderTotalPrice = totalPrice;
		System.out.println(ClientController.orderTotalPrice);
		vboxCart.getChildren().add(gridpaneIntoVbox);
		ScrollPane scrollPane = new ScrollPane(vboxCart);
		scrollPane.prefHeight(600);
		scrollPane.prefWidth(800);
		
		borderPane.setCenter(scrollPane);
	}
	
	@FXML
	public void getBtnBack(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		//category is located in a ArrayList
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktProductForm.fxml", null, ClientController.CurrentProductCategory.get(0));
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
		EktProductFormController.itemsInCart = 0;
		//Alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Clear Cart");
		alert.setHeaderText("This action will remove all items from the cart");
		alert.setContentText("Are you sure you want to continue?");
		Optional<ButtonType> result = alert.showAndWait();

		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window

		if (result.get() == ButtonType.OK) {
			//Login window//
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			//category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktCatalogForm.fxml", null, 
					ClientController.CurrentProductCategory.get(0));

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
		if(emptyCart) {
    		//Alert window
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("Empty Cart");
    		alert.setHeaderText("Your cart is empty, you cannot persist to order!");
    		Optional<ButtonType> result = alert.showAndWait();
    		
    		if (result.get() == ButtonType.OK) {
    			//Login window//
    			Stage primaryStage = new Stage();
    			//category is located in a ArrayList
    			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktProductForm.fxml", null, 
    					ClientController.CurrentProductCategory.get(0));
    	
    			ClientController.currentUserCart.keySet().clear();
    			
    			
    			primaryStage.setOnCloseRequest(we -> 
    				{
    					System.out.println("Pressed the X button."); 
    					System.exit(0);
    				});
    			primaryStage.show();
    			//////////////////////
    			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window

    		}
    	}
		else {
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			//category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktOrderSummary.fxml", null, "Order Summary");
			vboxCart.getChildren().clear();
			primaryStage.setOnCloseRequest(we -> 
				{
					System.out.println("Pressed the X button."); 
					System.exit(0);
				});
			primaryStage.show();
		}

	}
}