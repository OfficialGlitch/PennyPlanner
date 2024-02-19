package GUI;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SummaryPageController implements Initializable {
    @FXML
    public LineChart LineGraph;
    @FXML
    public PieChart PieChart;

    public XYChart.Series<String, Number> income;
    public XYChart.Series<String, Number> expenses;

    public void initialize(URL url, ResourceBundle bundle) {
        //LineGraph
        expenses = new XYChart.Series<>();
        expenses.setName("Expenses");
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

            Tooltip tooltip = new Tooltip("X: " + newDataPoint.getXValue() + ", Y: " + newDataPoint.getYValue());
            Tooltip.install(newDataPoint.getNode(), tooltip);

            //expensesSeries.getNode().setStyle("-fx-stroke: red;");
        }else {
            XYChart.Series<String, Number> incomeSeries = (XYChart.Series<String, Number>) LineGraph.getData().get(0);
            incomeSeries.getData().add(new XYChart.Data<>(xValue, yValue));

            XYChart.Data<String, Number> newDataPoint = incomeSeries.getData().get(incomeSeries.getData().size() - 1);
            newDataPoint.getNode().setStyle("-fx-background-color: green;");

            Tooltip tooltip = new Tooltip("X: " + newDataPoint.getXValue() + ", Y: " + newDataPoint.getYValue());
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
}