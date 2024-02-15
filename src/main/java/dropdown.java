import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class dropdown {



	public class Drowpdown extends Application  {

		public void start(Stage primaryStage) {
			primaryStage.setTitle("Dropdown Menu");

			ComboBox<String> comboBox = new ComboBox<>();

			comboBox.getItems().add("Expenses");
			comboBox.getItems().add("Savings");
			comboBox.getItems().add("Summary");

			HBox hbox = new HBox(comboBox);

			Scene scene = new Scene(hbox, 200, 120);
			primaryStage.setScene(scene);
			primaryStage.show();
		}

		public static void main(String[] args) {
			Application.launch(args);
		}
	}}
