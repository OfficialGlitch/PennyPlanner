package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.instances.ExpenseInstance;
import models.instances.IncomeInstance;

import jakarta.persistence.*;

@Entity(name = "time_periods")
@Access(AccessType.PROPERTY)
public class TimePeriod {
	public final StringProperty ID = new SimpleStringProperty();
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
	public String getID() {
		return ID.get();
	}
	
	public void setID(String nid) {
		ID.set(nid);
	}
	
	public static String constructID(int year, int month) {
		return String.format("%d-%d", year, month);
	}
	
	@OneToMany(fetch = FetchType.EAGER)
	public ObservableList<ExpenseInstance> getExpenses() {
		return expenses;
	}
	
	public void setExpenses(List<ExpenseInstance> eis) {
		expenses.clear();
		expenses.addAll(eis);
	}
	
	@OneToMany(fetch = FetchType.EAGER)
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
	
	@ManyToOne
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
			return o.ID.equals(this.ID);
		}
		return false;
	}
}

