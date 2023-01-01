package controllers;

import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/*
 * Rotem:
 * I co-opted this window for regional manager.
 * Original class name:
 * EktManagerWelcomePageController
 * new one:
 * this obv
 * 
 */
public class EktRegionalManagerHomePageController {
	
	@FXML
	private Button btnReviewReports;
	@FXML Button btnLogout;
	
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

	@FXML public void getLogoutBtn(ActionEvent event) {
		throw new UnsupportedOperationException("Logout is bad");
		
	}
}
