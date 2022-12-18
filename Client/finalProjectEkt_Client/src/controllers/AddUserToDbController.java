package controllers;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import logic.SystemUser;

public class AddUserToDbController {

    @FXML
    private Button btnConnect;

    @FXML
    private TextField creditCardTxt;

    @FXML
    private TextField emailTxt;

    @FXML
    private TextField firstNameTxt;

    @FXML
    private TextField lastNameTxt;

    @FXML
    private TextField passwordTxt;

    @FXML
    private TextField phoneNumberTxt;

    @FXML
    private TextField userIdTxt;

    @FXML
    private TextField usernameTxt;

	@FXML Text statusLabel;

    @FXML
    void getAddUserToDB(ActionEvent event) {
    	int id;
		statusLabel.setText("Checking input");
    	SCCP preparedMessage = new SCCP();
    	if(validFieldInput()) {
    		id = Integer.valueOf(userIdTxt.getText());
    		// set message accordingly
    		preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
    		// first field is table name - users here
    		Object[] fill = new Object[3];
    		fill[0] = "systemusers"; // add to table "systemusers" (hard code it elsewhere)
    		fill[1] = false; // add only 1
    		fill[2] = new Object[] {new SystemUser(firstNameTxt.getText(), lastNameTxt.getText(), id, 
    				phoneNumberTxt.getText(), emailTxt.getText(), creditCardTxt.getText(), usernameTxt.getText(), passwordTxt.getText())};
    		preparedMessage.setMessageSent(fill);
    		
    		// send to server
    		ClientUI.clientController.accept(preparedMessage);
    		
    		// check comm for answer:
    		// left this for the class to do at home
    		// fuck nvm
    		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
    			// good
    			// add more tests but fuck it
    			statusLabel.setText("Successfully added a client!");
    		}
    		else {
    			statusLabel.setText("ERROR!");
    		}
    		
    	}
    	else {
    		statusLabel.setText("Status: Invalid input");
    	}
    }

	private boolean validFieldInput() {
		boolean valid = true;
		// check username and password not empty:
		if(usernameTxt.getText().length() < 1 || passwordTxt.getText().length() < 1)
			return false;
		// check name is letters
		if(firstNameTxt.getText().length() < 2 || !(firstNameTxt.getText().matches("^[a-zA-Z]*$")))
			return false;
		// check email is not empty
		if(emailTxt.getText().length() < 1)
			return false;
		// check credit-card is numeric and "-" only
		if(!(creditCardTxt.getText().matches("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])"
				+ "[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$")))
			return false;
		// check phone is a number (???)
		if(phoneNumberTxt.getText().matches("^[a-zA-Z]*$"))
			return false;
		// check what the fuck why do we need credit card here?
		
		return valid;
	}

}
