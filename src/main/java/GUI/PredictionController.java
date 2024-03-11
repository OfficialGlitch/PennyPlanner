package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

//import javax.swing.*;

public class PredictionController {

    @FXML
    TextField Years;

    @FXML
    TextField Months;

    @FXML
    TextField Interest;

    @FXML
    TextField Amount;

    @FXML
    TextField MonthlyAdd;

    @FXML
    private javafx.scene.chart.LineChart<String, Number> LineChart;

		@FXML
		private CategoryAxis xAxis;

    @FXML
    public void initialize() {
        // Set tfActualSavings non-editable
//		tfActualSavings.setEditable(false);
    }


    @FXML
    Label lblSavingsGoalStatus;

    @FXML
    public void initialize2() {
        // Initialize the label color to black
        lblSavingsGoalStatus.setStyle("-fx-text-fill: black;");
    }

    @FXML
    void calculateCompound(ActionEvent event) {
        try {

					xAxis.setAutoRanging(true);


						double principal = Double.parseDouble(Amount.getText()); // Initial principal balance
            double annualInterestRate = Double.parseDouble(Interest.getText()); // Annual interest rate (5%)
            int years = Integer.parseInt(Years.getText()); // Number of years
            int months = Integer.parseInt(Months.getText()); // Number of years

            double monthlyAddition = Double.parseDouble(MonthlyAdd.getText());; // Monthly addition to the principal balance

            double r = annualInterestRate / 12; // Monthly interest rate

            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
						series1.setName("Compound");
						double futureValue = principal;
            for (int i=0;i<years * 12 + months; i++){
                futureValue = futureValue * (1 + r) + monthlyAddition;


                series1.getData().add(new XYChart.Data<>(String.valueOf(i), futureValue));

            }
            LineChart.getData().add(series1);




//			System.out.printf("Future value: $%.2f", futureValue);


            // Check if savings goal is reached
//			double savingsGoal = Double.parseDouble(tfSavingsGoal.getText());
//			if (savings >= savingsGoal) {
//				// Set label text to indicate savings goal reached and set color to green
//				lblSavingsGoalStatus.setText("Savings Goal Reached!");
//				lblSavingsGoalStatus.setStyle("-fx-text-fill: green;");
//			} else {
//				// Set label text to indicate savings goal not reached and set color to red
//				lblSavingsGoalStatus.setText("Savings Goal Not Reached");
//				lblSavingsGoalStatus.setStyle("-fx-text-fill: red;");
//			}
        } catch (NumberFormatException e) {
//			Double.parseDouble(Amount.setText("Invalid input");
            System.out.println("Invalid Input");
        }
    }
}
