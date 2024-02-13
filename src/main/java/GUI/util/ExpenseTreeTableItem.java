package GUI.util;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import models.TimePeriod;
import models.instances.ExpenseInstance;

public interface ExpenseTreeTableItem {
	public String name();
	
	public StringProperty nameProperty();
	
	public ObservableList<ExpenseInstance> getExpenseInstances(TimePeriod timePeriod);
	
	public void updateExpenses(TimePeriod timePeriod);
	
	public double getCost(TimePeriod timePeriod);
	
	public double getProjectedCost(TimePeriod timePeriod);
	
	public double difference(TimePeriod timePeriod);
//    public abstract

}