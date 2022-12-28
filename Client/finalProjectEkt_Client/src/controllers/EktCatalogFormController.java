package controllers;

import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import client.ClientController;

public class EktCatalogFormController {

	 	@FXML
	    private Button btnCatalog1;

	    @FXML
	    private ImageView btnClatalog0_0;

	    @FXML
	    private Button btnCatalog2;

	    @FXML
	    private ImageView btnCatalog0_1;

	    @FXML
	    private Button btnCatalog3;

	    @FXML
	    private ImageView btnCatalog0_2;

	    @FXML
	    private Button btnCatalog4;

	    @FXML
	    private ImageView btnCatalog0_3;

	    @FXML
	    private Button btnCatalog7;

	    @FXML
	    private ImageView btnCatalog1_0;

	    @FXML
	    private Button btnCatalog8;

	    @FXML
	    private ImageView btnCatalog1_1;

	    @FXML
	    private Button btnCatalog9;

	    @FXML
	    private ImageView btnCatalog1_2;

	    @FXML
	    private Button btnCatalog10;

	    @FXML
	    private ImageView btnCatalog1_3;

	    @FXML
	    private TextField txtSearch;

	    @FXML
	    private Button btnCategory;

	    @FXML
	    private Button btnInfo;

	    @FXML
	    private Button btnLogout;

    
	String productFormFXMLLocation = "/gui/EktProductForm.fxml";

//  //start primary stage
//  		public void start(Stage primaryStage) throws Exception {
//  			WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog");
//  			primaryStage.show();	 	
//  	}
    
  	//Category 1
  		@FXML
  		private void getBtnCatalog0_0(ActionEvent event)  {
 
  			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
  			Stage primaryStage = new Stage();
  			String category = "Healthy";
  			ClientController.CurrentProductCategory = category;
  			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null, category);
  			primaryStage.setOnCloseRequest(we -> 
  			{
  				System.out.println("Pressed the X button."); 
  				System.exit(0);
  			});
  			primaryStage.show();
  		}
  		//Category 2
  		@FXML
  		private void getBtnCatalog0_1(ActionEvent event)  {
  			Stage primaryStage = new Stage();
  			String category = "Soft drinks";
  			ClientController.CurrentProductCategory = category;
  			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null, category);
  			primaryStage.show();
  		}
  		//Category 3
  		@FXML
  		private void getBtnCatalog0_2(ActionEvent event)  {
  			Stage primaryStage = new Stage();
  			String category = "Fruits";
  			ClientController.CurrentProductCategory = category;
  			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null, category);
  		}
  		//Category 4
  		@FXML
  		private void getBtnCatalog0_3(ActionEvent event)  {
  			Stage primaryStage = new Stage();
  			String category = "Vegetables";
  			ClientController.CurrentProductCategory = category;
  			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null, category);
  		}
  		//Category 5
  		@FXML
  		private void getBtnCatalog1_0(ActionEvent event)  {
  			Stage primaryStage = new Stage();
  			String category = "Snacks";
  			ClientController.CurrentProductCategory = category;
  			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null, category);
  		}
  		//Category 6
  		@FXML
  		private void getBtnCatalog1_1(ActionEvent event)  {
  			Stage primaryStage = new Stage();
  			String category = "Sandwiches";
  			ClientController.CurrentProductCategory = category;
  			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null, category);
  		}
  		//Category 7
  		@FXML
  		private void getBtnCatalog1_2(ActionEvent event)  {
  			
  			Stage primaryStage = new Stage();
  			String category = "Chewing gum";
  			ClientController.CurrentProductCategory = category;
  			System.out.println(ClientController.getCurrentSystemUser().getFirstName());
  			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null, category);
  		}
  		//Category 8
  		@FXML
  		private void getBtnCatalog1_3(ActionEvent event)  {
  			Stage primaryStage = new Stage();
  			String category = "Dairy";
  			ClientController.CurrentProductCategory = category;
  			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), productFormFXMLLocation, null, category);
  		}

  	    @FXML
  	    void getBtnLogout(ActionEvent event) {
  	    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
  			Stage primaryStage = new Stage();
  			WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login");
  			// this was done so that we can use this button
  			primaryStage.setOnCloseRequest(we -> 
  			{
  				System.out.println("Pressed the X button."); 
  				System.exit(0);
  			}
  			);
  			primaryStage.show();
  	   }
}
