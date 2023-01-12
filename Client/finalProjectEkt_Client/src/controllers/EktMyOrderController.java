package controllers;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Order;

public class EktMyOrderController {

	@FXML
	private BorderPane borderPaneComplete;
	@FXML
	private BorderPane borderPaneInProgress;
	@FXML
	private Button btnBack;
	@FXML
	private GridPane gridPane;
    @FXML
    private TabPane inProgressTabPane;
    @FXML
    private TabPane completedTabPane;

	public void initialize() {
		// In Progress
		VBox productsVbox = new VBox();
		ScrollPane centerScrollBar = new ScrollPane(productsVbox);
		
		
		centerScrollBar.setPrefWidth(750);
		centerScrollBar.setPrefHeight(300);
		centerScrollBar.setStyle(
				"-fx-background-color: transparent; -fx-background:  linear-gradient(from 0px 0px to 0px 1500px, pink, yellow);");
		gridPane = new GridPane();
		
		gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
	
		
		
		////In Progress////
		SCCP preparedMessageForInProgress = new SCCP();
		
		preparedMessageForInProgress.setRequestType(ServerClientRequestTypes.SELECT);
		
		Integer orderId = 0;
		
		preparedMessageForInProgress.setMessageSent(new Object[] {"orders JOIN machine ON orders.machineID = machine.machineId"
				+ " JOIN order_contents ON orders.orderID = order_contents.orderID"
				+ " JOIN product ON order_contents.productID = product.productID", true,
				  "orders.orderID, machine.machineName,"
				+ "orders.date_received, product.productName, orders.total_quantity, orders.total_price", 
				true, "orders.statusId = 1" , true, "ORDER BY orders.orderID"});
		//Log message
		System.out.println("Client: Sending " + "order" + " to server.");
		
		ClientUI.clientController.accept(preparedMessageForInProgress);
		if (ClientController.responseFromServer.getRequestType().equals
				(ServerClientRequestTypes.ACK)) {
			ArrayList<?> arrayOfOrders = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			
			for(Object order: arrayOfOrders) {
				if(orderId != (Integer) ((ArrayList<?>)order).get(0)) {
					orderId = (Integer) ((ArrayList<?>)order).get(0);
					Tab orderTab = new Tab();
					Pane orderPane = new Pane();
					Text location = new Text();
					Text date = new Text();
					
					Text quantity = new Text();
					Text price = new Text();
					
					location.setText("Location: " + ((ArrayList<?>)order).get(1).toString());
					date.setText("Date: " + ((ArrayList<?>)order).get(2).toString());
					
					ArrayList<String> productList = new ArrayList<>();
					
					for(Object product: arrayOfOrders) 
						if(orderId == (Integer) ((ArrayList<?>)product).get(0)) 
							productList.add(((ArrayList<?>)product).get(3).toString());
					
					int i = 45, j = 20;
					
					TitledPane tp = new TitledPane();
					tp.setText("Click Here To See Product List");
					tp.setFont(new Font(14));
					tp.setLayoutX(275);
					tp.setLayoutY(15);
					tp.setPrefWidth(200);
					tp.setExpanded(false);
					Pane intoTp = new Pane();
					
					for(String productName : productList) {
						Text Products = new Text();
						Products.setText(productName);
						Products.setLayoutX(i);
						Products.setLayoutY(j);
						j +=35;
						Products.setFont(new Font(18));
						intoTp.getChildren().add(Products);
					}
					tp.setContent(intoTp);
					orderPane.getChildren().add(tp);
					
					quantity.setText("Total Quantity: " + ((ArrayList<?>)order).get(4).toString());
					price.setText("Total Price: " + ((ArrayList<?>)order).get(5).toString());

					location.setLayoutX(15);
					location.setLayoutY(35);
					location.setFont(new Font(18));
					orderPane.getChildren().add(location);
					
					date.setLayoutX(15);
					date.setLayoutY(70);
					date.setFont(new Font(18));
					orderPane.getChildren().add(date);
					
					quantity.setLayoutX(550);
					quantity.setLayoutY(35);
					quantity.setFont(new Font(18));
					orderPane.getChildren().add(quantity);
					
					price.setLayoutX(550);
					price.setLayoutY(70);
					price.setFont(new Font(18));
					orderPane.getChildren().add(price);
					
					Button reqToCancel = new Button();
					reqToCancel.setText("Request to cancel order");
					reqToCancel.setLayoutX(535);
					reqToCancel.setLayoutY(200);
					reqToCancel.setFont(new Font(15));
					orderPane.getChildren().add(reqToCancel);
					
					reqToCancel.setOnAction(event ->{
						
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.initStyle(StageStyle.UNDECORATED);
						alert.setTitle("Cancel Order");
						alert.setHeaderText("This action will send requset to cancel order!");
						alert.setContentText("Are you sure you want to continue?");
						Optional<ButtonType> result = alert.showAndWait();

						if (result.get() == ButtonType.OK) {
							System.out.println("Sending requset to cancel Order...");
							
							SCCP preparedMessage = new SCCP();
							
							preparedMessage.setRequestType(ServerClientRequestTypes.UPDATE);
							//name of table, add many?, array of objects (to add),  
							//ArrayList<Object> fillArrayToOrder = new ArrayList<>();
							
							Object[] changeOrderStatus = new Object[3];
							
							changeOrderStatus[0] = "orders";
							changeOrderStatus[1] = "statusId = 4";
							changeOrderStatus[2] = "orderID = " + ((ArrayList<?>)order).get(0);
							
							
							preparedMessage.setMessageSent(changeOrderStatus); 
							ClientUI.clientController.accept(preparedMessage);
							
							((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
							Stage primaryStage = new Stage();

							WindowStarter.createWindow(primaryStage, this, "/gui/EktMyOrderFrom.fxml", null, "Ekt My Orders");
							primaryStage.show();
							((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
							
						}
						
						else if (result.get() == ButtonType.CANCEL) {
							System.out.println("Cancel Order was canceled");
						}
					});
					
					orderTab.setContent(orderPane);
					orderTab.setText(orderId.toString());
					
					inProgressTabPane.getTabs().add(orderTab);
				}
			}
		}
		borderPaneInProgress.setCenter(inProgressTabPane);
		////End In Progress////
		
		////Complete////
		SCCP preparedMessageForComplete = new SCCP();
		
		preparedMessageForComplete.setRequestType(ServerClientRequestTypes.SELECT);
		
		Integer orderIdForComplete = 0;
		
		preparedMessageForComplete.setMessageSent(new Object[] {"orders JOIN machine ON orders.machineID = machine.machineId"
				+ " JOIN order_contents ON orders.orderID = order_contents.orderID"
				+ " JOIN product ON order_contents.productID = product.productID", true,
				  "orders.orderID, machine.machineName,"
				+ "orders.date_received, product.productName, orders.total_quantity, orders.total_price", 
				true, "orders.statusId = 3" , true, "ORDER BY orders.orderID"});
		//Log message
		System.out.println("Client: Sending " + "order" + " to server.");
		
		ClientUI.clientController.accept(preparedMessageForComplete);
		if (ClientController.responseFromServer.getRequestType().equals
				(ServerClientRequestTypes.ACK)) {
			ArrayList<?> arrayOfOrders = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			
			for(Object order: arrayOfOrders) {
				if(orderIdForComplete != (Integer) ((ArrayList<?>)order).get(0)) {
					orderIdForComplete = (Integer) ((ArrayList<?>)order).get(0);
					Tab orderTab = new Tab();
					Pane orderPane = new Pane();
					Text location = new Text();
					Text date = new Text();
					
					Text quantity = new Text();
					Text price = new Text();
					
					location.setText("Location: " + ((ArrayList<?>)order).get(1).toString());
					date.setText("Date: " + ((ArrayList<?>)order).get(2).toString());
					
					ArrayList<String> productList = new ArrayList<>();
					
					for(Object product: arrayOfOrders) 
						if(orderIdForComplete == (Integer) ((ArrayList<?>)product).get(0)) 
							productList.add(((ArrayList<?>)product).get(3).toString());
					
					int i = 45, j = 20;
					
					TitledPane tp = new TitledPane();
					tp.setText("Click Here To See Product List");
					tp.setFont(new Font(14));
					tp.setLayoutX(275);
					tp.setLayoutY(15);
					tp.setPrefWidth(200);
					tp.setExpanded(false);
					Pane intoTp = new Pane();
					
					for(String productName : productList) {
						Text Products = new Text();
						Products.setText(productName);
						Products.setLayoutX(i);
						Products.setLayoutY(j);
						j +=35;
						Products.setFont(new Font(18));
						intoTp.getChildren().add(Products);
					}
					tp.setContent(intoTp);
					orderPane.getChildren().add(tp);
					
					quantity.setText("Total Quantity: " + ((ArrayList<?>)order).get(4).toString());
					price.setText("Total Price: " + ((ArrayList<?>)order).get(5).toString());

					location.setLayoutX(15);
					location.setLayoutY(35);
					location.setFont(new Font(18));
					orderPane.getChildren().add(location);
					
					date.setLayoutX(15);
					date.setLayoutY(70);
					date.setFont(new Font(18));
					orderPane.getChildren().add(date);
					
					quantity.setLayoutX(550);
					quantity.setLayoutY(35);
					quantity.setFont(new Font(18));
					orderPane.getChildren().add(quantity);
					
					price.setLayoutX(550);
					price.setLayoutY(70);
					price.setFont(new Font(18));
					orderPane.getChildren().add(price);
					
					orderTab.setContent(orderPane);
					orderTab.setText(orderIdForComplete.toString());
					
					completedTabPane.getTabs().add(orderTab);
				}
			}
		}
		borderPaneComplete.setCenter(completedTabPane);
		////End Complete////
	}

	@FXML
	void getBtnBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog");

		primaryStage.show();
	}
}
