package controllers;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EktReportDisplayPageController {

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnBack;

	@FXML
	private Text txtMachineName;
	
	@FXML
	private Text txtLocationName;
	
	@FXML
	private Text txtTypeOfReport;

	@FXML
	private VBox vboxRight;
	
	String nameOfLocation, nameOfMachine;
	
	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {
		String typeOfReport = ClientController.getMachineID_TypeOfReport_Dates().get(0);
		nameOfLocation = ClientController.getMachineID_TypeOfReport_Dates().get(1);
		nameOfMachine = ClientController.getMachineID_TypeOfReport_Dates().get(2);
		
		
		int month = getMonthNumberByString(ClientController.getMachineID_TypeOfReport_Dates().get(3));
		int year = Integer.parseInt(ClientController.getMachineID_TypeOfReport_Dates().get(4));
		
		txtTypeOfReport.setText(typeOfReport + "Distribution Report");
		txtLocationName.setText(nameOfLocation);
		txtMachineName.setText("Machine: " + nameOfMachine);
		
		//Clear the array for the next report viewing
		ClientController.getMachineID_TypeOfReport_Dates().clear();
		
		//Center the text headers
		txtTypeOfReport.setLayoutX(400 - (txtTypeOfReport.minWidth(0))/2);
		txtLocationName.setLayoutX(400 - (txtLocationName.minWidth(0))/2);
		txtMachineName.setLayoutX(400 - (txtMachineName.minWidth(0))/2);
		
		
		////Create graph or chart
		switch (typeOfReport) {
			case "Orders":
				int monthBeginning = month;
				int yearBeginning = year;
				//Fetch data from the database
				SCCP fetchOrdersMessage = new SCCP();
				if (month + 1 == 13) {
					year++;
					monthBeginning = 1;
				}
				fetchOrdersMessage.setRequestType(ServerClientRequestTypes.SELECT);
				fetchOrdersMessage.setMessageSent(new Object[] {"machine", true, "orderID, total_price, total_quantity, typeId",
						false, null, true, "LEFT JOIN ektdb.orders on machine.machineId = orders.machineID"
								+ " LEFT JOIN ektdb.locations on locations.locationID = machine.locationId "
								+ "WHERE total_quantity IS NOT NULL AND date_received >= \"" + yearBeginning + "-" +  monthBeginning + "-01" + "\" AND date_received < \""
								+ year + "-" +  month + "-1" + "\" AND machineName = \"" + nameOfMachine + "\";" });
				
				ClientUI.clientController.accept(fetchOrdersMessage);
				
				ArrayList<?> ordersInCurrentMachine = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
				
				double profits = 0;
				int orderQuantity = 0;
				int amountOfItems = 0;
				//Counts number of orders
				int localOrdersCounter = 0;
				int pickupOrdersCounter = 0;
				int deliveryOrdersCounter = 0;
				
				PieChart pChart;
				
				if (ordersInCurrentMachine.isEmpty()) {
					System.out.println("Empty list!");
				} else {

					for(ArrayList<Object> orders: (ArrayList<ArrayList<Object>>) ordersInCurrentMachine) {
						amountOfItems +=  (Integer)orders.get(2);				
						profits += (int)orders.get(1);
						
						//Get the 4th column's (typeID) of our SQL query
						switch (((Integer) orders.get(3))) {
							//This row is a Pickup order
							case 1:
								pickupOrdersCounter++;
								orderQuantity++;
								break;
							//This row is a delivery order
							case 2:
								deliveryOrdersCounter++;
								orderQuantity++;
								break;
							//This row is a local order
							case 3:
								localOrdersCounter++;
								orderQuantity++;
								break;
							default:
								continue;
						}
					}
					//Create an observable list for the pie chart
					ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList( 
						new PieChart.Data("Pickup Orders: " + pickupOrdersCounter, pickupOrdersCounter),
						new PieChart.Data("Delivery Orders: " + deliveryOrdersCounter, deliveryOrdersCounter),
						new PieChart.Data("Local Orders: " + localOrdersCounter, localOrdersCounter)
					);
					pChart = new PieChart(pieData);
					pChart.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
					borderPane.setCenter(pChart);
				}
				Text txtProfits = new Text("Profits: " + profits + "$");
				txtProfits.setFont(Font.font("Berlin Sans FB", 22));
				Text txtorderQuantity = new Text("Order quantity: " + orderQuantity);
				txtorderQuantity.setFont(Font.font("Berlin Sans FB", 22));
				Text txtamountOfItems = new Text("Products sold: " + amountOfItems);
				txtamountOfItems.setFont(Font.font("Berlin Sans FB", 22));

				vboxRight.getChildren().add(txtProfits);
				vboxRight.getChildren().add(txtorderQuantity);
				vboxRight.getChildren().add(txtamountOfItems);
				break;
				
			case "Inventory":
				//Fetch data from the database
				SCCP fetchInventoryMessage = new SCCP();
				fetchInventoryMessage.setRequestType(ServerClientRequestTypes.SELECT);
				fetchInventoryMessage.setMessageSent(new Object[] {"machine", true, "orderID, total_amount, attribute, typeId",
						false, null, true, ""});
				
				ClientUI.clientController.accept(fetchInventoryMessage);
				break;
			case "Customer":
				
		}
		
	}


	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportSelectForm.fxml", null, "Ekt Report Select Form");
		primaryStage.setOnCloseRequest(we -> {
			System.out.println("Pressed the X button.");
			System.exit(0);
		});
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
	}
	
	private int getMonthNumberByString(String string) {
		switch (string) {
	    case "January":
	        return 1;
	    case "February":
	        return 2;
	    case "March":
	        return 3;
	    case "April":
	        return 4;
	    case "May":
	        return 5;
	    case "June":
	        return 6;
	    case "July":
	        return 7;
	    case "August":
	        return 8;
	    case "September":
	        return 9;
	    case "October":
	        return 10;
	    case "November":
	        return 11;
	    case "December":
	        return 12;
	    default:
	        return 0;
	}
		
	}
	
}
