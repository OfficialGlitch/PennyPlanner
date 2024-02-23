package GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import models.TimePeriod;

public class MonthTabContentController implements Initializable
{
	@FXML
	private ExpenseTableController expenseTableController;
	@FXML
	private IncomeTableController incomeTableController;
	
	private TimePeriod timePeriod;
	public void setTimePeriod(TimePeriod ntp) {
		timePeriod = ntp;
		incomeTableController.setFields(ntp);
		expenseTableController.setFields(ntp);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}
}