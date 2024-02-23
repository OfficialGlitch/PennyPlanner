package models.money;

import jakarta.persistence.*;
import javafx.beans.property.*;
import models.money.User;

@Entity(name = "incomeTypes")
@Access(AccessType.PROPERTY)
public class Income {
	public final IntegerProperty ID = new SimpleIntegerProperty();
	
	public final StringProperty name = new SimpleStringProperty();
	public final ObjectProperty<User> user = new SimpleObjectProperty<>();
	
	public Income() {
	
	}
	
	@Column(nullable = false)
	public String getName() {
		return name.get();
	}
	
	public void setName(String newName) {
		name.set(newName);
	}
	
	@Id
	@GeneratedValue
	public int getID() {
		return ID.get();
	}
	
	
	public void setID(int nid) {
		ID.set(nid);
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	public User getUser() {
		return user.get();
	}
	
	public void setUser(User user) {
		this.user.set(user);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}
		return ID == ((models.money.Income) other).ID;
	}
	
}