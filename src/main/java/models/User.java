package models;

import jakarta.persistence.*;
import models.money.Expense;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Access(AccessType.PROPERTY)
public class User {
    private String name;
    private String password;
    private String email;
    private String country;
    private String username;

    private List<Category> expenseCategories = new ArrayList<>();
    private List<Expense> expenseTypes = new ArrayList<>();
    private List<TimePeriod> history = new ArrayList<>();




    @Column
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Column
    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}

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
    public List<Category> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(List<Category> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public List<Expense> getExpenseTypes() {
        return expenseTypes;
    }

    public void setExpenseTypes(List<Expense> expenses) {
        this.expenseTypes = expenses;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public List<TimePeriod> getHistory() {
        return history;
    }

    public void setHistory(List<TimePeriod> history) {
        this.history = history;
    }
}
