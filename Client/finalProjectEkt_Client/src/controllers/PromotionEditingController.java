package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import common.InactivityChecker;
import common.WindowStarter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

public class PromotionEditingController implements Initializable{
  @FXML
  private TextField txtPromotionName;

  @FXML
  private TextField txtPromotionDescription;

  @FXML
  private ComboBox<String> cbLocation;
  
  @FXML
  private TextField txtProductId;

  @FXML
  private TextField txtDiscountPercentage;

  @FXML
  private DatePicker dpPromotionStartDate;

  @FXML
  private DatePicker dpPromotionEndDate;

  @FXML
  private Button btnCreatePromotion;

  @FXML
  private void createPromotionHandler() {
	  

    // Get the promotion details from the text fields and date pickers
    String promotionName = txtPromotionName.getText();
    String promotionDescription = txtPromotionDescription.getText();
    // Get the selected location from the ComboBox
    String storelocation = cbLocation.getValue();
    // Get the product ID from the TextField
    String productId = txtProductId.getText();
    int discountPercentage = Integer.parseInt(txtDiscountPercentage.getText());
    LocalDate startDate = dpPromotionStartDate.getValue();
    LocalDate endDate = dpPromotionEndDate.getValue();
 // Load the JDBC driver
  

    // Establish a connection to the database
    //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/mydatabase", "username", "password");

    // Create a PreparedStatement with the INSERT SQL statement
    //String sql = "INSERT INTO promotions (name, description, location, product_id, discount_percentage, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
   // PreparedStatement stmt = conn.prepareStatement(sql);

    // Set the values for the INSERT statement
//    stmt.setString(1, promotionName);
//    stmt.setString(2, promotionDescription);
//    stmt.setString(3, cbLocation.getValue());
//    stmt.setString(4, productId);
//    stmt.setInt(5, discountPercentage);
//    stmt.setDate(6, Date.valueOf(startDate));
//    stmt.setDate(7, Date.valueOf(endDate));
//
//    // Execute the INSERT statement
//    stmt.executeUpdate();
//
//    // Close the connection to the database
//    conn.close();

  }
  
  public void goBackHandler(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), "/gui/SalesManager.fxml", null, "Sales");
		primaryStage.show();
	}
@Override
public void initialize(URL location, ResourceBundle resources) {
	String [] items = {"North","South","United Arab Emirates"};
	cbLocation.getItems().addAll(items);
	}
	
}


