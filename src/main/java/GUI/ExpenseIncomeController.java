package GUI;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import models.TimePeriod;

import java.net.URL;
import java.util.ResourceBundle;

public class ExpenseIncomeController implements Initializable {
	@FXML
	private Label actualTotal;
	@FXML
	private ExpenseTableController expenseTableController;
	@FXML
	private IncomeTableController incomeTableController;
	@FXML
	private Text actualTotalCurrency;
	@FXML
	private Text diffCurrency;
	@FXML
	private Label difference;
	@FXML
	private AnchorPane actualWrapper;
	@FXML
	private AnchorPane projectedWrapper;
	@FXML
	private Text projectedTotalCurrency;
	@FXML
	private Label projectedTotal;
	@FXML
	private AnchorPane diffWrapper;
	private TimePeriod timePeriod;
	public void setFields(TimePeriod ntp) {
		timePeriod = ntp;
		incomeTableController.setFields(ntp);
		expenseTableController.setFields(ntp);
	}
	@FXML
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		var ss = new ScheduledService<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<>() {
					@Override
					protected Void call() throws Exception {
						Platform.runLater(() -> {
							setBalances(
								expenseTableController.getActualExpense(),
								expenseTableController.getProjectedExpense(),
								incomeTableController.getIncomeAmount(),
								incomeTableController.getProjectedIncomeAmount()
							);
						});
						return null;
					}
				};
			}
		};
		ss.setPeriod(Duration.millis(500));
		ss.start();
	}
	public void setBalances(double actualExpense, double projectedExpense, double actualIncome, double projectedIncome) {
		double actual = actualIncome - actualExpense;
		double projected = projectedIncome - projectedExpense;
		var diffVal = actual - projected;
		actualTotal.setText(String.format("%.2f", actual));
		projectedTotal.setText(String.format("%.2f", projected));
		difference.setText(String.format("%.2f", diffVal));
		difference.getStyleClass().clear();
		if(diffVal < 0) {
			difference.getStyleClass().addAll("text", "danger");
		} else if(diffVal > 0) {
			difference.getStyleClass().addAll("text", "success");
		}
	}
}