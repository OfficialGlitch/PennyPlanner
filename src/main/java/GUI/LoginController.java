package GUI;

import GUI.Loan.LoanController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.DataGenerator;
import models.TimePeriod;
import models.money.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import static GUI.App.loadFXML;

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
			if (usernameTextField.getText().isEmpty() && passwordField.getText().isEmpty()) {
				errorMessage.setText("Please enter username and password");
				return;
			}
			if (usernameTextField.getText().isEmpty()) {
				errorMessage.setText("Please enter username");
				return;
			}
		 if (passwordField.getText().isEmpty()) {
			 errorMessage.setText("Please enter password");
			 return;
		 }
			User user = App.s().createNamedQuery("UserByUsername", User.class).setParameter("username", usernameTextField.getText()).getSingleResultOrNull();
			if(user == null) {
				errorMessage.setText("User not found");
				return;
			}
			boolean correctPassword = BCrypt.checkpw(passwordField.getText(), user.getPassword());
			if(correctPassword) {
				App.setCurrentUser(user);
				try {
					FXMLLoader loader = loadFXML("MainPage");
					Parent p = loader.load();
					MainPageController controller = loader.getController();
					controller.setYear(Calendar.getInstance().get(Calendar.YEAR));
					App.setCurrentScene(p);
				} catch(IOException err) {
					System.err.println("Couldn't change scene: " + err.toString());
					err.printStackTrace();
				}
			} else {
				errorMessage.setText("Wrong username or password");
			}
		}

		public void register(ActionEvent ae) {
			try {
				FXMLLoader loader = loadFXML("Registration");
				Parent p = loader.load();
				App.setCurrentScene(p);
			}catch (IOException e) {
				System.err.println("Error loading registration page: " + e);
				e.printStackTrace();
				errorMessage.setText("Error loading registration page.");
			}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
