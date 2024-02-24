package models.money;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Savingspage extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("Savingspage.fxml"));
		Scene scene = new Scene(root, Color.WHITE);
		primaryStage.setTitle("Savings Calculator");
		primaryStage.setWidth(523);
		primaryStage.setHeight(340);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
