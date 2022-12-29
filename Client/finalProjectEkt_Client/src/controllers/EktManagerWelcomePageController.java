package controllers;

import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EktManagerWelcomePageController {
	
	@FXML
	private Button btnReviewReports;
	
	@FXML
	public void getBtnReviewReports(ActionEvent event) {
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktReportSelectForm.fxml", null, "Reviews");
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
}
