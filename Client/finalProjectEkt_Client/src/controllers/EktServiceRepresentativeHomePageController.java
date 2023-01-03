package controllers;

import client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;

public class EktServiceRepresentativeHomePageController {

    @FXML
    private Button btnBack;
    
    @FXML
    private Button btnMisc;
    
    @FXML
    private Text txtGreeting;

	@FXML Button btnRegisterCustomer;

    @FXML
    private void initialize() {
    	String tmp = txtGreeting.getText();
    	tmp = tmp.replace(", ", ", "+ClientController.getCurrentSystemUser().getFirstName()+" ");
    	txtGreeting.setText(tmp);
    }
    
    @FXML
    void getBackBtn(ActionEvent event) {
    	System.out.println("Serve 'em, get back2work!");
    }

	@FXML public void getRegisterCustomer(ActionEvent event) {
		System.out.println("Not yet");
	}

	@FXML public void getBtnMisc(ActionEvent event) {
		System.out.println("Arbeit macht frei, so step to.");
	}

}
