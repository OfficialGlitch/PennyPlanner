package GUI.Loan;

import GUI.App;
import GUI.ExpenseTableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import models.TimePeriod;
import models.money.User;

import models.Category;
import models.TimePeriod;
import models.instances.ExpenseInstance;
import org.hibernate.Session;
import GUI.util.ExpenseTreeTableItem;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PortfolioController {

    @FXML
    private TextField fromField;

    @FXML
    private TextField toField;

    @FXML
    private Label label1;

    @FXML
    private Button dateBtn;

    @FXML
    private LineChart<String, Number> LineChart;

    @FXML
    private BarChart<String, Number> BarChart;


	private TimePeriod timePeriod;

	public void setup(TimePeriod tp) {
		this.timePeriod = tp;
		LineChart.getData().clear();
		BarChart.getData().clear();
		initialize();
		setupData(tp, 0);

	}
	private double setupData(TimePeriod tp, double previous){

		List<Category> categories;

		try (Session s = App.sf().openSession()) {
			categories = s.createNamedQuery("getAllCategories", Category.class)
				.setParameter("user", App.getCurrentUser().getID()).getResultList();
		}
		double InvestmentTotal = 0;

		for (Category cat : categories){
			String name = cat.name.getValue();
			if(name.equals("Investments")){
				List<ExpenseInstance> expenseInstances = App.s().createNamedQuery("getExpenseInstancesForCategory", ExpenseInstance.class)
					.setParameter("tp", tp.ID.get()).setParameter("cat", cat.ID.get()).getResultList();
				for (ExpenseInstance eI : expenseInstances){
					String investmentName = eI.name();

					addDataToBarChart(investmentName, eI.cost.get());

					InvestmentTotal += eI.cost.get();
				}
			}
		}
		addPointToLineChart(String.valueOf(tp.getMonth()), InvestmentTotal+previous);
		textChange(label1, Double.toString(InvestmentTotal));
		return InvestmentTotal;
	}



	public void addDataToBarChart(String category, double value) {
		XYChart.Series<String, Number> series2 = new XYChart.Series<>();
		series2.getData().add(new XYChart.Data<>(category, value));
		series2.setName("Holdings");
		BarChart.getData().add(series2);
		BarChart.setLegendVisible(false);
	}

	public void addPointToLineChart(String xValue, Number yValue) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.getData().add(new XYChart.Data<>(xValue, yValue));
		LineChart.getData().add(series);

	}

	@FXML
	public void textChange(Label label, String text){
		label.setText(text);
	}

    @FXML
    private void initialize() {

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
				LineChart.setLegendVisible(false);
        series1.setName("Gains");
        series1.getData().add(new XYChart.Data<>("0", 0));
        LineChart.getData().add(series1);
    }
	@FXML
	public void changeDate(){
		LineChart.getData().clear();
		BarChart.getData().clear();

		int from = Integer.parseInt(fromField.getText());
		int to = Integer.parseInt(toField.getText());

		initialize();
		double totalInvestment = 0;
		for(int i=from; i<=to; i++){
			TimePeriod tp = TimePeriod.generateNewMonth((int) i, 2024);
			double monthlyInvestment = setupData(tp, totalInvestment);
			totalInvestment += monthlyInvestment;
		}
		textChange(label1, Double.toString(totalInvestment));
	}
}
