package common;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class WindowStarter {
	
	// method to save lines - opens new window
	// CHANGE:
	// Stage does not call show here, Senjor!
	public static void createWindow(Stage primaryStage, Object classObject, 
			String fxmlAddress, String cssAddress, String windowTitle) 
					 {
		
		Parent root;
		try {
			root = FXMLLoader.load(classObject.getClass().getResource(fxmlAddress));
			Scene scene = new Scene(root);
			if(cssAddress != null)
				scene.getStylesheets().add(classObject.getClass().getResource(cssAddress).toExternalForm());
			Image image = new Image("/gui/ekrutServerManager.png");
			primaryStage.getIcons().add(image);
			primaryStage.setTitle(windowTitle);
			primaryStage.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	}
}
