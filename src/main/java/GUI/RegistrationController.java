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

		User existingUser = App.s().createNamedQuery("UserByUsername", User.class)
			.setParameter("username", usernameTextField.getText())
			.getSingleResultOrNull();

		if (existingUser != null) {
			// A user with this username already exists, so display an error message
			errorMessage.setText("User already exists");
			return; // Stop further registration processing
		}


	}

	private void Back(ActionEvent event) {

	}


}
