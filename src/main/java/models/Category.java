package models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.money.Expense;
import org.hibernate.annotations.NamedQuery;

@Entity(name = "categories")
@Access(AccessType.PROPERTY)
@org.hibernate.annotations.NamedQueries({
		@NamedQuery(name = "getAllCategories", query = "select c from categories c " +
				"full join fetch c.user as u " +
				"where u = :user")
})
public class Category {
    public final IntegerProperty ID = new SimpleIntegerProperty();
    public final StringProperty name = new SimpleStringProperty();
    public final ObjectProperty<User> user = new SimpleObjectProperty<>();
    public final ReadOnlyBooleanProperty builtIn;
    ObservableList<Expense> expenses = FXCollections.observableArrayList();

    protected Category(boolean isDefault, String name) {
        builtIn = new ReadOnlyBooleanWrapper(isDefault);
        this.name.set(name);
    }

    public Category(String name) {
        this(false, name);
    }

    public Category() {
        builtIn = new ReadOnlyBooleanWrapper(false);
    }


    @Column(name = "name")
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @OneToMany(fetch = FetchType.EAGER)
    public ObservableList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses.clear();
        this.expenses.addAll(expenses);
    }

    @Id
    @GeneratedValue
    public int getID() {
        return ID.get();
    }

    public void setID(int id) {
        this.ID.set(id);
    }

    @Column(updatable = false)
    public boolean getBuiltIn() {
        return this.builtIn.get();
    }

    public void setBuiltIn(boolean unused) {

    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return ID == ((Category) other).ID;
    }


    @ManyToOne
    public User getUser() {
        return user.get();
    }

    public void setUser(User user) {
        this.user.set(user);
    }

    @Override
    public String toString() {
        return name.get();
    }

    public static ArrayList<Category> sensibleDefaults() {
        Category BILL = new Category(true, "Bills");
        Category TRANSPORTATION = new Category(true, "Transportation");
        Category INSURANCE = new Category(true, "Insurance");
        Category FOOD = new Category(true, "Food");
        Category SELF_CARE = new Category(true, "Self-Care");
        Category ENTERTAINMENT = new Category(true, "Entertainment");
        Category LOANS = new Category(true, "Loans");
        Category TAXES = new Category(true, "Taxes");
        Category INVESTMENTS = new Category(true, "Investments");
        Category GIFTS = new Category(true, "Gifts");
        ArrayList<Category> ret = new ArrayList<>();
        ret.add(BILL);
        ret.add(TRANSPORTATION);
        ret.add(INSURANCE);
        ret.add(FOOD);
        ret.add(SELF_CARE);
        ret.add(ENTERTAINMENT);
        ret.add(LOANS);
        ret.add(TAXES);
        ret.add(INVESTMENTS);
        ret.add(GIFTS);
        return ret;
    }
}
