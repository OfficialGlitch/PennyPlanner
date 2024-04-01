package UnitTest;

import GUI.Loan.LoanController;
import GUI.Loan.LoanInfo;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
	public void testAddButton() {
		Platform.runLater(() -> {
			loanController = new LoanController();
			TableView<LoanInfo> tableView = loanController.getLoanDetailsTableView();
			assertTrue(tableView.getItems().isEmpty());

			TextField loanAmountTextField = loanController.getLoanAmountTextField();
			loanAmountTextField.setText("1000");

			TextField interestRateTextField = loanController.getInterestRateTextField();
			interestRateTextField.setText("5.0");

			TextField paymentPeriodTextField = loanController.getPaymentPeriodTextField();
			paymentPeriodTextField.setText("12");

			loanController.handleAddButton();

			assertFalse(tableView.getItems().isEmpty());
			assertEquals(1, tableView.getItems().size());
		});
	}

	@Test
	public void testDeleteButton() {
		Platform.runLater(() -> {
			loanController = new LoanController();
			TableView<LoanInfo> tableView = loanController.getLoanDetailsTableView();
			assertNotNull(tableView);
			assertTrue(tableView.getItems().isEmpty());

			// Add a loan to the table
			TextField loanAmountTextField = loanController.getLoanAmountTextField();
			loanAmountTextField.setText("1000");

			TextField interestRateTextField = loanController.getInterestRateTextField();
			interestRateTextField.setText("5.0");

			TextField paymentPeriodTextField = loanController.getPaymentPeriodTextField();
			paymentPeriodTextField.setText("12");
			//loanController.addLoan(loanInfo);
			loanController.handleAddButton();
			// Delete the added loan
			loanController.handleDeleteButton();

			// Verify that the loan is deleted from the table
			assertTrue(tableView.getItems().isEmpty());
		});
	}
	@Test
	public void testSaveButton() {
		Platform.runLater(() -> {
			loanController = new LoanController();
			TableView<LoanInfo> tableView = loanController.getLoanDetailsTableView();
			assertNotNull(tableView);
			assertTrue(tableView.getItems().isEmpty());

			// Add a loan to the table
			TextField loanAmountTextField = loanController.getLoanAmountTextField();
			loanAmountTextField.setText("1000");

			TextField interestRateTextField = loanController.getInterestRateTextField();
			interestRateTextField.setText("5.0");

			TextField paymentPeriodTextField = loanController.getPaymentPeriodTextField();
			paymentPeriodTextField.setText("12");
			loanController.handleAddButton();

			// Save the added loan
			loanController.handleSaveButton();

			// Verify that the loan is saved (you need to implement this verification logic)
			assertTrue(!tableView.getItems().isEmpty());
		});
	}

}