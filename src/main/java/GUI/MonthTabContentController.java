package GUI;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.TimePeriod;

public class MonthTabContentController implements Initializable
{
	@FXML
	private ExpenseTableController expenseTableController;
	@FXML
	private IncomeTableController incomeTableController;
	@FXML
	private Label actualTotal;
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
	private MainPageController mainPageController;
	
	public void setFields(TimePeriod ntp, MainPageController mpc) {
		timePeriod = ntp;
		mainPageController = mpc;
		mpc.getTabs().getSelectionModel().selectedItemProperty().addListener((x -> {
			incomeTableController.setFields(ntp, this);
			expenseTableController.setFields(ntp, this);
		}));
		incomeTableController.setFields(ntp, this);
		expenseTableController.setFields(ntp, this);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}
	public void setBalances(double actual, double projected) {
		var diffVal = actual - projected;
		actualTotal.setText(String.format("%.2f", actual));
		projectedTotal.setText(String.format("%.2f", projected));
		difference.setText(String.format("%.2f", diffVal));
		difference.getStyleClass().clear();
		if(diffVal < 0) {
			difference.getStyleClass().addAll("text", "danger", "bold");
		} else if(diffVal > 0) {
			difference.getStyleClass().addAll("text", "success");
		}
	}
}