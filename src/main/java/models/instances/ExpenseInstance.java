package models.instances;

import javafx.beans.property.*;
import models.money.Expense;
import models.TimePeriod;

import jakarta.persistence.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity(name = "expenses")
@Access(AccessType.PROPERTY)
@NamedQueries({
		@NamedQuery(name = "getExpenseInstancesForCategory", query = "select e from expenses e " +
				"full join fetch e.expense as et " +
				"full join fetch e.month as em " +
				"full join fetch et.category as ec " +
				"where (em.ID = :tp and ec.ID = :cat)"),
		@NamedQuery(name = "getExpenseInstancesForExpenseType", query = "select e from expenses e " +
				"full join fetch e.expense ec " +
				"full join fetch e.month em " +
				"where em.ID = :curMonth and ec.ID = :curEID")
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
	public void setID(int nid) { ID.set(nid); }

	@ManyToOne
	public TimePeriod getMonth() {
		return month.get();
	}
	void setMonth(TimePeriod month) {
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
	@OneToOne
	public Expense getExpense() {
		return this.expense.get();
	}
	public void setExpense(Expense ne)  {
		this.expense.set(ne);
	}
}
