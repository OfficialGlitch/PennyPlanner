package GUI.util;

import GUI.App;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import models.Category;
import models.TimePeriod;
import models.instances.ExpenseInstance;
import org.hibernate.Session;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryIntermediate implements ExpenseTreeTableItem {
	
	public final Category category;
	
	public CategoryIntermediate(Category cat) {
		this.category = cat;
	}
	
	private final ObservableList<ExpenseInstance> expenseInstances = FXCollections.observableArrayList();
	
	@Override
	public String name() {
		return this.category.getName();
	}
	
	@Override
	public StringProperty nameProperty() {
		return category.name;
	}
	
	@Override
	public void updateExpenses(TimePeriod timePeriod) {
		Task<List<ExpenseInstance>> task = new Task<List<ExpenseInstance>>() {
			@Override
			protected List<ExpenseInstance> call() throws Exception {
				List<ExpenseInstance> list;
				try (Session s = App.sf().openSession()) {
					list = s.createNamedQuery("getExpenseInstancesForCategory", ExpenseInstance.class)
						.setParameter("tp", timePeriod.getID())
						.setParameter("cat", category.ID.get())
						.getResultList();
				}
				return list;
			}
		};
		task.setOnSucceeded((e) -> {
			expenseInstances.setAll(task.getValue());
		});
		ExecutorService es = Executors.newFixedThreadPool(1);
		es.execute(task);
		es.shutdown();
	}
	
	@Override
	public ObservableList<ExpenseInstance> getExpenseInstances(TimePeriod timePeriod) {
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
