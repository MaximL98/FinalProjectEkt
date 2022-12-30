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
		// check credit-card is legit (why?! we need to remove credit card from user (user needs ONLY user,pass, id, role!))
		if(false && !(txtCreditCard.getText().matches("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])"
				+ "[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$")))
			return false;
		// check phone is a number (currently not letters)
		if(txtPhoneNumber.getText().matches("^[a-zA-Z]*$"))
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
