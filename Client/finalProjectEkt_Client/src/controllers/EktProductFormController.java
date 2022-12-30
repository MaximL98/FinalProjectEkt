
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Product;

import java.awt.Scrollbar;
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
    private Label lblCategoryName;
    
    @FXML
    private BorderPane borderPane;

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
    
    ///// Dima 30/12 17:15
    private int gridPaneRow = 0;
    private String nextItemLocation = "left";
    ////////////////////
    	
	public void initialize() throws FileNotFoundException {
		
		///////////// Dima 30/12 17:39 ////////////
		GridPane gridPaneProducts = new GridPane();
		ColumnConstraints columnLeft = new ColumnConstraints();
	    columnLeft.setPercentWidth(50);
	    ColumnConstraints columnRight = new ColumnConstraints();
	    columnRight.setPercentWidth(50);
	    gridPaneProducts.getColumnConstraints().addAll(columnLeft, columnRight); // each get 50% of width
	    
		gridPaneProducts.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		gridPaneProducts.setPrefSize(800 - 10, 600 - 4);
		gridPaneProducts.setHgap(5);;
		gridPaneProducts.setVgap(5);;
		gridPaneProducts.setPadding(new Insets(5,0,0,6));

	    
		gridPaneProducts.setAlignment(Pos.TOP_CENTER);
		///////////////////////////////////////////
		
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
				
				/////// Dima 30/12 18:05//////////////////////////////////////
				productDetails.getChildren().add(txtProductName);
				productDetails.getChildren().add(txtProductID);
				
				//Implement item on sale	
				if (((Product) product).getProductID().equals("103")) {
					//This is just an example
					txtProductCostPerUnit.setStrikethrough(true);
					Text txtSubscriberSale = new Text();
					txtSubscriberSale.setText("On Sale: 10.90");
					txtSubscriberSale.setFill(Color.CRIMSON);
					txtSubscriberSale.setFont(new Font(18));
					
					productDetails.getChildren().add(txtProductCostPerUnit);
					
					productDetails.getChildren().add(txtSubscriberSale);
				}
				else {
					productDetails.getChildren().add(txtProductCostPerUnit);
				}
				// if(itemOnSale == true) - > add this text:
				//////////////////////////////////////////////////////////////
				
				productDetails.setAlignment(Pos.CENTER);
				
				String pathToImage = "controllers/Images/" + ((Product) product).getProductID() + ".png";
				ImageView productImageView = new ImageView(new Image(pathToImage));
				productImageView.setFitHeight(150);
				productImageView.setFitWidth(150);
				///////////////////////////////////////
					
				//AddToCart Button + amountTxt
				VBox productAddToCartVBox = new VBox();
				
				//////// Dima 30/12 17:29////////////////////
				Button addToCartButton = new Button();
				ImageView addToCartImageView = new ImageView(new Image("controllers/Images/addToCartIcon.png"));
				addToCartImageView.setFitHeight(50);
				addToCartImageView.setFitWidth(50);
				addToCartButton.setPrefSize(50, 50);
				addToCartButton.setGraphic(addToCartImageView);
				////////////////////////////////////////////
				
				
				addToCartButton.setOnAction(action -> {
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
					String currentProductID = ((Product)product).getProductID();
					if (!ClientController.getProductByID.containsKey(currentProductID))
						ClientController.getProductByID.put(currentProductID, (Product) product);
					ClientController.currentUserCart.merge(currentProductID, 1, Integer::sum);
				});
				
//				Text amountOfItems = new Text();
//				amountOfItems.setText("");
//				amountOfItems.setFont(new Font(18));
				
				productAddToCartVBox.getChildren().add(addToCartButton);
//				productAddToCartVBox.getChildren().add(amountOfItems);
				//////////////////////////////////////////////////////
				productAddToCartVBox.setAlignment(Pos.CENTER_RIGHT);
				productHBox.setAlignment(Pos.CENTER);
				productHBox.setPrefSize(400, 150);
				productDetails.setPrefSize(150, 150);
				
				productHBox.getChildren().add(productDetails);
				productHBox.getChildren().add(productImageView);
				productHBox.getChildren().add(productAddToCartVBox);
				
				Pane pane = new Pane();
				pane.setStyle("-fx-border-color: #8A2BE2; -fx-border-width: 5px; -fx-border-radius: 10;"
						+ " -fx-background-color:  #FefFc0; -fx-background-radius: 12");
				pane.getChildren().add(productHBox);
				DropShadow paneShadow = new DropShadow();
				paneShadow.setColor(Color.GREY);
				paneShadow.setRadius(10);
				paneShadow.setSpread(0.2);
				pane.setEffect(paneShadow);
				
				
				///////// Dima 30/12 17:24 ///////////////////////
				if (nextItemLocation.equals("left")) {
					gridPaneProducts.add(pane, 0, gridPaneRow);
					nextItemLocation = "right";
				} 
				else {
					gridPaneProducts.add(pane, 1, gridPaneRow);
					gridPaneRow++;
					nextItemLocation = "left";
				}
				//////////////////////////////////////////////////////
				
				System.out.println(((Product) product).getProductID());			
							
			}
			
			ScrollPane scrollPane = new ScrollPane(gridPaneProducts);
			scrollPane.prefHeight(600);
			scrollPane.prefWidth(800);
			scrollPane.setStyle("-fx-background: #EE82EE; -fx-border-width: 10px; -fx-background-color: BLACK;");
			borderPane.setCenter(scrollPane);
			
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
