package models.instances;

import GUI.App;
import GUI.util.ExpenseTreeTableItem;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.money.Expense;
import models.TimePeriod;

import jakarta.persistence.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity(name = "expenses")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name = "getExpenseInstancesForCategory", query = "select e from expenses e " +
		"full join e.expense as et " +
		"full join e.month as em " +
		"full join et.category as ec " +
		"where (em.ID = :tp and ec.ID = :cat)"),
	@NamedQuery(name = "getExpenseInstancesForExpenseType", query = "select e from expenses e " +
		"full join e.expense ec " +
		"full join e.month em " +
		"where em.ID = :curMonth and ec.ID = :curEID"),
	@NamedQuery(name = "getExpensesForTimePeriod", query = "select e from expenses e " +
		"full join e.month em " +
		"full join e.expense as ee " +
		"full join ee.category as ec " +
		"full join ec.user as iU " +
		"where em.ID = :month and iU.ID = :user")
})
public class ExpenseInstance implements ExpenseTreeTableItem {
	public final IntegerProperty ID = new SimpleIntegerProperty();
	
	public final DoubleProperty projectedCost = new SimpleDoubleProperty(0d);
	
	public final DoubleProperty cost = new SimpleDoubleProperty(0d);
	
	public final ObjectProperty<Expense> expense = new SimpleObjectProperty<>();
	
	public final ObjectProperty<TimePeriod> month = new SimpleObjectProperty<>();
	
	@Id
	@GeneratedValue
	public int getID() {
		return ID.get();
	}
	
	public void setID(int nid) {
		ID.set(nid);
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	public TimePeriod getMonth() {
		return month.get();
	}
	
	public void setMonth(TimePeriod month) {
		this.month.set(month);
	}
	
	
	@Column(nullable = true)
	public double getCost() {
		return cost.get();
	}
	
	public void setCost(double newCost) {
		cost.set(newCost);
	}
	
	@Column(nullable = true)
	public double getProjectedCost() {
		return projectedCost.get();
	}
	
	public void setProjectedCost(double newCost) {
		projectedCost.set(newCost);
	}
 
	public double difference() {
		return projectedCost.get() - cost.get();
	}
	
	@ManyToOne
	@JoinColumn(unique = false)
	@PrimaryKeyJoinColumn(name = "Expense_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	public Expense getExpense() {
		return this.expense.get();
	}
	
	public void setExpense(Expense ne) {
		this.expense.set(ne);
	}
	
	public String name() {
		return this.expense.get().getName();
	}
	
	@Override
	public StringProperty nameProperty() {
		return expense.get().name;
	}
	
	@Override
	public ObservableList<ExpenseInstance> getExpenseInstances(TimePeriod timePeriod) {
		return FXCollections.emptyObservableList();
	}
	
	@Override
	public void updateExpenses(TimePeriod timePeriod) {
		// unused
	}
	
	@Override
	public double getCost(TimePeriod timePeriod) {
		return this.getCost();
	}
	
	@Override
	public double getProjectedCost(TimePeriod timePeriod) {
		return this.getProjectedCost();
	}
	
	@Override
	public double difference(TimePeriod timePeriod) {
		return this.difference();
	}
}
