package GUI;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.control.Tab;
import models.TimePeriod;

public class MonthTabContentController implements Initializable
{
	@FXML
	private ExpenseTableController expenseTableController;
	@FXML
	private IncomeTableController incomeTableController;
	
	private TimePeriod timePeriod;
	private MainPageController mainPageController;
	public void setFields(TimePeriod ntp, MainPageController mpc) {
		timePeriod = ntp;
		mainPageController = mpc;
		mpc.getTabs().getSelectionModel().selectedItemProperty().addListener((x -> {
			incomeTableController.setFields(ntp);
			expenseTableController.setFields(ntp);
		}));
		incomeTableController.setFields(ntp);
		expenseTableController.setFields(ntp);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}
}