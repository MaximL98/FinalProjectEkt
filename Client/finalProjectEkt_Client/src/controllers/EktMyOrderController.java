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

import javax.imageio.ImageIO;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
	
		
		
		
		//String order = ClientController
		SCCP preparedMessage = new SCCP();
		
		preparedMessage.setRequestType(ServerClientRequestTypes.SELECT);
		//Search for products for the correct catalog
		
		preparedMessage.setMessageSent(new Object[] {"orders", false, null , false, null , true, "LEFT JOIN order_contents on orders.orderID = order_contents.orderID"});
		//Log message
		System.out.println("Client: Sending " + "order" + " to server.");
		
		ClientUI.clientController.accept(preparedMessage);
		if (ClientController.responseFromServer.getRequestType().equals
				(ServerClientRequestTypes.ACK)) {
			ArrayList<?> arrayOfOrders = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			
			for(Object order: arrayOfOrders) {
				
				System.out.println("The order is = " + order.toString());
			}

		}
		borderPaneInProgress.setCenter(centerScrollBar);
	}

	@FXML
	void getBtnBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog");

		primaryStage.show();
	}
}
