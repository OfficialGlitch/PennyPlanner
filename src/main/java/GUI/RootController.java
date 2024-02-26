package GUI;

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
		setupData();
		initialize();

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

				for (ExpenseInstance eI : expenseInstances){
					String investmentName = eI.name();

					addDataToPieChart(investmentName, eI.cost.get(), "E");

					InvestmentTotal += eI.cost.get();
				}
			}
		}
		addPointToLineChart(timePeriod.toString(), InvestmentTotal, "E");
		textChange(label1, Double.toString(InvestmentTotal));
	}



	public void addDataToPieChart(String category, double value, String colour) {
		XYChart.Series<String, Number> series2 = new XYChart.Series<>();
		series2.getData().add(new XYChart.Data<>(category, value));
		series2.setName("Holdings");
		BarChart.getData().add(series2);
//
	}

	public void addPointToLineChart(String xValue, Number yValue, String type) {
		if (type.equals("E")){
			XYChart.Series<String, Number> expensesSeries = (XYChart.Series<String, Number>) LineChart.getData().get(0);
			expensesSeries.getData().add(new XYChart.Data<>(xValue, yValue));

			XYChart.Data<String, Number> newDataPoint = expensesSeries.getData().get(expensesSeries.getData().size() - 1);
			expensesSeries.getNode().setStyle("-fx-stroke: transparent;");
			newDataPoint.getNode().setStyle("-fx-background-color: red;");


			//expensesSeries.getNode().setStyle("-fx-stroke: red;");
		}else {
			XYChart.Series<String, Number> incomeSeries = (XYChart.Series<String, Number>) LineChart.getData().get(0);
			incomeSeries.getData().add(new XYChart.Data<>(xValue, yValue));

			XYChart.Data<String, Number> newDataPoint = incomeSeries.getData().get(incomeSeries.getData().size() - 1);
			newDataPoint.getNode().setStyle("-fx-background-color: green;");

			//incomeSeries.getNode().setStyle("-fx-stroke: green;");
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
        series1.setName("Gains");
        series1.getData().add(new XYChart.Data<>("0", 0));
//        series1.getData().add(new XYChart.Data<>("2", 300));
//        series1.getData().add(new XYChart.Data<>("3", 400));
        LineChart.getData().add(series1);

        // Bar Chart
//        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
//        series2.setName("Holdings");
//        BarChart.getData().add(series2);
    }

}
