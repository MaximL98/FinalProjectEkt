package controllers;

import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EktMyOrderController {

	@FXML
	private BorderPane borderPaneComplete;

	@FXML
	private BorderPane borderPaneInProgress;

	@FXML
	private Button btnBack;

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
