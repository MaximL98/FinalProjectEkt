package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class TestScrollbarController {
	@FXML
	private Pane topPane;
	
	@FXML
	private Pane bottomPane;
	
	
	@FXML
	private BorderPane mainBorderPane;
	
	@FXML
	public void initialize() {
		VBox productsVbox = new VBox();
		Rectangle rectangle = new Rectangle();
		rectangle.setStyle("-fx-background-color: RED;");
		Rectangle rectangle1 = new Rectangle();
		rectangle1.setStyle("-fx-background-color: BLUE;");
		Rectangle rectangle2 = new Rectangle();
		rectangle2.setStyle("-fx-background-color: RED;");
		Rectangle rectangle3 = new Rectangle();
		rectangle3.setStyle("-fx-background-color: BLUE;");
		Rectangle rectangle4 = new Rectangle();
		rectangle4.setStyle("-fx-background-color: RED;");
		
		rectangle.setHeight(200);
		
		productsVbox.getChildren().add(rectangle);
		productsVbox.getChildren().add(rectangle1);
		productsVbox.getChildren().add(rectangle2);
		productsVbox.getChildren().add(rectangle3);
		productsVbox.getChildren().add(rectangle4);
		
		
		
		
		ScrollPane centerScrollBar = new ScrollPane(productsVbox);
		centerScrollBar.setPrefHeight(600);
		centerScrollBar.setPrefWidth(20);
		
		mainBorderPane.setCenter(centerScrollBar);
		
	}
	
	@FXML
	public void getBtnBack(ActionEvent event) {
		
	}
}
