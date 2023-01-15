package ek_configuration;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Product;
import logic.superProduct;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class _EKConfigurationOrderSummaryController {
	// TODO: move this to a dedicated constants class (or to the database if there's time)
	private static final Double COST_REDUCTION_PER_SUBSRIBER = 0.8;
	

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnApprove;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnClose;

	@FXML
	private Text txtOrderTotal;

	private GridPane gridPane;
	
	private Integer totalQuantity = 0;

	private ArrayList<String> OrderInformation = new ArrayList<>(); 
	
	public void initialize() {
		VBox productsVbox = new VBox();
		ScrollPane centerScrollBar = new ScrollPane(productsVbox);

		centerScrollBar.setPrefHeight(600);
		centerScrollBar.setPrefWidth(800);
		centerScrollBar.setStyle(
				"-fx-background-color: transparent; -fx-background:  linear-gradient(from 0px 0px to 0px 1500px, pink, yellow);");
		gridPane = new GridPane();

		Double totalPrice = 0.0;
		final int numCols = 4;
		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800 / 4);
			gridPane.getColumnConstraints().add(colConst);
		}

		///////////////////////// Dima 31/12 10:30
		gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		gridPane.setPrefSize(800 - 2, 550);
		gridPane.setVgap(5);
		//////////////////////////////////////////////////////
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime tomorrow = now.plusDays(1);
		tomorrow = tomorrow.plusHours(1);
		
		ClientController.orderDateReceived = dtf.format(now);
		ClientController.orderDeliveryTime = dtf.format(tomorrow);
		
		OrderInformation.add(ClientController.orderDateReceived);
		OrderInformation.add(ClientController.orderNumber.toString());
		OrderInformation.add("Items in order:");
		
		int i = 0, j = 0;
		for (superProduct product : ClientController.getProductByID.values()) {
			
			if (!(ClientController.cartPrice.get(product) == 0.0)) {
				String currentProductID = product.getProductID();
				Text productName = new Text("" + product.getProductName());
				productName.setStyle("-fx-font: 20 System; -fx-font-weight: bold;");

				Integer quantityNum = ClientController.currentUserCart.get(currentProductID);
				totalQuantity += quantityNum;
				Text quantity = new Text("Quantity: " + (quantityNum).toString());
				Double costPerUnit = Double.valueOf(product.getCostPerUnit());
				if(ClientController.getCustomerIsSubsriber()!=null && ClientController.getCustomerIsSubsriber()) {
					if(firstOrderForSubscriber())
					costPerUnit *= COST_REDUCTION_PER_SUBSRIBER;
				}
				Double totalSum = quantityNum * costPerUnit;
				Text sum = new Text("Cost: " + (new DecimalFormat("##.##").format(totalSum)).toString() + " $");
				totalPrice += totalSum;
				Label emptySpace = new Label("");
				emptySpace.setMinHeight(75);
				productName.setFont(new Font(18));
				quantity.setFont(new Font(18));
				sum.setFont(new Font(18));

				Image img = new Image(new ByteArrayInputStream(product.getFile()));

				ImageView productImageView = new ImageView(img);
				productImageView.setFitHeight(75);
				productImageView.setFitWidth(75);
				productImageView.setTranslateX(20);
				productImageView.setTranslateY(0);

				gridPane.add(productImageView, j, i);
				GridPane.setHalignment(productImageView, HPos.CENTER);

				j++;
				gridPane.add(productName, j, i);
				GridPane.setHalignment(productName, HPos.CENTER);

				j++;
				gridPane.add(quantity, j, i);
				GridPane.setHalignment(quantity, HPos.CENTER);

				j++;
				gridPane.add(sum, j, i);
				GridPane.setHalignment(sum, HPos.CENTER);
//			
//			gridPane.add(emptySpace, j, i);
//			GridPane.setHalignment(emptySpace, HPos.CENTER);
				i++;
				j = 0;
				
				//Max 7/1: add product name in order to array
				OrderInformation.add(product.getProductID());
				OrderInformation.add(quantityNum.toString());
			}
		}
		txtOrderTotal.setText("ORDER TOTAL: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
		txtOrderTotal.setLayoutX(400 - txtOrderTotal.minWidth(0) / 2);
		ClientController.orderTotalPrice = totalPrice;
		OrderInformation.add("\nAt a total price of " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
		//Max 7/1: Add to user order hash map the items in the order!
		ClientController.userOrders.put(ClientController.orderNumber, OrderInformation);
		ClientController.orderTotalQuantity = totalQuantity;
		
		productsVbox.getChildren().add(gridPane);
		borderPane.setCenter(centerScrollBar);

	}

	private boolean firstOrderForSubscriber() {
		// send the following query:
		// select orderID from customer_orders WHERE customerId=ConnectedClientID;
		// if empty, return true, else false
		if(ClientController.getCustomerIsSubsriber()== null || !ClientController.getCustomerIsSubsriber()) {
			System.out.println("Invalid call to firstOrderForSubscriber() -> connected user is not a subsriber");
		}
		ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.SELECT, 
				new Object[]
						{"customer_orders", 
								true, "orderID",
								true, "customerId = " + ClientController.getCurrentSystemUser().getId(),
								false, null}));
		if(!ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
//			throw new RuntimeException("Invalid database operation");
			System.out.println("Invalid database operation (checking subsriber orders history failed). (returnin false [no existing orders])");
			return false; // Rotem forgot to add this back then
		}
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
		// true if we have NO ORDERS else false
		return res.size() == 0;
	}
	

	@FXML
	void getBtnApprove(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationPaymentForm.fxml", null, "payment", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window

	}

	@FXML
	void getBtnBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationCartForm.fxml", null, "Ekt Cart", true);

		primaryStage.show();
	}

	@FXML
	void getBtnClose(ActionEvent event) {
		// Alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.setTitle("Cancel Order");
		alert.setHeaderText("This action will remove all items from the order!");
		alert.setContentText("Are you sure you want to continue?");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			System.out.println("Canceling Order...");
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();

			//category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/_EKConfigurationCustomerLocalOrderFrame.fxml", null, 
					ClientController.CurrentProductCategory.get(0), true);
	
			_EKConfigurationProductController.itemsInCart = 0;
			ClientController.getProductByID.keySet().clear();
			ClientController.cartPrice.keySet().clear();
			ClientController.currentUserCart.keySet().clear();;
			primaryStage.show();
			//////////////////////
			((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
		}
		
		else if (result.get() == ButtonType.CANCEL) {
			System.out.println("Cancel Order was canceled");
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			//category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/_EKConfigurationOrderSummary.fxml", null, "Order Summary", true);

			primaryStage.show();

		}
	}
}
