package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    
	
	public void initialize() {
		String productCategory = ClientController.getCurrentProductCategory();
		lblCategoryName.setText(productCategory + " Products");
		
		SCCP preparedMessage = new SCCP();
		
		preparedMessage.setRequestType(ServerClientRequestTypes.FETCH_PRODUCTS_BY_CATEGORY);
		//Search for products for the correct catalog
		preparedMessage.setMessageSent(productCategory);
		//Log message
		System.out.println("Client: Sending " + productCategory + " category to server.");
		
		ClientUI.clientController.accept(preparedMessage);
		System.out.println("im stuck");
		if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.FETCH_PRODUCTS_BY_CATEGORY)) {
			System.out.println("im stuck");

			ResultSet resultSetOfProducts = (ResultSet)ClientController.responseFromServer.getMessageSent();
			try {
				while(resultSetOfProducts.next()) {
					String productID = resultSetOfProducts.getString("productID");
					
					String productName = resultSetOfProducts.getString("productName");
					
					String costPerUnit = resultSetOfProducts.getString("costPerUnit");
					
					String category = resultSetOfProducts.getString("category");
					
					String subCategory = resultSetOfProducts.getString("subCategory");
					
					//Main product hbox
					HBox productHBox = new HBox();
					
					//ProductName + ProductID + ProductPrice
					VBox productDetails = new VBox();
					Text txtProductName = new Text(productName);
					Text txtProductID = new Text(productID);
					Text txtProductCostPerUnit = new Text(costPerUnit);
					productDetails.getChildren().add(txtProductName);
					productDetails.getChildren().add(txtProductID);
					productDetails.getChildren().add(txtProductCostPerUnit);
					////////////////////////////////////////
					
					//Product Image
					ImageView productImageView = new ImageView();
					Image productImage = new Image("/gui.Images.Products/" + productID);
					productImageView.setImage(productImage);
					/////////////////////////////////////////
					
					//AddToCart Button + amountTxt
					VBox productAddToCartVBox = new VBox();
					Button addToCart = new Button();
					Text amountOfItems = new Text();
					productAddToCartVBox.getChildren().add(addToCart);
					productAddToCartVBox.getChildren().add(amountOfItems);
					//////////////////////////////////////////////////////
					
					productHBox.setPrefSize(800, 200);
					productHBox.getChildren().add(productDetails);
					productHBox.getChildren().add(productImageView);
					vboxProducts.getChildren().add(productAddToCartVBox);
				}
			} catch (SQLException e) {
				e.printStackTrace();
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
}
