package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import controllers.EktCatalogFormController;
import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
public class EktProductFormController {

    @FXML
    private VBox vboxProducts;
    
    @FXML
    private Label lblCategoryName;

	@FXML
    private Label lblProductName1;

    @FXML
    private Label lblProductCode1;

    @FXML
    private Label lblPrice1;

    @FXML
    private Button addItem1;

    @FXML
    private Label lblCount1;

    @FXML
    private Label lblProductName2;

    @FXML
    private Label lblProductCode2;

    @FXML
    private Label lblPrice2;

    @FXML
    private Button addItem2;

    @FXML
    private Label lblCount2;

    @FXML
    private Label lblProductName3;

    @FXML
    private Label lblProductCode3;

    @FXML
    private Label lblPrice3;

    @FXML
    private Button addItem3;

    @FXML
    private Label lblCount3;

    @FXML
    private Button btnBack;
    
    @FXML
    private Button btnCart;
    
    @FXML
    private Label txtNumberOfItemsInCart;

    public static  int itemsInCart = 0;
    
    	
	public void initialize() throws FileNotFoundException {
		String  itemsInCartString = itemsInCart + "";
		txtNumberOfItemsInCart.setText(itemsInCartString);
		if (itemsInCart  == 0) 
			txtNumberOfItemsInCart.setStyle("-fx-background-color: #a7e8f2; -fx-opacity:  0.75; -fx-background-radius: 10;");
		
		else
			txtNumberOfItemsInCart.setStyle("-fx-background-color: #da8888; -fx-background-radius: 10; -fx-opacity: 0.75;");
		//Maxim new: added cart image 
		ImageView cartImg = new ImageView(new Image("controllers/Images/cart.png"));
		cartImg.setFitHeight(50);
		cartImg.setPreserveRatio(true);
		btnCart.setGraphic(cartImg);
		
		String productCategory = ClientController.CurrentProductCategory.get(0);
		lblCategoryName.setText(productCategory + " Products");
		
		SCCP preparedMessage = new SCCP();
		
		preparedMessage.setRequestType(ServerClientRequestTypes.FETCH_PRODUCTS_BY_CATEGORY);
		//Search for products for the correct catalog
		preparedMessage.setMessageSent(productCategory);
		//Log message
		System.out.println("Client: Sending " + productCategory + " category to server.");
		
		ClientUI.clientController.accept(preparedMessage);
		
		if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.FETCH_PRODUCTS_BY_CATEGORY)) {
			
			//Might want to check this suppression
			ArrayList<?> arrayOfProducts = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			
			for(Object product: arrayOfProducts) {
				//Main product hbox
				HBox productHBox = new HBox();
				
				
				//ProductName + ProductID + ProductPrice
				VBox productDetails = new VBox();
				Text txtProductName = new Text();
				txtProductName.setText(((Product) product).getProductName());
				txtProductName.setFont(new Font(18));
				Text txtProductID = new Text();
				txtProductID.setText("Product ID: " + ((Product) product).getProductID());
				txtProductID.setFont(new Font(18));
				Text txtProductCostPerUnit = new Text();
				txtProductCostPerUnit.setText("Price: " + ((Product) product).getCostPerUnit());
				txtProductCostPerUnit.setFont(new Font(18));
				
				productDetails.getChildren().add(txtProductName);
				productDetails.getChildren().add(txtProductID);
				productDetails.getChildren().add(txtProductCostPerUnit);
				productDetails.setAlignment(Pos.CENTER);
				////////////////////////////////////////
				String pathToImage = "controllers/Images/" + ((Product) product).getProductID() + ".png";
				ImageView productImageView = new ImageView(new Image(pathToImage));
				productImageView.setFitHeight(200);
				productImageView.setFitWidth(200);
//				///////////////////////////////////////
					
				//AddToCart Button + amountTxt
				VBox productAddToCartVBox = new VBox();
				Button addToCart = new Button();
				
				
				
				addToCart.setOnAction(action -> {
					itemsInCart++;
					if (itemsInCart  == 1) {
						String itemsInCartStr = itemsInCart + "";
						txtNumberOfItemsInCart.setText(itemsInCartStr);
						txtNumberOfItemsInCart.setStyle("-fx-background-color: #da8888; -fx-background-radius: 10; -fx-opacity: 0.75;");
					}
					else {
						String itemsInCartStr = itemsInCart + "";
						txtNumberOfItemsInCart.setText(itemsInCartStr);
					}
					//Increment value of the product key in the hash map
					//If it does not exist, set value to "1"
					ClientController.currentUserCart.merge((Product)product, 1, Integer::sum);
				});
				
				
				
				addToCart.setText("Add To Cart");
				
//				Text amountOfItems = new Text();
//				amountOfItems.setText("");
//				amountOfItems.setFont(new Font(18));
				
				productAddToCartVBox.getChildren().add(addToCart);
//				productAddToCartVBox.getChildren().add(amountOfItems);
				//////////////////////////////////////////////////////
				productAddToCartVBox.setAlignment(Pos.CENTER_RIGHT);
				productHBox.setAlignment(Pos.CENTER);
				productHBox.setPrefSize(800, 200);
				productDetails.setPrefSize(200, 200);
				
				productHBox.getChildren().add(productDetails);
				productHBox.getChildren().add(productImageView);
				productHBox.getChildren().add(productAddToCartVBox);
				vboxProducts.getChildren().add(productHBox);
				System.out.println(((Product) product).getProductID());			
							
			}
			
			
		}
	}
	
	@FXML
	void getBtnBack(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog");
		// this was done so that we can use this button
		primaryStage.setOnCloseRequest(we -> 
		{
			System.out.println("Pressed the X button."); 
			System.exit(0);
		}
		);
		primaryStage.show();
	}
	
	@FXML
    public void getBtnCart(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
        Stage primaryStage = new Stage();
        WindowStarter.createWindow(primaryStage, this, "/gui/EktCartForm.fxml", null, "Ekt Cart");
        primaryStage.setOnCloseRequest(we -> 
            {
            System.out.println("Pressed the X button."); 
            System.exit(0);
            });

        primaryStage.show();
    }
	
	
}
