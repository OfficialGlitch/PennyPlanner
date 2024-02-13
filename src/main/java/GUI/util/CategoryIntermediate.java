package GUI.util;

import GUI.App;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Category;
import models.TimePeriod;
import models.instances.ExpenseInstance;
import org.hibernate.Session;
import org.hibernate.annotations.NamedQuery;

public class CategoryIntermediate implements ExpenseTreeTableItem {
	
	public final ObjectProperty<Category> category = new SimpleObjectProperty<>();
	
	public CategoryIntermediate(Category cat) {
		this.category.set(cat);
	}
	
	private final ObservableList<ExpenseInstance> expenseInstances = FXCollections.observableArrayList();
	
	@Override
	public String name() {
		return this.category.get().getName();
	}
	
	@Override
	public StringProperty nameProperty() {
		return category.get().name;
	}
	
	@Override
	public void updateExpenses(TimePeriod timePeriod) {
		try (Session s = App.s()) {
			var listy = s.createNamedQuery("getExpenseInstancesForCategory", ExpenseInstance.class)
				.setParameter("tp", timePeriod.getID())
				.setParameter("cat", category.get().ID.get())
				.getResultList();
			expenseInstances.setAll(listy);
		}
	}
	
	@Override
	public ObservableList<ExpenseInstance> getExpenseInstances(TimePeriod timePeriod) {
//		updateExpenses(timePeriod);
		return expenseInstances;
	}
	
	@Override
	public double getCost(TimePeriod timePeriod) {
		double result = 0d;
		for (ExpenseInstance i : getExpenseInstances(timePeriod)) {
			result += i.getCost();
		}
		return result;
	}
	
	@Override
	public double getProjectedCost(TimePeriod timePeriod) {
		double result = 0d;
		for (ExpenseInstance i : getExpenseInstances(timePeriod)) {
			result += i.getProjectedCost();
		}
		return result;
	}
	
	@Override
	public double difference(TimePeriod timePeriod) {
		return getProjectedCost(timePeriod) - getCost(timePeriod);
	}
}
