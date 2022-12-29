package controllers;

import java.text.DecimalFormat;

import java.util.Optional;

import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Product;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class EktOrderSummaryController {
	@FXML
    private VBox VBox;

	@FXML
    private Button btnApprove;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnClose;
    
    @FXML
    private Label lblTotalPrice;
    
    private GridPane gridPane;
    
    
    public void initialize() {
    	gridPane = new GridPane();
    	gridPane.setPrefSize(800, 100);
    	
    	Double totalPrice = 0.0;
    	final int numCols = 5;
		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800/5);
			gridPane.getColumnConstraints().add(colConst);
		}
		
		int i = 0, j = 0;
    	for(Product product : ClientController.currentUserCart.keySet()) {
    		Text productName = new Text("Product:\n" + product.getProductName());
    		Integer quantityNum = ClientController.currentUserCart.get(product);
    		Text quantity = new Text("Quantity:\n" + (quantityNum).toString());
    		Double costPerUnit = Double.valueOf(product.getCostPerUnit());
    		Double totalSum = quantityNum * costPerUnit;
    		Text sum = new Text("Cost:\n" + (new DecimalFormat("##.##").format(totalSum)).toString() + " $");
    		totalPrice += totalSum;
    		Label emptySpace = new Label("");
    		emptySpace.setMinHeight(75);
    		productName.setFont(new Font(18));
    		quantity.setFont(new Font(18));
    		sum.setFont(new Font(18));
    		
    		
    		String pathToImage = "controllers/Images/" + ((Product) product).getProductID() + ".png";
			ImageView productImageView = new ImageView(new Image(pathToImage));
			productImageView.setFitHeight(75);
			productImageView.setFitWidth(75);
			productImageView.setTranslateX(20);
			productImageView.setTranslateY(75 * i);
			
			gridPane.getChildren().add(productImageView);
			
			j++; 
			gridPane.add(productName, j, i);
			GridPane.setHalignment(productName, HPos.LEFT);
			
			j++;	
			gridPane.add(quantity, j, i);
			GridPane.setHalignment(quantity, HPos.CENTER);
			
			j++;
			gridPane.add(sum, j, i);
			GridPane.setHalignment(sum, HPos.CENTER);
			gridPane.add(emptySpace, j, i);
			GridPane.setHalignment(emptySpace, HPos.CENTER);
			i++;j=0;
    	}
    		lblTotalPrice.setText((new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
    		ClientController.orderTotalPrice = totalPrice;
    		VBox.getChildren().add(gridPane);

    	
    }
    
    
    @FXML
    void getBtnApprove(ActionEvent event) {
    	Stage primaryStage = new Stage();
        WindowStarter.createWindow(primaryStage, this, "/gui/EktPaymentForm.fxml", null, "payment");
        // this was done so that we can use this button
        primaryStage.setOnCloseRequest(we -> 
        {
            System.out.println("Pressed the X button."); 
            System.exit(0);
        }
        );
        primaryStage.show();
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).close(); //closing primary window

    }

    @FXML
    void getBtnBack(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCartForm.fxml", null, "Ekt Cart");
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
    void getBtnClose(ActionEvent event) {
    	//Alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cancel Order");
		alert.setHeaderText("This action will remove all items from the order!");
		alert.setContentText("Are you sure you want to continue?");
		Optional<ButtonType> result = alert.showAndWait();
		
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
}
