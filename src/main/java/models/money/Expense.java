package models.money;

import jakarta.persistence.*;
import javafx.beans.property.*;
import models.Category;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity(name = "expenseTypes")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name = "getExpensesForCategory", query = "select e from expenseTypes e " +
		"join e.category as ec " +
		"where (ec.ID = :cat)"),
})
public class Expense {
	public final IntegerProperty ID = new SimpleIntegerProperty();
	
	public final StringProperty name = new SimpleStringProperty();
	public final ObjectProperty<Category> category = new SimpleObjectProperty<>();
	
	public Expense() {
	}
	
	@Id
	@GeneratedValue
	public int getID() {
		return ID.get();
	}
	
	public void setID(int nid) {
		ID.set(nid);
	}
	
	@Column(nullable = false)
	public String getName() {
		return name.get();
	}
	
	public void setName(String newName) {
		name.set(newName);
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "expenseTypes_categories", joinColumns = {
		@JoinColumn(name = "expenseID", referencedColumnName = "ID")
	}, inverseJoinColumns = @JoinColumn(name = "category", referencedColumnName = "ID"))
	public Category getCategory() {
		return category.get();
	}
	
	public void setCategory(Category newType) {
		category.set(newType);
	}
	
	@Override
	public String toString() {
		return this.name.get();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}
		return ID.get() == ((models.money.Expense) other).ID.get();
	}
}
