package project;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SavingsCalculator extends Application {

	private TextField tfIncome = new TextField();
	private TextField tfExpenses = new TextField();
	private TextField tfSavings = new TextField();
	private Button btnCalculate = new Button("Calculate");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setAlignment(Pos.CENTER);

		gridPane.add(new Label("Income:"), 0, 0);
		gridPane.add(tfIncome, 1, 0);

		gridPane.add(new Label("Expenses:"), 0, 1);
		gridPane.add(tfExpenses, 1, 1);

		gridPane.add(new Label("Savings:"), 0, 2);
		gridPane.add(tfSavings, 1, 2);

		gridPane.add(btnCalculate, 1, 3);
		GridPane.setHalignment(btnCalculate, HPos.RIGHT);

		btnCalculate.setOnAction(e -> calculateSavings());

		Scene scene = new Scene(gridPane, 300, 200);
		primaryStage.setTitle("Savings Calculator");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void calculateSavings() {
		try {
			double income = Double.parseDouble(tfIncome.getText());
			double expenses = Double.parseDouble(tfExpenses.getText());
			double savings = Savings.calculateSavings(income, expenses);
			tfSavings.setText(String.valueOf(savings));
		} catch (NumberFormatException ex) {
			tfSavings.setText("Invalid input");
		}
	}
}
