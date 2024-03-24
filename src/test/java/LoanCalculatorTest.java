import GUI.App;
import GUI.Loan.LoanController;
import GUI.Loan.LoanInfo;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoanCalculatorTest {

	private LoanController loanController;

	@BeforeAll
	public static void initJavaFX() {
		// Initialize JavaFX Toolkit
		Platform.startup(() -> {
			// No need to do anything here, just ensure the toolkit is initialized
		});
	}

	@Test
	public void testLoanAmountTextField() {
		Platform.runLater(() -> {
			loanController = new LoanController();
			TextField loanAmountTextField = loanController.getLoanAmountTextField();
			assertNotNull(loanAmountTextField);
			// Simulate entering a loan amount of 1000
			loanAmountTextField.setText("1000");
			assertEquals("1000", loanAmountTextField.getText());
		});
	}

	@Test
	public void testInterestRateTextField() {
		Platform.runLater(() -> {
			loanController = new LoanController();
			TextField interestRateTextField = loanController.getInterestRateTextField();
			assertNotNull(interestRateTextField);
			// Simulate entering an interest rate of 5.0
			interestRateTextField.setText("5.0");
			assertEquals("5.0", interestRateTextField.getText());
		});
	}

	@Test
	public void testPaymentPeriodTextField() {
		Platform.runLater(() -> {
			loanController = new LoanController();
			TextField paymentPeriodTextField = loanController.getPaymentPeriodTextField();
			assertNotNull(paymentPeriodTextField);
			// Simulate entering a payment period of 12 months
			paymentPeriodTextField.setText("12");
			assertEquals("12", paymentPeriodTextField.getText());
		});
	}

	@Test
	public void testCalculateButton() {
		Platform.runLater(() -> {
			loanController = new LoanController();
			TableView<LoanInfo> tableView = loanController.getLoanDetailsTableView();
			assertNotNull(tableView);
			assertTrue(tableView.getItems().isEmpty());

			TextField loanAmountTextField = loanController.getLoanAmountTextField();
			loanAmountTextField.setText("1000");

			TextField interestRateTextField = loanController.getInterestRateTextField();
			interestRateTextField.setText("5.0");

			TextField paymentPeriodTextField = loanController.getPaymentPeriodTextField();
			paymentPeriodTextField.setText("12");

			loanController.handleCalculateButton();

			assertFalse(tableView.getItems().isEmpty());
			assertEquals(1, tableView.getItems().size());
		});
	}
}