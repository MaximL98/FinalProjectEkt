
package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Product;

import java.awt.Scrollbar;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;


import controllers.EktCatalogFormController;
import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;

public class EktProductFormController {
	
    @FXML
    private Text txtCategoryName;
    
    @FXML
    private Pane topPane;
    
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
		///////////// Dima 31/12 10:00 ////////////
		GridPane gridPaneProducts = new GridPane();
		ColumnConstraints columnLeft = new ColumnConstraints();
	    columnLeft.setPercentWidth(50);
	    ColumnConstraints columnRight = new ColumnConstraints();
	    columnRight.setPercentWidth(50);
	    gridPaneProducts.getColumnConstraints().addAll(columnLeft, columnRight); // each gets 50% of width
	    
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
		cartImg.setFitWidth(50);
		cartImg.setPreserveRatio(true);
		btnCart.setGraphic(cartImg);
		
		String productCategory = ClientController.CurrentProductCategory.get(0);
		txtCategoryName.setText(productCategory);
		//txtCategoryName.setTextFill(Color.WHITE);
		txtCategoryName.setLayoutX(400 - (txtCategoryName.minWidth(gridPaneRow))/2);
		
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
				txtProductName.setFill(Color.BLACK);
				txtProductName.setStyle("-fx-font: 20 System; -fx-font-weight: bold;");
				
				Text txtProductID = new Text();
				txtProductID.setText("");
				txtProductID.setFont(new Font(18));
				txtProductID.setFill(Color.BLACK);
				Text txtProductCostPerUnit = new Text();
				txtProductCostPerUnit.setText(((Product) product).getCostPerUnit() + "$");
				txtProductCostPerUnit.setFont(new Font(18));
				txtProductCostPerUnit.setFill(Color.BLACK);
				
				/////// Dima 30/12 18:05//////////////////////////////////////
				productDetails.getChildren().add(txtProductName);
				//productDetails.getChildren().add(txtProductID);
				
				//Implement item on sale	
				//if(ClientController.getcurrentCustomer == Subscriber AND product == ON-SALE -> display item on sale
				if (((Product) product).getProductID().equals("103")) {
					//This is just an example
					txtProductCostPerUnit.setStrikethrough(true);
					Text txtSubscriberSale = new Text();
					txtSubscriberSale.setText("ON SALE: 10.90$");
					txtSubscriberSale.setFill(Color.CRIMSON);
					txtSubscriberSale.setFont(new Font(18));
					txtSubscriberSale.setStyle("-fx-font: 20 System; -fx-font-weight: bold;");
					
					productDetails.getChildren().add(txtProductCostPerUnit);
					
					productDetails.getChildren().add(txtSubscriberSale);
				}
				else {
					productDetails.getChildren().add(txtProductCostPerUnit);
				}
				// if(itemOnSale == true) - > add this text:
				//////////////////////////////////////////////////////////////
				
				productDetails.setAlignment(Pos.CENTER);
				
				//getting files (images) for product from database, based on product id
				SCCP getImageFromDatabase = new SCCP();
				
				getImageFromDatabase.setRequestType(ServerClientRequestTypes.SELECT);
				//Search for products for the correct catalog
				
				getImageFromDatabase.setMessageSent(new Object[] {"files", false, null , true, "file_name = '" + ((Product) product).getProductID() + ".png'" , false, null});
				//Log message
				System.out.println("Client: Sending " + "Product Files" + " to server.");
				
				
				ClientUI.clientController.accept(getImageFromDatabase);
				if (ClientController.responseFromServer.getRequestType().equals
						(ServerClientRequestTypes.ACK)) {
					//[[file_id, file, file_name] , [...]]
					@SuppressWarnings("unchecked")
					ArrayList<ArrayList<Object>> arrayOfFiles = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
					
					for(ArrayList<Object> file: arrayOfFiles) {
						System.out.println("The file is = " + file.toString());
						Image img = new Image(new ByteArrayInputStream((byte[])file.get(1)));
						ImageView productImageView = new ImageView(img);
						productImageView.setFitHeight(150);
						productImageView.setFitWidth(150);
						productHBox.getChildren().add(productImageView);

					}

				}
				
				//AddToCart Button + amountTxt
				VBox productAddToCartVBox = new VBox();
				
				//////// Dima 30/12 17:29////////////////////
				Button addToCartButton = new Button();
				ImageView addToCartImageView = new ImageView(new Image("controllers/Images/addToCartIcon.png"));
				addToCartImageView.setFitHeight(50);
				addToCartImageView.setFitWidth(45);
				addToCartButton.setPrefSize(50, 50);
				addToCartButton.setGraphic(addToCartImageView);
				addToCartButton.setStyle("-fx-background-color: transparent; -fx-border-color:crimson; "
						+ "-fx-border-width: 1px; -fx-border-radius: 100");
				////////////////////////////////////////////
			
				
				addToCartButton.setOnAction(event -> {
					
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
					//Animate add to cart
					

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
				productHBox.getChildren().add(productAddToCartVBox);
				
				Pane pane = new Pane();
				pane.setStyle("-fx-border-color: black; -fx-border-width: 3px; -fx-border-radius: 10;"
						+ " -fx-background-color:   linear-gradient(from 0px 0px to 0px 1500px, pink, yellow); -fx-background-radius: 12");

				pane.getChildren().add(productHBox);
				DropShadow paneShadow = new DropShadow();
				paneShadow.setColor(Color.YELLOW);
				paneShadow.setRadius(1);
				paneShadow.setSpread(0.001);
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
			////////////////// Dima 31/12 10:50 changed styling into this
			Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
			borderPane.setCenter(scrollPane);
			System.out.println("WARNING HAPPENS HERE:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			scrollPane.setStyle("-fx-background-color: transparent; -fx-background:  linear-gradient(from 0px 0px to 0px 1500px, pink, red);"
					+ "-fx-border-color: transparent;");
			scrollPane.setBorder(border);
			
			//////////////////////////////////////////////////
			
			//linear-gradient(from 0px 0px to 0px 1500px, black, crimson);
		}
	}
	
	@FXML
	void getBtnBack(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog");

		primaryStage.show();
	}
	
	@FXML
    public void getBtnCart(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
        Stage primaryStage = new Stage();
        WindowStarter.createWindow(primaryStage, this, "/gui/EktCartForm.fxml", null, "Ekt Cart");


        primaryStage.show();
    }
	
	
}
