package controllers;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EktDivisionManagerHomePageController {
    @FXML
    private Button btnLogout;

    @FXML
    private Button btnViewReports;

    @FXML
    private Text txtGreeting;

    @FXML
    public void initialize() {
    	txtGreeting.setText("WELCOME BACK, " + ClientController.getCurrentSystemUser().getFirstName());
    	txtGreeting.setLayoutX(400 - (txtGreeting.minWidth(0))/ 2);
    }
    
    @FXML
    public void getBtnViewReports(ActionEvent event) {
    	Stage primaryStage = new Stage();
    	WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktReportSelectForm.fxml", null, "Ekt Report Select");
    	primaryStage.show();
    	((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    }
    
    @FXML
    void getBtnLogout(ActionEvent event) {
    	Stage primaryStage = new Stage();
    	WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktSystemUserLoginForm.fxml", null, "Ekt Report Select");
    	ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
    	primaryStage.show();
    	((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    	
    }

}
