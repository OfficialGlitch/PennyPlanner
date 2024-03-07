package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.TimePeriod;

import java.io.IOException;

//import javax.swing.*;

public class SavingspageController {

	@FXML
	TextField tfSavingsGoal;

	@FXML
	TextField tfIncomeAmount;

	@FXML
	TextField tfTotalExpenses;

	@FXML
	TextField tfActualSavings;

	@FXML
	public void initialize() {
		// Set tfActualSavings non-editable
		tfActualSavings.setEditable(false);
	}

	@FXML
	Label lblSavingsGoalStatus;

	@FXML
	public void initialize2() {
		// Initialize the label color to black
		lblSavingsGoalStatus.setStyle("-fx-text-fill: black;");
	}

	@FXML
	private void changePage(ActionEvent event) {
		event.consume();
		try {
			FXMLLoader loader = App.loadFXML("ExpenseTable");
			Parent p = loader.load();
			ExpenseTableController controller = loader.getController();
			controller.setFields(TimePeriod.generateNewMonth());
			App.setCurrentScene(p);
		} catch(IOException err) {
			System.err.println("Couldn't change scene: " + err.toString());
			err.printStackTrace();
			return;
		}
	}

	@FXML
	void calculateSavings(ActionEvent event) {
		try {
			double income = Double.parseDouble(tfIncomeAmount.getText());
			double expenses = Double.parseDouble(tfTotalExpenses.getText());
			double savings = income - expenses;
			tfActualSavings.setText(String.format("%.2f", savings));

			// Check if savings goal is reached
			double savingsGoal = Double.parseDouble(tfSavingsGoal.getText());
			if (savings >= savingsGoal) {
				// Set label text to indicate savings goal reached and set color to green
				lblSavingsGoalStatus.setText("Savings Goal Reached!");
				lblSavingsGoalStatus.setStyle("-fx-text-fill: green;");
			} else {
				// Set label text to indicate savings goal not reached and set color to red
				lblSavingsGoalStatus.setText("Savings Goal Not Reached");
				lblSavingsGoalStatus.setStyle("-fx-text-fill: red;");
			}
		} catch (NumberFormatException e) {
			tfActualSavings.setText("Invalid input");
		}
	}
}
