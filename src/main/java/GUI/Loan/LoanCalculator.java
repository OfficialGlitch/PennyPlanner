package GUI.Loan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoanCalculator extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(LoanCalculator.class.getResource("loan_calculator.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 800, 600);
		stage.setTitle("Loan Calculator");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
