package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

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
    void calculateCompound(ActionEvent event) {
        try {

					xAxis.setAutoRanging(true);


						double principal = Double.parseDouble(Amount.getText()); // Initial principal balance
            double annualInterestRate = Double.parseDouble(Interest.getText()); // Annual interest rate (5%)
            int years = Integer.parseInt(Years.getText()); // Number of years
            int months = Integer.parseInt(Months.getText()); // Number of years

            double monthlyAddition = Double.parseDouble(MonthlyAdd.getText()); // Monthly addition to the principal balance

            double r = annualInterestRate / 12; // Monthly interest rate

            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
						series1.setName("Compound");
						double futureValue = principal;
						// calculate compound
            for (int i=0;i<years * 12 + months; i++){
                futureValue = futureValue * (1 + r) + monthlyAddition;
                series1.getData().add(new XYChart.Data<>(String.valueOf(i), futureValue));
            }
            LineChart.getData().add(series1);

        } catch (NumberFormatException e) {
						showAlert("Invalid input", "Enter valid value");
        }
    }
	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
