package controllers;

import client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class EktDivisionManagerHomePageController {

    @FXML
    private Button btnBack;

    @FXML
    private Text txtGreeting;

    @FXML
    private void initialize() {
    	String tmp = txtGreeting.getText();
    	tmp = tmp.replace(", ", ", "+ClientController.getCurrentSystemUser().getFirstName()+" ");
    	txtGreeting.setText(tmp);
    }
    
    @FXML
    void getBackBtn(ActionEvent event) {
    	System.out.println("Insert a massage here :)");
    }

}
