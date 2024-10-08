package GUI;

import javafx.beans.value.WeakChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import models.TimePeriod;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
	@FXML
	private ExpenseIncomeController expenseIncomePageController;
	@FXML
	private SummaryPageController summaryPageController;
	@FXML
	private SavingspageController savingsPageController;
	@FXML
	private TabPane months;
	@FXML
	private Tab savingsTab;
	@FXML
	private Tab portfolioTab;

	@FXML
	private TabPane pageTabs;
	@FXML
	private Tab summaryTab;
	@FXML
	private Tab expenseIncomeTab;
	@FXML
	private ConvertCurrency currencyConverterController;
	
	private int year = Calendar.getInstance().get(Calendar.YEAR);
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTabs();
	}
	
	public void setYear(int nyear) {
		year = nyear;
	}
	public void initTabs() {
		var cal = Calendar.getInstance();
		for(int i = 0; i < 12; i++) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.MONTH, i);
			Tab newTab = new Tab(new SimpleDateFormat("MMMM", Locale.CANADA).format(cal.getTime()));

			newTab.setClosable(false);
			months.getTabs().add(newTab);
		}
		months.getSelectionModel().selectedIndexProperty().addListener(((obs, oldValue, newValue) -> {
			TimePeriod tp = TimePeriod.generateNewMonth((int) newValue + 1, year);
			expenseIncomePageController.setFields(tp);
			summaryPageController.setup(tp);
//			portfolioPageController.setup(tp);
			savingsPageController.setup(tp);
		}));
		pageTabs.getSelectionModel().selectedIndexProperty().addListener((obs, old, newVal) ->{
			TimePeriod tp = TimePeriod.generateNewMonth((int) months.getSelectionModel().selectedIndexProperty().getValue() + 1, year);
			expenseIncomePageController.setFields(tp);
			summaryPageController.setup(tp);
//			portfolioPageController.setup(tp);
			savingsPageController.setup(tp);
		});
		
		months.getSelectionModel().select(Calendar.getInstance().get(Calendar.MONTH));
	}
}