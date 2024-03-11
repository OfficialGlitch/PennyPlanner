package GUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SavingspageController {
	@FXML
	private TextField tfSavingsGoal;

	@FXML
	private TextField tfIncomeAmount;

	@FXML
	private TextField tfTotalExpenses;

	@FXML
	private TextField tfActualSavings;

	@FXML
	private Label lblSavingsGoalStatus;

	@FXML
	private BarChart<String, Number> barChart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

//    @FXML
//    private void initialize1() {
//        // Set the bar chart axes
//        xAxis.setLabel("Category");
//        yAxis.setLabel("Value");
//
//        // Set the bar chart title (if needed)
//         barChart.setTitle("Savings Analysis");
//    }

	@FXML
	private void initialize() {
		// Set tfActualSavings non-editable
		tfActualSavings.setEditable(false);
	}

	@FXML
	private void calculateSavings(ActionEvent event) {
		try {
			double income = Double.parseDouble(tfIncomeAmount.getText());
			double expenses = Double.parseDouble(tfTotalExpenses.getText());
			double savings = income - expenses;
			tfActualSavings.setText(String.format("%.2f", savings));

			// Update the bar chart
			updateBarChart(income, expenses, savings);

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

	private void updateBarChart(double income, double expenses, double savings) {
		// Clear existing data
		barChart.getData().clear();

		// Create series for the bar chart
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.getData().add(new XYChart.Data<>("Savings Goal", Double.parseDouble(tfSavingsGoal.getText())));
		series.getData().add(new XYChart.Data<>("Actual Savings", savings));


		// Add series to the bar chart
		barChart.getData().add(series);
	}
}