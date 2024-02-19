package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.money.User;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public AnchorPane loginMessage;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;
    @FXML
    public Button registerButton;
    @FXML
    public TextField usernameTextField;
		@FXML
		public Label errorMessage;
		
		public void login(ActionEvent ae) {
			User user = App.s().createNamedQuery("UserByUsername", User.class).setParameter("username", usernameTextField.getText()).getSingleResultOrNull();
			if(user == null) {
				errorMessage.setText("User not found");
				return;
			}
			boolean correctPassword = BCrypt.checkpw(passwordField.getText(), user.getPassword());
			if(correctPassword) {
				App.setCurrentUser(user);
			} else {
				errorMessage.setText("Wrong username or password");
				return;
			}
		}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
