package GUI;

import GUI.util.ExpenseTreeTableItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.text.TextFlow;
import models.Category;
import models.TimePeriod;
import models.instances.ExpenseInstance;
import org.hibernate.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InsuranceController {

	@FXML
	private Label label1;

	@FXML
	private Label label2;

	@FXML
	private Label growthMessageLabel;

	@FXML
	private javafx.scene.chart.LineChart<String, Number> LineChart;

	@FXML
	private javafx.scene.chart.BarChart<String, Number> BarChart;

	@FXML
	private LineChart<String, Number> predictionLineChart;


	private TimePeriod timePeriod;

	public void setup(TimePeriod tp) {
		this.timePeriod = tp;
		LineChart.getData().clear();
		BarChart.getData().clear();
		predictionLineChart.getData().clear();
		initialize();
		setupData();

	}
	private void setupData() {
		List<Category> categories;
		List<TreeItem<ExpenseTreeTableItem>> items = new ArrayList<>();
		double totalInsurance = 0;

		try (Session s = App.sf().openSession()) {
			categories = s.createNamedQuery("getAllCategories", Category.class)
				.setParameter("user", App.getCurrentUser().getID()).getResultList();
		}

		for (Category cat : categories) {
			System.out.println(cat);
			String names = cat.name.getValue();
			if (names.equals("Insurance")) { // Filter for the "Insurance" category
				List<ExpenseInstance> expenseInstance = App.s().createNamedQuery("getExpenseInstancesForCategory", ExpenseInstance.class)
					.setParameter("tp", timePeriod.ID.get()).setParameter("cat", cat.ID.get()).getResultList();
				System.out.println(this.timePeriod.getMonth());
				for (ExpenseInstance ei : expenseInstance) {
					String insuranceName = ei.name(); // Assuming `name()` method gets the insurance name
					double insuranceCost = ei.cost.get(); // Assuming `cost` is the property for the expense amount

					// Add each insurance expense as a new data point in your charts
					addDataToPieChart(insuranceName, insuranceCost, "E"); // Assuming color "E" is correct for your use case

					totalInsurance += insuranceCost;
				}
			}
		}

		// Add total insurance cost to the line chart for the current period
		addPointToLineChart(String.valueOf(this.timePeriod.getMonth()), totalInsurance, "E");
		textChange(label1, Double.toString(totalInsurance)); // Update label1 with the total insurance cost
		addPredictionData(totalInsurance);
	}




	public void addDataToPieChart(String category, double value, String colour) {
		XYChart.Series<String, Number> series2 = new XYChart.Series<>();
		series2.getData().add(new XYChart.Data<>(category, value));
		series2.setName("Detailed Graph");
		BarChart.getData().add(series2);
		BarChart.setLegendVisible(false);
//
	}

	public void addPointToLineChart(String xValue, Number yValue, String type) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.getData().add(new XYChart.Data<>(xValue, yValue));
		LineChart.getData().add(series);


	}

	private void addPredictionData(double initialAmount) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Insurance Growth");

		double currentAmount = initialAmount;

		for (int month = 1; month <= 6; month++) {
			// Calculate the amount after applying 7% growth
			currentAmount = currentAmount * 1.07;

			// Add the data point to the series
			series.getData().add(new XYChart.Data<>(String.valueOf(month), currentAmount));
		}
		String message = String.format("Your %.2f will grow to %.2f after 6 months", initialAmount, currentAmount);
		growthMessageLabel.setText(message);
		growthMessageLabel.setStyle("-fx-text-fill: green;");
		// Add the series to the prediction chart
		predictionLineChart.getData().add(series);

	}


	@FXML
	public void textChange(Label label, String text){
		label.setText(text);
	}

	@FXML
	private void initialize() {

		label2.setText("7000");


		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		LineChart.setLegendVisible(false);
		series1.setName("Insurance Chart");
		series1.getData().add(new XYChart.Data<>("0", 0));
		LineChart.getData().add(series1);



	}

}
