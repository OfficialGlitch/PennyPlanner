package GUI;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Category;
import models.money.Expense;
import models.money.Income;
import models.money.User;
import org.mindrot.jbcrypt.BCrypt;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.*;

import static GUI.App.loadFXML;

public class RegistrationController {
	@FXML
	public TextField usernameTextField;
	@FXML
	public PasswordField passwordField;
	@FXML
	public Label errorMessage;
	@FXML
	public Button backButton;
	@FXML
	public Button registerButton;
	@FXML
	public TextField countryTextField;
	@FXML
	public TextField nameTextField;
	@FXML
	public TextField emailTextField;


	@FXML
	public void register(ActionEvent ae) {
		if (usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty() ||
			emailTextField.getText().isEmpty() || nameTextField.getText().isEmpty() ||
			countryTextField.getText().isEmpty()) {
			errorMessage.setText("Please fill in all fields.");
			return;
		}
		User user = App.s().createNamedQuery("UserByUsername", User.class)
			.setParameter("username", usernameTextField.getText()).getSingleResultOrNull();
		if (user != null) {
			errorMessage.setText("User already exists");
			return;
		}
		// Create a new user since the username is not taken
		user = new User();
		user.setUsername(usernameTextField.getText());
		user.setPassword(BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt()));
		user.setEmail(emailTextField.getText());
		user.setName(nameTextField.getText());
		user.setCountry(countryTextField.getText());

		dataInjection(user);

		EntityManager em = App.s(); // Get the EntityManager
		EntityTransaction transaction = em.getTransaction();

		try {
			transaction.begin();
			em.persist(user);
			transaction.commit();

			// Provide feedback to the user
			errorMessage.setText("Registration successful.");

			// Optionally navigate to the login page or another page
			App.setCurrentUser(user);
			try {
				FXMLLoader loader = loadFXML("MainPage");
				Parent p = loader.load();
				MainPageController controller = loader.getController();
				controller.setYear(Calendar.getInstance().get(Calendar.YEAR));
				App.setCurrentScene(p);
			} catch (IOException err) {
				System.err.println("Couldn't change scene: " + err.toString());
				err.printStackTrace();
			}
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			errorMessage.setText("Registration failed. Please try again.");
			e.printStackTrace(); // Print the stack trace for debugging
		}

	}

	public void login(ActionEvent ae) {
		try {
			FXMLLoader loader = loadFXML("Login");
			Parent p = loader.load();
			App.setCurrentScene(p);
		} catch (IOException e) {
			System.err.println("Error loading Login page: " + e);
			e.printStackTrace();
			errorMessage.setText("Error loading login page.");
		}
	}
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


	// add Income and expense table to new user
	private void dataInjection(User user){
		ArrayList<Category> cats = new ArrayList<>();
		var pi = new Income();
		pi.setName("Primary");
		pi.setUser(user);
		user.getIncomeTypes().add(pi);
		String[] elements = new String[]{
			"Rent", "Electricity", "Gas", "Water", "Cable",
			"Internet", "Waste removal", "Maintenance/Repairs"
		};
		cats.add(createCategory("Bills", elements, user));

		elements = new String[]{
			"Vehicle payment", "Bus/Taxi fare", "Insurance",
			"Licensing", "Fuel", "Maintenance", "Other"
		};
		cats.add(createCategory("Transportation", elements, user));

		elements = new String[]{
			"Home", "Health", "Life"
		};
		cats.add(createCategory("Insurance", elements, user));

		elements = new String[]{
			"Groceries", "Dining out"
		};
		cats.add(createCategory("Food", elements, user));

		elements = new String[]{
			"Medical", "Hair/nails", "Clothing", "Dry cleaning",
			"Gym membership", "Other memberships/fees"
		};
		cats.add(createCategory("Self-Care", elements, user));

		elements = new String[]{
			"Night out", "Music streaming services", "Movies",
			"Concerts", "Sporting events", "Live theater",
			"Video streaming services", "Other"
		};
		cats.add(createCategory("Entertainment", elements, user));

		elements = new String[]{
			"Personal", "Student", "Credit card"
		};
		cats.add(createCategory("Loans", elements, user));

		elements = new String[]{
			"Federal", "State", "Local", "Other"
		};
		cats.add(createCategory("Taxes", elements, user));

		elements = new String[]{
			"Retirement account",
			"Investment account",
			"Stocks/securities"
		};
		cats.add(createCategory("Investments", elements, user));

		elements = new String[]{
			"Charity",
			"Donations",
			"Tips"
		};
		cats.add(createCategory("Gifts", elements, user));

		user.setExpenseCategories(cats);
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
		App.s().persist(user);
	}


}
