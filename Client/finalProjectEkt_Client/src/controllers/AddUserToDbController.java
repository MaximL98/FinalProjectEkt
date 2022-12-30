package controllers;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
    public void initialize() {
        System.out.println("TEST: initializing AddUserToDB page.");
        // validate text field inputs:
        // TODO:
        /*
     // fired by every text property changes
        firstNameTxt.textProperty().addListener(
          (observable, oldValue, newValue) -> {
            // Your validation rules, anything you like
            // (! note 1 !) make sure that empty string (newValue.equals("")) 
            //   or initial text is always valid
            //   to prevent inifinity cycle
            // do whatever you want with newValue
        	  if(!isValidName(newValue))
            // If newValue is not valid for your rules
        		  ((StringProperty)observable).setValue(oldValue);
            // (! note 2 !) do not bind textProperty (textProperty().bind(someProperty))
            //   to anything in your code.  TextProperty implementation
            //   of StringProperty in TextFieldControl
            //   will throw RuntimeException in this case on setValue(string) call.
            //   Or catch and handle this exception.

            // If you want to change something in text
            // When it is valid for you with some changes that can be automated.
            // For example change it to upper case
            
            ((StringProperty)observable).setValue(newValue.toUpperCase());
          }
        );
        */
        
        
        /*firstNameTxt.setTextFormatter(new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (!isValidName(text)) { // your validation logic
                return null;
            }
            return change;
        })
        );
        
        lastNameTxt.setTextFormatter(new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (!isValidName(text)) { // your validation logic
                return null;
            }
            return change;
        })
        );
        // validate ID:
        userIdTxt.setTextFormatter(new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (!isValidID(text)) { // your validation logic
                return null;
            }
            return change;
        })
        );*/

        
    }
	
    private boolean isValidID(String idString) {
		// TODO - this is a shit way to handle this
		return idString.length() < 9 && idString.toUpperCase().equals(idString.toLowerCase());
	}

	private boolean isValidName(String name) {
		return name.equals("") || name.length() > 1;
	}

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
    		fill[0] = "systemuser"; // add to table "systemusers" (hard code it elsewhere)
    		fill[1] = false; // add only 1
    		fill[2] = new Object[] {new SystemUser(id, firstNameTxt.getText(), lastNameTxt.getText(), 
    				phoneNumberTxt.getText(), emailTxt.getText(), creditCardTxt.getText(), usernameTxt.getText(), passwordTxt.getText())};
    		preparedMessage.setMessageSent(fill);
    		
    		// send to server
    		ClientUI.clientController.accept(preparedMessage);
    		
    		// check comm for answer:
    		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
    			// add test that response.messageSent is the array we had in fill[2] (SAME OBJECT)
    			statusLabel.setText("Successfully added a client!");
    		}
    		else {
    			statusLabel.setText("ERROR!"); // add specifics
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
		if(!(emailTxt.getText().contains("@")) || emailTxt.getText().length() < 1)
			return false;
		// check credit-card is legit (why?)
		if(!(creditCardTxt.getText().matches("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])"
				+ "[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$")))
			return false;
		// check phone is a number (currently not letters)
		if(phoneNumberTxt.getText().matches("^[a-zA-Z]*$"))
			return false;
		
		return valid;
	}

//	@FXML public void usernameFieldEvent(ActionEvent event) {}
//
//	@FXML public void passwordFieldEvent(ActionEvent event) {}
//
//	@FXML public void userIdFieldEvent(ActionEvent event) {}
//
//	@FXML public void fnameFieldEvent(ActionEvent event) {}
//
//	@FXML public void lnameFieldEvent(ActionEvent event) {}
//
//	@FXML public void phoneFieldEvent(ActionEvent event) {}
//
//	@FXML public void emailFieldEvent(ActionEvent event) {}
//
//	@FXML public void ccFieldEvent(ActionEvent event) {}

}
