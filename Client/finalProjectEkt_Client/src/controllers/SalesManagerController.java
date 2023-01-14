package controllers;

import java.io.IOException;

import client.ClientController;
import client.ClientUI;
import client.InactivityChecker;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SalesManagerController {

    @FXML
    private Button btnAddNewPromotion;

    @FXML
    private Button btnEditActivePromotions;

    @FXML
    private Button btnLogout;
    @FXML
    private Text txtSalesManager;


    @FXML
    private void addNewPromotionHandler(ActionEvent event) throws IOException {
    	InactivityChecker inactivityChecker = new InactivityChecker(300000, event);//importent
    	inactivityChecker.updateActivityTime();
        // Get the current stage
        Stage stage = new Stage();
        WindowStarter.createWindow(stage, new Object(), "/gui/PromotionEditing.fxml", null, "Promotion Editing", true);
        stage.show();
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
        Thread inactivityCheckerThread = new Thread(inactivityChecker);//importent
        inactivityCheckerThread.start();
        

    }
    

    @FXML
    private void editActivePromotionsHandler(ActionEvent event) {
    	// Rotem 1.13 - added event to constructor so that we can actually close the window
    	InactivityChecker inactivityChecker = new InactivityChecker(300000, event);//importent
    	inactivityChecker.updateActivityTime();
        // Get the current stage
        Stage stage = new Stage();
        WindowStarter.createWindow(stage, new Object(), "/gui/EditActivePromotions.fxml", null, "Promotion Editing", true);

        stage.show();
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
        Thread inactivityCheckerThread = new Thread(inactivityChecker);//importent
        inactivityCheckerThread.start();
        
    }

    // Rotem:
    // I fucxed it to actually work as a log-out button (it used to work like an exit button)
    @FXML
    private void logoutHandler(ActionEvent event) throws Exception {
    	// actually do the logout:
    	ClientController.sendLogoutRequest();
    	
    	// log
    	System.out.println("Sales manager has logged off");
	    ((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);
		primaryStage.show();
    }
    
    @FXML
    public void initialize() {
    	// you can't have a label when it's sales manager (it can't happen sry)

    	//txtSalesManager.setText("Hello, " + ClientController.getCurrentSystemUser().getFirstName() + "!");
    	txtSalesManager.setLayoutX(400-(txtSalesManager.minWidth(0)/2));
    }
}

