package controllers;

import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EktCeoHomePageController {

    @FXML
    private Button btnBack;

    @FXML
    private Text txtGreeting;
    
    @FXML
    private void initialize() {
    	// we set greeting to have both first and last name (honor & respect)
    	txtGreeting.
    	setText(txtGreeting.getText().
    			replace(", ", ", "+ClientController.getCurrentSystemUser().getFirstName() 
    					+" " + ClientController.getCurrentSystemUser().getLastName()));
    }
    
    @FXML
    void getBackBtn(ActionEvent event) {
    	System.out.println("CEO with username="+ClientController.getCurrentSystemUser().getUsername()+" is logging out.");
    	ClientController.sendLogoutRequest();
        // move to new window
        ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	Stage primaryStage = new Stage();
    	WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", false);

    	primaryStage.show();	
    	
    }

}
