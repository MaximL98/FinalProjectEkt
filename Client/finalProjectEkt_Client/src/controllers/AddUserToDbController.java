package controllers;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.SystemUser;
import javafx.scene.control.TableView;

public class AddUserToDbController {

    @FXML
    private Button btnConnect;

    @FXML
    private TextField txtCreditCard;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtUsername;

	@FXML Text lblStatus;

	@FXML TextField txtRole;

	@FXML Button btnAdd;
	
	@FXML Button btnBack;

    @FXML
    public void initialize() {
        
        
    }

	@FXML
    void getAddUserToDB(ActionEvent event) {
    	int id;
		lblStatus.setText("Checking input");
    	SCCP preparedMessage = new SCCP();
    	if(validFieldInput()) {
    		id = Integer.valueOf(txtID.getText());
    		// set message accordingly
    		preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
    		// first field is table name - users here
    		Object[] fill = new Object[3];
    		fill[0] = "systemuser"; // add to table "systemusers" (hard code it elsewhere)
    		fill[1] = false; // add only 1
    		fill[2] = new Object[] {new SystemUser(id, txtFirstName.getText(), txtLastName.getText(), 
    				txtPhoneNumber.getText(), txtEmail.getText(), txtCreditCard.getText(), txtUsername.getText(), txtPassword.getText(), txtRole.getText())};
    		preparedMessage.setMessageSent(fill);
    		
    		// send to server
    		ClientUI.clientController.accept(preparedMessage);
    		
    		// check comm for answer:
    		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
    			// add test that response.messageSent is the array we had in fill[2] (SAME OBJECT)
    			lblStatus.setText("Successfully added a " + txtRole.getText()+"!");
    			// clean all fields:
    			txtID.setText("");
    			txtFirstName.setText("");
    			txtLastName.setText("");
    			txtPhoneNumber.setText("");
    			txtEmail.setText("");
    			txtCreditCard.setText("");
    			txtUsername.setText("");
    			txtPassword.setText("");
    			txtRole.setText("");


    		}
    		else {
    			lblStatus.setText("ERROR!"); // add specifics
    		}
    		
    	}
    	else {
    		lblStatus.setText("Status: Invalid input");
    	}
    }

	private boolean validFieldInput() {
		boolean valid = true;
		// I forgot to check ID
		if(txtID.getText().length() < 1) {
			return false;
		}
		// check username and password not empty:
		if(txtUsername.getText().length() < 1 || txtPassword.getText().length() < 1)
			return false;
		// check name is letters
		if(txtFirstName.getText().length() < 2 || !(txtFirstName.getText().matches("^[a-zA-Z]*$")))
			return false;
		// check email is not empty
		if(!(txtEmail.getText().contains("@")) || txtEmail.getText().length() < 1)
			return false;
		// check phone is a number (currently not letters)
		if(txtPhoneNumber.getText().matches("^[a-zA-Z]*$"))
			return false;
		
		return valid;
	}

	@FXML
	public void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), 
				"/gui/EktDivisionManagerHomePage", null, "Ekt Division Manager", true);
		primaryStage.show();
		((Stage) ((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
	}

}
