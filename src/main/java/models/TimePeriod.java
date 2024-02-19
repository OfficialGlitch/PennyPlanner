package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import GUI.App;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.instances.ExpenseInstance;
import models.instances.IncomeInstance;

import jakarta.persistence.*;
import models.money.Expense;
import models.money.User;

@Entity(name = "time_periods")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name = "findTimePeriod", query = "from time_periods t " +
		"join t.user as user " +
		"where user.id = :uid and " +
		"t.month = :month and " +
		"t.year = :year")
})
public class TimePeriod {
	public SimpleLongProperty ID = new SimpleLongProperty();
	private ObservableList<ExpenseInstance> expenses = FXCollections.observableArrayList();
	private ObservableList<IncomeInstance> incomeSources = FXCollections.observableArrayList();
	private IntegerProperty year = new SimpleIntegerProperty();
	private IntegerProperty month = new SimpleIntegerProperty();
	private ObjectProperty<User> user = new SimpleObjectProperty<>();
	
	public TimePeriod() {
		Calendar inst = Calendar.getInstance();
		this.year.set(inst.get(Calendar.YEAR));
		this.month.set(inst.get(Calendar.MONTH) + 1);
	}
	
	@Id
	@GeneratedValue
	public long getID() {
		return ID.get();
	}
	public void setID(long nid) {
		ID.set(nid);
	}
	
	public static String constructID(int year, int month) {
		return String.format("%d-%d", year, month);
	}
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public ObservableList<ExpenseInstance> getExpenses() {
		return expenses;
	}
	
	public void setExpenses(List<ExpenseInstance> eis) {
		expenses.clear();
		expenses.addAll(eis);
	}
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public List<IncomeInstance> getIncomeSources() {
		return incomeSources;
	}
	
	public void setIncomeSources(List<IncomeInstance> eis) {
		incomeSources.clear();
		incomeSources.addAll(eis);
	}
	
	@Column
	public int getYear() {
		return year.get();
	}
	
	public void setYear(int year) {
		this.year.set(year);
	}
	
	@Column
	public int getMonth() {
		return month.get();
	}
	public void setMonth(int month) {
		this.month.set(month);
	}
	@ManyToOne(cascade = CascadeType.ALL)
	public User getUser() {
		return user.get();
	}
	
	public void setUser(User user) {
		this.user.set(user);
	}
	
	public double projectedIncome() {
		double total = 0d;
		for (IncomeInstance ii : incomeSources) {
			total += ii.getProjected();
		}
		return total;
	}
	
	public double actualIncome() {
		double total = 0d;
		for (IncomeInstance ii : incomeSources)
			total += ii.getAmount();
		
		return total;
	}
	
	public double projectedExpense() {
		double total = 0d;
		for (ExpenseInstance ei : expenses) total += ei.getProjectedCost();
		return total;
	}
	
	public double actualExpense() {
		double total = 0d;
		for (ExpenseInstance ei : expenses) total += ei.getCost();
		return total;
	}
	
	public double projectedBalance() {
		return this.projectedIncome() - this.projectedExpense();
	}
	
	public double actualBalance() {
		return this.projectedIncome() - this.actualExpense();
	}
	
	public double difference() {
		return this.actualBalance() - this.projectedBalance();
	}
	
	@Override
	public int hashCode() {
		return ID.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof TimePeriod o) {
			return o.ID.get() == (this.ID.get());
		}
		return false;
	}
	
	public static TimePeriod generateNewMonth() {
		User u = App.getCurrentUser();
		Calendar instance = Calendar.getInstance();
		TimePeriod ctp = App.s()
			.createNamedQuery("findTimePeriod", TimePeriod.class)
			.setParameter("uid", u.getID())
			.setParameter("month", instance.get(Calendar.MONTH) + 1)
			.setParameter("year", instance.get(Calendar.YEAR)).getSingleResultOrNull();
		if(ctp == null) {
			TimePeriod tp = new TimePeriod();
			tp.setUser(u);
			tp.setMonth(instance.get(Calendar.MONTH) + 1);
			tp.setYear(instance.get(Calendar.YEAR));
			try (var sess = App.sf().openSession()) {
				var categories = sess.createNamedQuery("getAllCategories", Category.class).setParameter("user", u.getID()).getResultList();
				categories.forEach(c -> {
//					sess.refresh(c);
					var exps = sess.createNamedQuery("getExpensesForCategory", Expense.class).setParameter("cat", c.getID()).getResultList();
					exps.forEach(e -> {
						ExpenseInstance i = new ExpenseInstance();
						i.setExpense(e);
						App.doWork(s -> {
							i.setMonth(tp);
							s.persist(i);
						});
						tp.getExpenses().add(i);
					});
				});
			}
			u.getIncomeTypes().forEach(it -> {
				IncomeInstance ii = new IncomeInstance();
				ii.setIncomeSource(it);
				App.doWork(s -> {
					s.persist(ii);
				});
				tp.getIncomeSources().add(ii);
			});
//			App.doWork(s -> {
//				s.persist(tp);
//			});
			tp.getExpenses().forEach(x -> {
				App.doWork(s -> {
					x.setMonth(tp);
					s.merge(x);
				});
			});
			for (int i = 0; i < tp.getIncomeSources().size(); i++) {
				var x = tp.getIncomeSources().get(i);
				App.doNonSessionWork(s -> {
					x.setMonth(tp);
					s.merge(x);
				});
			}
			
			return tp;
		}
		return ctp;
	}
}

