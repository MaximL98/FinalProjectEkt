package controllers;

import client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class EktCeoHomePageController {

    @FXML
    private Button btnBack;

    @FXML
    private Text txtGreeting;
    
    @FXML
    private void initialize() {
    	String tmp = txtGreeting.getText();
    	System.out.println("We give both first and last name here as a sign of ana rend respekt");
    	tmp = tmp.replace(", ", ", "+ClientController.getCurrentSystemUser().getFirstName() 
    			+" " + ClientController.getCurrentSystemUser().getLastName());
    	txtGreeting.setText(tmp);
    }
    
    @FXML
    void getBackBtn(ActionEvent event) {
    	System.out.println("Don't look back!");
    }

}
