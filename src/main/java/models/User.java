package models;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

@Entity(name = "users")
@Access(AccessType.PROPERTY)
public class User {
    private Long ID;
    private String name;
    private String password;
    private String email;
    private ArrayList<Category> expenseCategories = new ArrayList<>();
    private ArrayList<Expense> expenseTypes = new ArrayList<>();
    private ArrayList<TimePeriod> history = new ArrayList<>();

    @GeneratedValue
    public Long getID() {
        return ID;
    }
    public void setID(Long id) {
        this.ID = id;
    }

    @Column
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Id
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
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @OneToMany(fetch = FetchType.EAGER)
    public ArrayList<Category> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(ArrayList<Category> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public ArrayList<Expense> getExpenseTypes() {
        return expenseTypes;
    }

    public void setExpenseTypes(ArrayList<Expense> expenses) {
        this.expenseTypes = expenses;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public ArrayList<TimePeriod> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<TimePeriod> history) {
        this.history = history;
    }
}
