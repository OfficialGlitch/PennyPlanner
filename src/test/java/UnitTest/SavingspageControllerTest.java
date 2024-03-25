package UnitTest;

import GUI.SavingspageController;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SavingspageControllerTest {

	private static SavingspageController controller;

	@BeforeAll
	public static void initJavaFX() throws InterruptedException {
		// Ensure JavaFX platform is initialized
		final boolean[] flag = {false};
		Platform.startup(() -> flag[0] = true);
		// Wait for JavaFX platform to initialize
		while (!flag[0]) {
			Thread.sleep(10);
		}

		// Initialize your controller (if needed, adjust according to your app structure)
		Platform.runLater(() -> {
			controller = new SavingspageController();
			// Assuming your controller uses FXML annotations, you might need to manually instantiate components here for the test
			controller.tfSavingsGoal = new TextField();
			controller.tfIncomeAmount = new TextField();
			controller.tfTotalExpenses = new TextField();
			controller.tfActualSavings = new TextField(); // TextField is non-editable in the UI, but we can still manipulate it in tests
			controller.lblSavingsGoalStatus = new Label();
		});
	}

	@Test
	public void testCalculateSavings() throws InterruptedException {
		Platform.runLater(() -> {
			// Setup the input values
			controller.tfIncomeAmount.setText("1000");
			controller.tfTotalExpenses.setText("500");
			controller.tfSavingsGoal.setText("200");

			// Trigger the calculation
			controller.calculateSavings(null); // Event can be null as it's not used in the method

			// Assert the outcomes
			assertEquals("500.00", controller.tfActualSavings.getText(), "The actual savings calculated is incorrect.");
			assertEquals("Savings Goal Reached!", controller.lblSavingsGoalStatus.getText(), "The savings goal status message is incorrect.");
		});
		// Wait for Platform.runLater() to finish
		Thread.sleep(100); // Adjust sleep time as needed
	}

	// Additional tests can follow a similar structure

	@Test
	public void testWhenExpensesGreaterThanIncome() throws InterruptedException {
		Platform.runLater(() -> {
			// Set inputs where expenses are greater than income
			controller.tfIncomeAmount.setText("500");
			controller.tfTotalExpenses.setText("800");
			controller.tfSavingsGoal.setText("100");

			// Perform calculation
			controller.calculateSavings(null);

			// Assertions
			assertEquals("-300.00", controller.tfActualSavings.getText(), "Actual savings should be negative.");
			assertEquals("Savings Goal Not Reached", controller.lblSavingsGoalStatus.getText(), "Status message incorrect for savings goal not reached due to negative savings.");
		});
		Thread.sleep(100); // Allow time for the asynchronous operation to complete
	}

	@Test
	public void testInvalidInputForIncome() throws InterruptedException {
		Platform.runLater(() -> {
			// Set inputs with invalid income value
			controller.tfIncomeAmount.setText("invalid");
			controller.tfTotalExpenses.setText("400");
			controller.tfSavingsGoal.setText("200");

			// Perform calculation
			controller.calculateSavings(null);

			// Assertions
			assertEquals("Invalid input", controller.tfActualSavings.getText(), "Invalid input handling failed for income.");
		});
		Thread.sleep(100);
	}

	@Test
	public void testInvalidInputForExpenses() throws InterruptedException {
		Platform.runLater(() -> {
			// Set inputs with invalid expenses value
			controller.tfIncomeAmount.setText("1000");
			controller.tfTotalExpenses.setText("invalid");
			controller.tfSavingsGoal.setText("500");

			// Perform calculation
			controller.calculateSavings(null);

			// Assertions
			assertEquals("Invalid input", controller.tfActualSavings.getText(), "Invalid input handling failed for expenses.");
		});
		Thread.sleep(100);
	}

	@Test
	public void testSavingsExactlyMeetGoal() throws InterruptedException {
		Platform.runLater(() -> {
			// Set inputs where savings exactly meet the savings goal
			controller.tfIncomeAmount.setText("1000");
			controller.tfTotalExpenses.setText("600");
			controller.tfSavingsGoal.setText("400");

			// Perform calculation
			controller.calculateSavings(null);

			// Assertions
			assertEquals("400.00", controller.tfActualSavings.getText(), "Actual savings should exactly meet the savings goal.");
			assertEquals("Savings Goal Reached!", controller.lblSavingsGoalStatus.getText(), "Status message incorrect for exactly meeting the savings goal.");
		});
		Thread.sleep(100);
	}

	@Test
	public void testZeroOrNegativeInputValues() throws InterruptedException {
		Platform.runLater(() -> {
			// Set zero or negative input values
			controller.tfIncomeAmount.setText("0");
			controller.tfTotalExpenses.setText("-100");
			controller.tfSavingsGoal.setText("0");

			// Perform calculation
			controller.calculateSavings(null);

			// Assertions for zero income and negative expenses
			assertEquals("100.00", controller.tfActualSavings.getText(), "Actual savings incorrect for zero income and negative expenses.");
			assertEquals("Savings Goal Reached!", controller.lblSavingsGoalStatus.getText(), "Status message incorrect for zero or negative input values.");
		});
		Thread.sleep(100);
	}

	@Test
	public void testExtremelyLargeValues() throws InterruptedException {
		Platform.runLater(() -> {
			// Set extremely large values for income and expenses
			controller.tfIncomeAmount.setText("1E308");
			controller.tfTotalExpenses.setText("1E307"); // 10 times smaller, ensuring savings are positive
			controller.tfSavingsGoal.setText("1E307");

			// Perform calculation
			controller.calculateSavings(null);

			// Assertions to check for overflow or incorrect calculation
			assertEquals("9E307", controller.tfActualSavings.getText(), "Actual savings incorrect for extremely large values.");
			assertEquals("Savings Goal Reached!", controller.lblSavingsGoalStatus.getText(), "Status message incorrect for large values.");
		});
		Thread.sleep(100);
	}

}
