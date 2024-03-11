package GUI;

import GUI.util.ExpenseTreeTableItem;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import models.Category;
import models.TimePeriod;
import models.instances.ExpenseInstance;
import models.instances.IncomeInstance;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SummaryPageController implements Initializable{
	@FXML
	public LineChart LineGraph;
	@FXML
	public PieChart PieChart;

	public XYChart.Series<String, Number> income;
	public XYChart.Series<String, Number> expenses;
	private TimePeriod timePeriod;

	public void setup(TimePeriod tp) {
		this.timePeriod = tp;
		initialize(null, null);
		setupData();
	}

	private void setupData(){
//		TimePeriod timePeriod1 = App.s().createNamedQuery("findTimePeriod", TimePeriod.class)
//			.setParameter("month", 1) // replace this with the current month
//			.setParameter("year", 2024) // replace this with the current year
//			.setParameter("uid", App.getCurrentUser().getID()).getSingleResultOrNull();
//
//		// get list of expenses for time period
//		List<ExpenseInstance> results = App.s().createNamedQuery("getExpensesForTimePeriod", ExpenseInstance.class)
//			.setParameter("month", timePeriod1.getID())
//			.setParameter("user", App.getCurrentUser().getID())
//			.getResultList();
//
//		// get list of incomes for time period
//		List<IncomeInstance> incomeResults = App.s().createNamedQuery("getIncomesForTimePeriod", IncomeInstance.class)
//			.setParameter("month", timePeriod1.getID())
//			.setParameter("user", App.getCurrentUser().getID()).getResultList();

		List<Category> categories;
		List<TreeItem<ExpenseTreeTableItem>> items = new ArrayList<>();

		double totalExpenses = 0;

		try (Session s = App.sf().openSession()) {
			categories = s.createNamedQuery("getAllCategories", Category.class)
				.setParameter("user", App.getCurrentUser().getID()).getResultList();
//					s.refresh(timePeriod);
		}

		for (Category cat : categories){
			String name = cat.name.getValue();
			double categoryTotal = 0;

			List<ExpenseInstance> expenseInstances = App.s().createNamedQuery("getExpenseInstancesForCategory", ExpenseInstance.class)
				.setParameter("tp", timePeriod.ID.get()).setParameter("cat", cat.ID.get()).getResultList();

			for (ExpenseInstance eI : expenseInstances){
				categoryTotal += eI.cost.get();
			}
			if (categoryTotal != 0){
				addPointToLineChart(name, categoryTotal, "E");
				addDataToPieChart(name, categoryTotal, "E");
			}

		}

		List<IncomeInstance> incomes = App.s().createNamedQuery("getIncomesForTimePeriod", IncomeInstance.class)
			.setParameter("month", timePeriod.getID())
			.setParameter("user", App.getCurrentUser().getID()).getResultList();

		for (IncomeInstance i : incomes){
			if (i.getAmount() != 0){
				String name = i.getIncomeSource().getName();
				addPointToLineChart(name, i.getAmount(), "I");
				addDataToPieChart(name, i.getAmount(), "I");
			}
		}
	}

	public void setup(URL url, ResourceBundle bundle) {
		//LineGraph
		expenses = new XYChart.Series<>();
		expenses.setName("Expenses");
		LineGraph.getData().clear();
		LineGraph.getData().add(expenses);

		income = new XYChart.Series<>();
		income.setName("Income");
		LineGraph.getData().add(income);

		LineGraph.setLegendVisible(false);
		PieChart.setLegendVisible(false);
	}

	public void addPointToLineChart(String xValue, Number yValue, String type) {
		if (type.equals("E")){
			XYChart.Series<String, Number> expensesSeries = (XYChart.Series<String, Number>) LineGraph.getData().get(0);
			expensesSeries.getData().add(new XYChart.Data<>(xValue, yValue));

			XYChart.Data<String, Number> newDataPoint = expensesSeries.getData().get(expensesSeries.getData().size() - 1);
			expensesSeries.getNode().setStyle("-fx-stroke: transparent;");
			newDataPoint.getNode().setStyle("-fx-background-color: red;");

			Tooltip tooltip = new Tooltip(xValue + ", : -$" + yValue);
			Tooltip.install(newDataPoint.getNode(), tooltip);

			//expensesSeries.getNode().setStyle("-fx-stroke: red;");
		}else {
			XYChart.Series<String, Number> incomeSeries = (XYChart.Series<String, Number>) LineGraph.getData().get(0);
			incomeSeries.getData().add(new XYChart.Data<>(xValue, yValue));

			XYChart.Data<String, Number> newDataPoint = incomeSeries.getData().get(incomeSeries.getData().size() - 1);
			newDataPoint.getNode().setStyle("-fx-background-color: green;");

			Tooltip tooltip = new Tooltip(xValue + ", : +$" + yValue);
			Tooltip.install(newDataPoint.getNode(), tooltip);
			//incomeSeries.getNode().setStyle("-fx-stroke: green;");
		}
	}

	// Method to add data to the PieChart
	public void addDataToPieChart(String category, double value, String colour) {
		ObservableList<PieChart.Data> pieChartData = PieChart.getData();
		PieChart.Data data = new PieChart.Data(category, value);
		pieChartData.add(data);
//        PieChart.Data slice1 = new PieChart.Data(category, value);
//
		if (colour.equals("E")){
			data.getNode().setStyle("-fx-pie-color: #ff0000;"); // Red
		}else {
			data.getNode().setStyle("-fx-pie-color: #00ff00;"); // Green
		}
//
//        PieChart.getData().addAll(slice1);
	}

	public void addStubData() {
//        Database d = new Database();

		// EXAMPLE FUNCTIONS

		// Adding Person 1
		ArrayList<Object> added = new ArrayList<Object>();

		added.add(1);added.add("JOHN");added.add("password");

		ArrayList<Object> Expenses = new ArrayList<Object>();
		Expenses.add(20.1);Expenses.add(40.0);Expenses.add(30.0);Expenses.add(10.0);added.add(Expenses);

		ArrayList<Object> Incomes = new ArrayList<Object>();
		Incomes.add(100.0);Incomes.add(120.0);added.add(Incomes);

		ArrayList<Object> Investments = new ArrayList<Object>();
		Investments.add(100.1);Investments.add(200.1);added.add(Investments);

//        d.addPerson(added);

		addPointToLineChart("Jan", (double)Expenses.get(0), "E");
		addPointToLineChart("Feb", (double)Expenses.get(1), "E");
		addPointToLineChart("Mar", (double)Expenses.get(2), "E");
		addPointToLineChart("Apr", (double)Expenses.get(3), "E");

		addPointToLineChart("Jan", (double)Incomes.get(0), "I");
		addPointToLineChart("Feb", 0, "I");
		addPointToLineChart("Mar", (double)Incomes.get(1), "I");
		addPointToLineChart("Apr", 0, "I");

		addDataToPieChart("Total Expenses: " + (double)Expenses.get(0), (double)Expenses.get(0), "E");
		addDataToPieChart("Total Income: " + (double)Incomes.get(0), (double)Incomes.get(0), "I");

		runToolTip();
	}

	private void runToolTip(){
		for (XYChart.Data<String, Number> data : expenses.getData()) {
			Tooltip tooltip = new Tooltip("Month: " + data.getXValue() + ", Amount: " + data.getYValue());
			Tooltip.install(data.getNode(), tooltip);
		}
		for (XYChart.Data<String, Number> data : income.getData()) {
			Tooltip tooltip = new Tooltip("Month: " + data.getXValue() + ", Amount: " + data.getYValue());
			Tooltip.install(data.getNode(), tooltip);
		}
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

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		//LineGraph
		expenses = new XYChart.Series<>();
		expenses.setName("Expenses");
		LineGraph.getData().clear();
		LineGraph.getData().add(expenses);

		income = new XYChart.Series<>();
		income.setName("Income");
		LineGraph.getData().add(income);

		if (timePeriod != null){
			String month = null;
			int monthNum = timePeriod.getMonth();
			if (monthNum == 1){
				month = "January";
			}else if (monthNum == 2){
				month = "February";
			}else if (monthNum == 3){
				month = "March";
			}else if (monthNum == 4){
				month = "April";
			}else if (monthNum == 5){
				month = "May";
			}else if (monthNum == 6){
				month = "June";
			}else if (monthNum == 7){
				month = "July";
			}else if (monthNum == 8){
				month = "August";
			}else if (monthNum == 9){
				month = "September";
			}else if (monthNum == 10){
				month = "October";
			}else if (monthNum == 11){
				month = "November";
			}else if (monthNum == 12){
				month = "December";
			}

			PieChart.getData().clear();
			PieChart.titleProperty().set("Spending for: " + month);
		}


		LineGraph.setLegendVisible(false);
		PieChart.setLegendVisible(false);
	}
}
