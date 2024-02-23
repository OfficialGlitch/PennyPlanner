package models.money;

import jakarta.persistence.*;
import models.Category;
import models.TimePeriod;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name = "UserByUsername", query = "from users " +
		"where username = :username"
	)
})
public class User {
	private long ID;
	private String name;
	private String password;
	private String email;
	private String country;
	private String username;
	private List<Income> incomeTypes = new ArrayList<>();
	private List<Category> expenseCategories = new ArrayList<>();
	private List<Expense> expenseTypes = new ArrayList<>();
	private List<TimePeriod> history = new ArrayList<>();
	
	@Column(unique = true)
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column
	public String getCountry() {
		return country;
		
		
	}
	
	public void setCountry(String country) {
		this.country = country;
		
	}
	
	@Column
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Id
	@GeneratedValue
	public long getID() {
		return ID;
	}
	
	public void setID(long ID) {
		this.ID = ID;
	}
	
	@Column
	public String getName() {
		return name;
	}
	
	public void setName(String nickName) {
		this.name = nickName;
	}
	
	@Column
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public List<Category> getExpenseCategories() {
		return expenseCategories;
	}
	
	public void setExpenseCategories(List<Category> expenseCategories) {
		this.expenseCategories = expenseCategories;
	}
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public List<Expense> getExpenseTypes() {
		return expenseTypes;
	}
	
	public void setExpenseTypes(List<Expense> expenses) {
		this.expenseTypes = expenses;
	}
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public List<TimePeriod> getHistory() {
		return history;
	}
	public void setHistory(List<TimePeriod> list) {
		history = list;
	}
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public List<Income> getIncomeTypes() {
		return incomeTypes;
	}
	
	public void setIncomeTypes(List<Income> it) {
		incomeTypes = it;
	}
	
	@Override
	public int hashCode() {
		return (int) ID;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}
		return ID == ((User) other).ID;
	}
}
