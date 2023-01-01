package controllers;

import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class OrderReceiptPageController {
	@FXML
	private Button btnBack;

	@FXML
	private Button btnLogout;

	
	private static String EktEmailUsername = "EkrutShop@gmail.com";
	private static String EktEmailPassword = "dimarotemmaxim";
	
	public void initialize() {
		//////////IF WE HAVE TIME WE CAN DO IT. I THINK WE CAN DO IT BUT NOT USING A GOOGLE ACCOUNT!
		String CustomerEmail = "dimakislitsyn96@gmail.com";
		SendEmail(CustomerEmail);
	}

	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktCatalogForm.fxml",
				null, "Ekt Catalog");
		primaryStage.setOnCloseRequest(we -> {
			System.out.println("Pressed the X button.");
			System.exit(0);
		});
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
	}

	@FXML
	void getBtnLogout(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktSystemUserLoginForm.fxml",
				null, "Login");
		primaryStage.setOnCloseRequest(we -> {
			System.out.println("Pressed the X button.");
			System.exit(0);
		});
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
	}

	
	private void SendEmail(String CustomerEmail) {


	        final String username = EktEmailUsername;
	        final String password = EktEmailPassword;

	        Properties prop = new Properties();
	        
			prop.put("mail.smtp.host", "smtp.google.com");
	        prop.put("mail.smtp.port", "587");
	        prop.put("mail.smtp.auth", "true");
	        prop.put("mail.smtp.starttls.enable", "true"); //TLS
	     
	        Session session = Session.getInstance(prop,
	                new javax.mail.Authenticator() {
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(username, password);
	                    }
	                });

	        try {

	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(EktEmailUsername));
	            message.setRecipients(
	                    Message.RecipientType.TO,
	                    InternetAddress.parse(CustomerEmail)
	            );
	            message.setSubject("Testing Gmail TLS");
	            message.setText("Dear Mail Crawler,"
	                    + "\n\n Please do not spam my email!");

	            Transport.send(message);

	            System.out.println("Done");

	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    
	}
	
}
