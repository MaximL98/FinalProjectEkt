package controllers;

import client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class EktLogisticsManagerHomePageController {

    @FXML
    private Button btnBack;

    @FXML
    private Text txtGreeting;

    @FXML
    private void initialize() {
    	txtGreeting.setText(txtGreeting.getText().concat(" ".concat(ClientController.getCurrentSystemUser().getFirstName())));
    }
    
    @FXML
    void getBackBtn(ActionEvent event) {
    	System.out.println("Operation not supported - replace this with a logout button!");
    }

}
