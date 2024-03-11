package GUI;

import jakarta.persistence.NoResultException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.money.User;

import java.awt.event.ActionEvent;

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
	private void Register(ActionEvent event) {

		try {
			// Attempt to retrieve a user by username
			User existingUser = App.s().createNamedQuery("UserByUsername", User.class)
				.setParameter("username", username)
				.getSingleResult();
			// If a user is found, return true indicating the user already exists
			return existingUser != null;
		} catch (NoResultException e) {
			// No user was found with the given username, return false
			return false;
		}
	}

	private void Back(ActionEvent event) {

	}

}