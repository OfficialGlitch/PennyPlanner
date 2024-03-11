package models.instances;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import javafx.beans.property.*;
import models.money.*;
import models.*;

import org.hibernate.annotations.*;
import jakarta.persistence.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity(name = "incomes")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name = "getIncomesForTimePeriod", query = "select i from incomes i " +
		"full join i.incomeSource as iS " +
		"full join i.month as iM " +
		"full join iS.user as iU " +
		"where iM.ID = :month and iU.ID = :user")
})
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
	
	@ManyToOne
	@JoinColumn(unique = false)
	@PrimaryKeyJoinColumn(name = "IncomeSource_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
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
