package models.instances;

import javafx.beans.property.*;
import models.money.*;
import models.*;

import jakarta.persistence.*;

@Entity(name = "incomes")
@Access(AccessType.PROPERTY)
public class IncomeInstance {
	public final IntegerProperty ID = new SimpleIntegerProperty();
	
	public final DoubleProperty projected = new SimpleDoubleProperty(0d);
	
	public final DoubleProperty amount = new SimpleDoubleProperty(0d);
	
	public final ObjectProperty<Income> incomeSource = new SimpleObjectProperty<>();
	
	public final ObjectProperty<TimePeriod> month = new SimpleObjectProperty<>();
	
	@Column(nullable = false)
	public double getAmount() {
		return amount.get();
	}
	
	public void setAmount(double newAmount) {
		amount.set(newAmount);
	}
	
	@Column(nullable = false)
	public double getProjected() {
		return projected.get();
	}
	
	public void setProjected(double projectedAmt) {
		projected.set(projectedAmt);
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	public TimePeriod getMonth() {
		return month.get();
	}
	
	public void setMonth(TimePeriod month) {
		this.month.set(month);
	}
	
	@OneToOne
	public Income getIncomeSource() {
		return incomeSource.get();
	}
	
	public void setIncomeSource(Income is) {
		incomeSource.set(is);
	}
	
	@Id
	@GeneratedValue
	public int getID() {
		return ID.get();
	}
	
	public void setID(int nid) {
		ID.set(nid);
	}
}
