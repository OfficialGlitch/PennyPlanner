package GUI;

import GUI.util.ExpenseTreeTableItem;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import models.Category;
import models.TimePeriod;
import models.instances.ExpenseInstance;
import models.instances.IncomeInstance;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class SavingspageController {
	@FXML
    public TextField tfSavingsGoal;

	@FXML
	public Label lblIncomeAmount;

	@FXML
	public Label lblTotalExpenses;

	@FXML
	public Label lblActualSavings;

	@FXML
	public Label lblSavingsGoalStatus;

	@FXML
	private BarChart<String, Number> barChart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

private TimePeriod timePeriod;

	public void setup(TimePeriod tp) {
		this.timePeriod = tp;
		initialize();
		setupData();
		barChart.getData().clear();
	}

	private void setupData() {
		List<Category> categories;
		List<TreeItem<ExpenseTreeTableItem>> items = new ArrayList<>();
		double totalExpenses = 0;
		double totalIncome = 0;

		try (Session s = App.sf().openSession()) {
			categories = s.createNamedQuery("getAllCategories", Category.class)
				.setParameter("user", App.getCurrentUser().getID()).getResultList();
		}

		for (Category cat : categories) {
			System.out.println(cat);
			 //String names = cat.name.getValue();

				List<ExpenseInstance> expenseInstance = App.s().createNamedQuery("getExpenseInstancesForCategory", ExpenseInstance.class)
					.setParameter("tp", timePeriod.ID.get()).setParameter("cat", cat.ID.get()).getResultList();
				System.out.println(this.timePeriod.getMonth());
				for (ExpenseInstance ei : expenseInstance) {

						//String insuranceName = ei.name();
						double insuranceCost = ei.cost.get();


					totalExpenses += insuranceCost;
				}
		}
		List<IncomeInstance> incomes = App.s().createNamedQuery("getIncomesForTimePeriod", IncomeInstance.class)
			.setParameter("month", timePeriod.getID())
			.setParameter("user", App.getCurrentUser().getID()).getResultList();
		System.out.println(this.timePeriod.getMonth());
		for (IncomeInstance i : incomes){

				double incomegot = i.getAmount();
			  totalIncome += incomegot;


		}
		    textChange(lblIncomeAmount, Double.toString(totalIncome));
		    textChange(lblTotalExpenses, Double.toString(totalExpenses));
		    calculateSavings(totalIncome, totalExpenses);
	}


	public void calculateSavings(double income, double expense) {
		try {
			double curincome = income;
			double curexpense = expense;
			double savings = curincome - curexpense;
			lblActualSavings.setText(String.format("%.2f", savings));

			// Update the bar chart
			updateBarChart(savings);

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
			lblActualSavings.setText("Invalid input");
		}
	}
	@FXML
	private void handleCalculateAction(ActionEvent event) {
		// This method is called when the Calculate button is clicked.
		// Assuming the setupData method calculates and shows the total income and total expenses,
		// and you want to calculate and compare the savings upon clicking the calculate button.
		setupData(); // This will calculate the totals and display them.
	}
	private void updateBarChart(double savings) {
		// Clear existing data
		barChart.getData().clear();

		// Create series for the bar chart
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.getData().add(new XYChart.Data<>("Savings Goal", Double.parseDouble(tfSavingsGoal.getText())));
		series.getData().add(new XYChart.Data<>("Actual Savings", savings));


		// Add series to the bar chart
		barChart.getData().add(series);
	}

	@FXML
	public void textChange(Label label, String text){
		label.setText(text);
	}

	@FXML
	private void initialize() {

	}


}