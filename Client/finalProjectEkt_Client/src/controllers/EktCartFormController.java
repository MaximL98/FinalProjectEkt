package controllers;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import javafx.scene.control.Label;

import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Product;

public class EktCartFormController {
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private Button btnCancelOrder;

    @FXML
    private Text txtTotalPrice;
	
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
		}
		if(totalPrice == 0.0) {
			emptyCart = true;
			System.out.println("The cart is empty right now!");
		}
	}

	
	@FXML
	public void initialize() {
		vboxCart = new VBox();
		gridpaneIntoVbox  = new GridPane();
		
		txtTotalPrice.setText("CART TOTAL: 0$");
		txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);

		final int numCols = 6;
		Double costPerUnit = 0.0;

		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800/6);
			gridpaneIntoVbox.getColumnConstraints().add(colConst);
		}	
		gridpaneIntoVbox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_COMPUTED_SIZE);
		gridpaneIntoVbox.setPrefSize(800 - 10, 550);
		gridpaneIntoVbox.setHgap(5);;
		gridpaneIntoVbox.setVgap(5);;
		int i = 0, j = 0;
		for (Product product: ClientController.getProductByID.values()) {
	
			String currentProductID = product.getProductID();
			calculatePriceToAdd(costPerUnit, ClientController.currentUserCart.get(currentProductID), product);
			ClientController.cartPrice.put(product,priceToAdd);
			calculateTotalPrice();
			txtTotalPrice.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
			txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);
			
//			emptyCart = false;
			
			///////////// Dima 31/12 10:15
			Image removeItemIcon = new Image("controllers/Images/removeItemFromCart.png");
			ImageView removeItemIconImageView = new ImageView(removeItemIcon);
			removeItemIconImageView.setFitHeight(30);
			removeItemIconImageView.setFitWidth(30);
			
			Image addOneToCartIcon = new Image("controllers/Images/addOneToCart.png");
			ImageView addOneToCartIconImageView = new ImageView(addOneToCartIcon);
			addOneToCartIconImageView.setFitHeight(30);
			addOneToCartIconImageView.setFitWidth(30);
			
			Image removeOneToCartIcon = new Image("controllers/Images/removeOneFromCart.png");
			ImageView removeOneFromCartIconImageView = new ImageView(removeOneToCartIcon);
			removeOneFromCartIconImageView.setFitHeight(30);
			removeOneFromCartIconImageView.setFitWidth(30);
			
			///////////////////////////////////////////////////////
			
			//getting files (images) for product from database, based on product id
			SCCP getImageFromDatabase = new SCCP();
			
			getImageFromDatabase.setRequestType(ServerClientRequestTypes.SELECT);
			//Search for products for the correct catalog
			
			getImageFromDatabase.setMessageSent(new Object[] {"files", false, null , true, "file_name = '" + ((Product) product).getProductID() + ".png'" , false, null});
			//Log message
			System.out.println("Client: Sending " + "Product Files" + " to server.");
			
			Image img = null;
			ClientUI.clientController.accept(getImageFromDatabase);
			if (ClientController.responseFromServer.getRequestType().equals
					(ServerClientRequestTypes.ACK)) {
				//[[file_id, file, file_name] , [...]]
				@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Object>> arrayOfFiles = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
				
				for(ArrayList<Object> file: arrayOfFiles) {
					System.out.println("The file is = " + file.toString());
					img = new Image(new ByteArrayInputStream((byte[])file.get(1)));
				}

			}
			
			ImageView productImageView = new ImageView(img);
			
			productImageView.setFitHeight(75);
			productImageView.setFitWidth(75);
			productImageView.setTranslateX(20);
			productImageView.setTranslateY(0);
			gridpaneIntoVbox.add(productImageView, j, i);
			
			///////////////////////////////////////////////////////
			//Image productImage = new Image("controllers/Images/" + currentProductID + ".png");
			//ImageView productImageView = new ImageView(productImage);
			
					
			
			Text productName = new Text(product.getProductName());
			Text quantityLabel = new Text("Quantity: " + ClientController.currentUserCart.get(currentProductID));
			
			productName.setStyle("-fx-font: 18 System; -fx-font-weight: bold;");
			productName.setFont(new Font(18));
			quantityLabel.setFont(new Font(18));
			
			Button removeButton = new Button();
			Button addButton = new Button();
			Button removeOneButton = new Button();
			removeButton.setFont(new Font(18));
			addButton.setFont(new Font(18));
			removeOneButton.setFont(new Font(18));
			/////////////////////// Dima 31/12 10:18
			removeButton.setPrefSize(50, 50);
			removeButton.setGraphic(removeItemIconImageView);
			removeButton.setStyle("-fx-background-color: transparent; -fx-border-color:crimson; "
					+ "-fx-border-width: 1px; -fx-border-radius: 100");
			
			addButton.setPrefSize(50, 50);
			addButton.setGraphic(addOneToCartIconImageView);
			addButton.setStyle("-fx-background-color: transparent; -fx-border-color:crimson; "
					+ "-fx-border-width: 1px; -fx-border-radius: 100");
			
			removeOneButton.setPrefSize(50, 50);
			removeOneButton.setGraphic(removeOneFromCartIconImageView);
			removeOneButton.setStyle("-fx-background-color: transparent; -fx-border-color:crimson; "
					+ "-fx-border-width: 1px; -fx-border-radius: 100");
			///////////////////////////////////////////////////////////////////////
			
			j++;
			
			gridpaneIntoVbox.add(productName, j, i);
			GridPane.setHalignment(productName, HPos.CENTER);
			j++;
			
			gridpaneIntoVbox.add(quantityLabel, j, i);
			GridPane.setHalignment(quantityLabel, HPos.CENTER);
			j++;
			
			gridpaneIntoVbox.add(addButton, j, i);
			GridPane.setHalignment(addButton, HPos.RIGHT);
			j++;
			
			gridpaneIntoVbox.add(removeOneButton, j, i);
			GridPane.setHalignment(removeOneButton, HPos.LEFT);
			j++;
			
			gridpaneIntoVbox.add(removeButton, j, i);
			GridPane.setHalignment(removeButton, HPos.RIGHT);
			i++; j = 0;
			
			
			removeButton.setOnAction(action -> {
				System.out.println("item" + product.getProductName() + " was removed");
				gridpaneIntoVbox.getChildren().remove(productName);
				gridpaneIntoVbox.getChildren().remove(quantityLabel);
				gridpaneIntoVbox.getChildren().remove(removeButton);
				gridpaneIntoVbox.getChildren().remove(addButton);
				gridpaneIntoVbox.getChildren().remove(removeOneButton);
				gridpaneIntoVbox.getChildren().remove(productImageView);

				//removeProduct = true;
				EktProductFormController.itemsInCart -= ClientController.currentUserCart.get(currentProductID);
				ClientController.currentUserCart.put(currentProductID, 0);
				calculatePriceToAdd(costPerUnit, ClientController.currentUserCart.get(currentProductID), product);
				ClientController.cartPrice.put(product, 0.0);
				calculateTotalPrice();
				txtTotalPrice.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);
				
				//Max 7/1
				if(EktProductFormController.itemsInCart == 0){
					ClientController.currentUserCart.keySet().clear();
					ClientController.getProductByID.keySet().clear();
					ClientController.cartPrice.keySet().clear();
				}

			});
			

			addButton.setOnAction(action -> {
				EktProductFormController.itemsInCart++;
				ClientController.currentUserCart.put(currentProductID, ClientController.currentUserCart.get(currentProductID) + 1);
				quantityLabel.setText("Quantity: " + (ClientController.currentUserCart.get(currentProductID).toString()));
				calculatePriceToAdd(costPerUnit, ClientController.currentUserCart.get(currentProductID), product);
				ClientController.cartPrice.put(product, priceToAdd);
				calculateTotalPrice();
				txtTotalPrice.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);
			});
			

			removeOneButton.setOnAction(action -> {
				EktProductFormController.itemsInCart--;
				ClientController.currentUserCart.put(currentProductID, ClientController.currentUserCart.get(currentProductID) - 1);
				quantityLabel.setText("Quantity: " + (ClientController.currentUserCart.get(currentProductID).toString()));
				calculatePriceToAdd(costPerUnit, ClientController.currentUserCart.get(currentProductID), product);
				ClientController.cartPrice.put(product, priceToAdd);
				calculateTotalPrice();
				txtTotalPrice.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);
				if (ClientController.currentUserCart.get(currentProductID) < 1) {
					System.out.println("item" + product.getProductName() + " was removed");
					gridpaneIntoVbox.getChildren().remove(productName);
					gridpaneIntoVbox.getChildren().remove(quantityLabel);
					gridpaneIntoVbox.getChildren().remove(removeButton);
					gridpaneIntoVbox.getChildren().remove(addButton);
					gridpaneIntoVbox.getChildren().remove(removeOneButton);
					gridpaneIntoVbox.getChildren().remove(productImageView);
				}
				//Max 7/1
				if(EktProductFormController.itemsInCart == 0){
					ClientController.currentUserCart.keySet().clear();
					ClientController.getProductByID.keySet().clear();
					ClientController.cartPrice.keySet().clear();
				}

			});
			

			if(!ClientController.currentUserCart.get(currentProductID).equals(0)) {
				ClientController.arrayOfAddedProductsToGridpane.add(product);
				emptyCart = false;
			}
			
			if(ClientController.currentUserCart.get(currentProductID).equals(0)) {
				ClientController.cartPrice.put(product, 0.0);
				emptyCart = true;
				gridpaneIntoVbox.getChildren().remove(productName);
				gridpaneIntoVbox.getChildren().remove(quantityLabel);
				gridpaneIntoVbox.getChildren().remove(removeButton);
				gridpaneIntoVbox.getChildren().remove(addButton);
				gridpaneIntoVbox.getChildren().remove(removeOneButton);
				gridpaneIntoVbox.getChildren().remove(productImageView);
				
			}
			//Implement amount of items
		}

		ClientController.orderTotalPrice = totalPrice;
		System.out.println("total price = " + ClientController.orderTotalPrice);
		vboxCart.getChildren().add(gridpaneIntoVbox);
		ScrollPane scrollPane = new ScrollPane(vboxCart);
		
		scrollPane.prefHeight(600);
		scrollPane.prefWidth(800);
		scrollPane.setStyle("-fx-background:  linear-gradient(from -120px -120px to 0px 1620px, pink, yellow); -fx-border-color: transparent;"
				+ "-fx-background-color: transparent;");
		
		borderPane.setCenter(scrollPane);
	}
	
	@FXML
	public void getBtnBack(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		//category is located in a ArrayList
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktProductForm.fxml", null, ClientController.CurrentProductCategory.get(0));
		vboxCart.getChildren().clear();
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
		alert.initStyle(StageStyle.UNDECORATED);
		Optional<ButtonType> result = alert.showAndWait();

		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window

		if (result.get() == ButtonType.OK) {
			System.out.println("Cleaning cart...");
			EktProductFormController.itemsInCart = 0;
			
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			//category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktCatalogForm.fxml", null, 
					ClientController.CurrentProductCategory.get(0));

			ClientController.currentUserCart.keySet().clear();
			ClientController.getProductByID.keySet().clear();
			ClientController.cartPrice.keySet().clear();
			primaryStage.show();
			//////////////////////
			((Stage) ((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
		}
		else if(result.get() == ButtonType.CANCEL) {
			System.out.println("Clear cart was canceled");
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
	        WindowStarter.createWindow(primaryStage, this, "/gui/EktCartForm.fxml", null, "Ekt Cart");
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

			primaryStage.show();
		}

	}
}