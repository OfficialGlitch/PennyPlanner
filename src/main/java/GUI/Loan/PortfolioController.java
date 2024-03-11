package GUI.Loan;

import GUI.App;
import GUI.ExpenseTableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import models.TimePeriod;
import models.money.User;

import javafx.scene.control.TreeItem;

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
    private TextFlow textFlow1;

    @FXML
    private TextFlow textFlow2;

    @FXML
    private Label label1;

    @FXML
    private Label label2;


    @FXML
    private Button BackButton;

    @FXML
    private Button button;

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
		setupData();

	}
	private void setupData(){


		List<Category> categories;
		List<TreeItem<ExpenseTreeTableItem>> items = new ArrayList<>();

		double totalExpenses = 0;

		try (Session s = App.sf().openSession()) {
			categories = s.createNamedQuery("getAllCategories", Category.class)
				.setParameter("user", App.getCurrentUser().getID()).getResultList();
//					s.refresh(timePeriod);
		}
		double InvestmentTotal = 0;

		for (Category cat : categories){
			System.out.println(cat);
			String name = cat.name.getValue();
			if(name.equals("Investments")){
				List<ExpenseInstance> expenseInstances = App.s().createNamedQuery("getExpenseInstancesForCategory", ExpenseInstance.class)
					.setParameter("tp", timePeriod.ID.get()).setParameter("cat", cat.ID.get()).getResultList();
				System.out.println(this.timePeriod.getMonth());
				for (ExpenseInstance eI : expenseInstances){
					String investmentName = eI.name();

					addDataToPieChart(investmentName, eI.cost.get(), "E");

					InvestmentTotal += eI.cost.get();
				}
			}
		}
		addPointToLineChart(String.valueOf(this.timePeriod.getMonth()), InvestmentTotal, "E");
		textChange(label1, Double.toString(InvestmentTotal));
	}



	public void addDataToPieChart(String category, double value, String colour) {
		XYChart.Series<String, Number> series2 = new XYChart.Series<>();
		series2.getData().add(new XYChart.Data<>(category, value));
		series2.setName("Holdings");
		BarChart.getData().add(series2);
		BarChart.setLegendVisible(false);
//
	}

	public void addPointToLineChart(String xValue, Number yValue, String type) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.getData().add(new XYChart.Data<>(xValue, yValue));
		LineChart.getData().add(series);


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
			}
		}

	@FXML
	public void textChange(Label label, String text){
		label.setText(text);
	}

    @FXML
    private void initialize() {

//        label1.setText(InvestmentTita);
        label2.setText("9000");


        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
				LineChart.setLegendVisible(false);
        series1.setName("Gains");
        series1.getData().add(new XYChart.Data<>("0", 0));
        LineChart.getData().add(series1);

        // Bar Chart
//        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
//        series2.setName("Holdings");
//        BarChart.getData().add(series2);
    }

}
