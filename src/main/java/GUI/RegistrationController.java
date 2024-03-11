package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
	private void handleRegisterAction(ActionEvent event) {
		if (usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty() ||
			countryTextField.getText().isEmpty() || nameTextField.getText().isEmpty() ||
			emailTextField.getText().isEmpty()) {
			errorMessage.setText("Please fill in all fields.");
			return;
		}

	}

}