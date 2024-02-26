package models;

import GUI.App;
import models.instances.ExpenseInstance;
import models.money.Expense;
import models.money.Income;
import models.money.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class DataGenerator {
	private static Category createCategory(String name, String[] expenses, User u) {
		Category val = new Category(true, name);
		val.setUser(u);
		for (String el : expenses) {
			Expense e = new Expense();
			e.setName(el);
			val.getExpenses().add(e);
			e.setCategory(val);
			App.doWork(x -> x.persist(e));
		}
		return val;
	}
	
	public static void generate() {
		ArrayList<Category> cats = new ArrayList<>();
		User sample = new User();
		sample.setUsername("test");
		sample.setPassword(BCrypt.hashpw("Testing123!", BCrypt.gensalt()));
		sample.setEmail("test@testi.ng");
		var pi = new Income();
		pi.setName("Primary");
		pi.setUser(sample);
		sample.getIncomeTypes().add(pi);
		String[] elements = new String[]{
			"Rent", "Electricity", "Gas", "Water", "Cable",
			"Internet", "Waste removal", "Maintenance/Repairs"
		};
		cats.add(createCategory("Bills", elements, sample));
		
		elements = new String[]{
			"Vehicle payment", "Bus/Taxi fare", "Insurance",
			"Licensing", "Fuel", "Maintenance", "Other"
		};
		cats.add(createCategory("Transportation", elements, sample));
		
		elements = new String[]{
			"Home", "Health", "Life"
		};
		cats.add(createCategory("Insurance", elements, sample));
		
		elements = new String[]{
			"Groceries", "Dining out"
		};
		cats.add(createCategory("Food", elements, sample));
		
		elements = new String[]{
			"Medical", "Hair/nails", "Clothing", "Dry cleaning",
			"Gym membership", "Other memberships/fees"
		};
		cats.add(createCategory("Self-Care", elements, sample));
		
		elements = new String[]{
			"Night out", "Music streaming services", "Movies",
			"Concerts", "Sporting events", "Live theater",
			"Video streaming services", "Other"
		};
		cats.add(createCategory("Entertainment", elements, sample));
		
		elements = new String[]{
			"Personal", "Student", "Credit card"
		};
		cats.add(createCategory("Loans", elements, sample));
		
		elements = new String[]{
			"Federal", "State", "Local", "Other"
		};
		cats.add(createCategory("Taxes", elements, sample));
		
		elements = new String[]{
			"Retirement account",
			"Investment account",
			"Stocks/securities"
		};
		cats.add(createCategory("Investments", elements, sample));
		
		elements = new String[]{
			"Charity",
			"Donations",
			"Tips"
		};
		cats.add(createCategory("Gifts", elements, sample));
		
		
		sample.setExpenseCategories(cats);
		for (Category c : cats) {
			App.doWork(x -> {
				x.persist(c);
			});
			for (Expense e : c.getExpenses()) {
				App.doNonSessionWork(x -> {
					x.persist(e);
				});
			}
		}
		App.s().persist(sample);
//		App.setCurrentUser(sample);
	}
}
