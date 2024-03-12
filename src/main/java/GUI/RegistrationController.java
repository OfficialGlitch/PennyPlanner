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
import models.money.User;
import org.mindrot.jbcrypt.BCrypt;

import java.awt.event.ActionEvent;
import java.io.IOException;
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
	private void register(ActionEvent event) {
		if (usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty() ||
			emailTextField.getText().isEmpty() || nameTextField.getText().isEmpty() ||
			countryTextField.getText().isEmpty()) {
			errorMessage.setText("Please fill in all fields.");
			return;
		}
		User user = App.s().createNamedQuery("UserByUsername", User.class)
			.setParameter("username", usernameTextField.getText()).getSingleResultOrNull();
		if(user != null) {
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


		EntityManager em = App.s(); // Get the EntityManager
		EntityTransaction transaction = em.getTransaction();

		try {
			transaction.begin();
			em.persist(user);
			transaction.commit();

			// Provide feedback to the user
			errorMessage.setText("Registration successful.");

			// Optionally navigate to the login page or another page
			login(event); // If you have a method to navigate back to the login page
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			errorMessage.setText("Registration failed. Please try again.");
			e.printStackTrace(); // Print the stack trace for debugging
		}
	}

	private void login(ActionEvent event) {
		try {
			FXMLLoader loader = loadFXML("Login");
			Parent p = loader.load();
			App.setCurrentScene(p);
		}catch (IOException e) {
			System.err.println("Error loading Login page: " + e);
			e.printStackTrace();
			errorMessage.setText("Error loading login page.");
		}
	}

}
