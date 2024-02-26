package GUI;

import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import models.TimePeriod;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class MainPageController implements Initializable
{
	@FXML
	private TabPane tabs;
	
	private int year;
	private final Calendar iCal = Calendar.getInstance();
	public TabPane getTabs() {return tabs;}
	
	public void setYear(int nyear) {
		year = nyear;
		initTabs();
	}
	public void initTabs() {
		tabs.getTabs().clear();
		var cal = Calendar.getInstance();
		for(int i = 0; i < 12; i++) {
			cal.set(Calendar.MONTH, i);
			Tab newTab = new Tab(new SimpleDateFormat("MMMM").format(cal.getTime()));
			URL rootURL = getClass().getResource("MonthTabContent.fxml");
			FXMLLoader loader = new FXMLLoader(rootURL);
			MonthTabContentController controller;
			TimePeriod tp = TimePeriod.generateNewMonth(i + 1, year);
			Parent root;
			try {
				root = loader.load();
				controller = loader.getController();
				newTab.setUserData(tp);
			} catch(IOException e) {
				throw new RuntimeException("Couldn't load tab: " + e.getMessage());
			}
			newTab.setClosable(false);
			newTab.setContent(root);
			controller.setFields(tp, this);
			tabs.getTabs().add(newTab);
		}
	}
	@FXML
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}
}