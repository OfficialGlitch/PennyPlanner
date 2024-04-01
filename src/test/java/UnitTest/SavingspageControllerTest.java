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

		// Initialize your controller
		Platform.runLater(() -> {
			controller = new SavingspageController();
			controller.tfSavingsGoal = new TextField();
			controller.lblIncomeAmount = new Label();
			controller.lblTotalExpenses = new Label();
			controller.lblActualSavings = new Label();
			controller.lblSavingsGoalStatus = new Label();
		});
	}

	@Test
	public void testCalculateSavings() throws InterruptedException {
		Platform.runLater(() -> {
			// Simulate setting total income and expenses directly as the controller method would
			controller.lblIncomeAmount.setText("1000");
			controller.lblTotalExpenses.setText("500");
			controller.tfSavingsGoal.setText("200");

			// Assuming calculateSavings takes directly set values or simulate the setupData call here
			double income = Double.parseDouble(controller.lblIncomeAmount.getText());
			double expenses = Double.parseDouble(controller.lblTotalExpenses.getText());
			controller.calculateSavings(income, expenses);

			assertEquals("500.00", controller.lblActualSavings.getText());
			assertEquals("Savings Goal Reached!", controller.lblSavingsGoalStatus.getText());
		});
		Thread.sleep(100);
	}

	@Test
	public void testWhenExpensesGreaterThanIncome() throws InterruptedException {
		Platform.runLater(() -> {
			// Set inputs where expenses are greater than income
			controller.lblIncomeAmount.setText("500");
			controller.lblTotalExpenses.setText("800");
			controller.tfSavingsGoal.setText("100");

			// Assertions
			assertEquals("-300.00", controller.lblActualSavings.getText(), "Actual savings should be negative.");
			assertEquals("Savings Goal Not Reached", controller.lblSavingsGoalStatus.getText(), "Status message incorrect for savings goal not reached due to negative savings.");
		});
		Thread.sleep(100); // Allow time for the asynchronous operation to complete
	}

	@Test
	public void testInvalidInputForIncome() throws InterruptedException {
		Platform.runLater(() -> {

			// Set inputs with invalid expenses value
			controller.lblIncomeAmount.setText("invalid");
			controller.lblTotalExpenses.setText("1000");
			controller.tfSavingsGoal.setText("500");

			// Assertions
			assertEquals("Invalid input", controller.lblActualSavings.getText(), "Invalid input handling failed for income.");
		});
		Thread.sleep(100);
	}


	@Test
	public void testInvalidInputForExpenses() throws InterruptedException {
		Platform.runLater(() -> {
			// Set inputs with invalid expenses value
			controller.lblIncomeAmount.setText("1000");
			controller.lblTotalExpenses.setText("invalid");
			controller.tfSavingsGoal.setText("500");

			// Assertions
			assertEquals("Invalid input", controller.lblActualSavings.getText(), "Invalid input handling failed for expenses.");
		});
		Thread.sleep(100);
	}

	@Test
	public void testSavingsExactlyMeetGoal() throws InterruptedException {
		Platform.runLater(() -> {
			// Assuming direct calculation based on input values
			double income = 1000;
			double expenses = 600;
			double goal = 400;

			controller.calculateSavings(income, expenses);

			assertEquals("400.00", controller.lblActualSavings.getText(), "Actual savings should exactly meet the savings goal.");
			assertTrue(controller.lblSavingsGoalStatus.getText().contains("Savings Goal Reached"), "Status message incorrect for exactly meeting the savings goal.");
		});
		Thread.sleep(100);
	}


	@Test
	public void testZeroOrNegativeInputValues() throws InterruptedException {
		Platform.runLater(() -> {
			double income = 0;
			double expenses = -100; // Indicates a refund or similar scenario

			controller.calculateSavings(income, expenses);

			assertEquals("100.00", controller.lblActualSavings.getText(), "Actual savings incorrect for zero income and negative expenses.");
			assertTrue(controller.lblSavingsGoalStatus.getText().contains("Savings Goal Reached"), "Status message incorrect for zero or negative input values.");
		});
		Thread.sleep(100);
	}


	@Test
	public void testExtremelyLargeValues() throws InterruptedException {
		Platform.runLater(() -> {
			double income = 1E308;
			double expenses = 1E307; // Ensuring positive savings

			controller.calculateSavings(income, expenses);

			assertTrue(controller.lblActualSavings.getText().startsWith("9E307"), "Actual savings incorrect for extremely large values.");
			assertTrue(controller.lblSavingsGoalStatus.getText().contains("Savings Goal Reached"), "Status message incorrect for large values.");
		});
		Thread.sleep(100);
	}

	@Test
	public void testDecimalValuesForIncomeAndExpenses() throws InterruptedException {
		Platform.runLater(() -> {
			double income = 999.99;
			double expenses = 499.99;

			controller.calculateSavings(income, expenses);

			assertEquals("500.00", controller.lblActualSavings.getText(), "Actual savings incorrect for decimal income and expenses.");
			assertTrue(controller.lblSavingsGoalStatus.getText().contains("Savings Goal Reached"), "Status message incorrect for decimal values.");
		});
		Thread.sleep(100);
	}

	@Test
	public void testNoSavingsScenario() throws InterruptedException {
		Platform.runLater(() -> {
			double income = 1000;
			double expenses = 1000;

			controller.calculateSavings(income, expenses);

			assertEquals("0.00", controller.lblActualSavings.getText(), "Actual savings should be zero in a no savings scenario.");
			assertTrue(controller.lblSavingsGoalStatus.getText().contains("Savings Goal Not Reached"), "Status message incorrect for no savings scenario.");
		});
		Thread.sleep(100);
	}
	@Test
	public void testSavingsLessThanGoal() throws InterruptedException {
		Platform.runLater(() -> {
			double income = 1500;
			double expenses = 1200;
			// Assuming the goal is set to something greater than the savings, like 400
			double goal = 400; // This value is for logical comparison

			controller.calculateSavings(income, expenses);

			assertTrue(Double.parseDouble(controller.lblActualSavings.getText()) < goal, "Actual savings should be less than the savings goal.");
			assertTrue(controller.lblSavingsGoalStatus.getText().contains("Savings Goal Not Reached"), "Status message incorrect for savings less than goal.");
		});
		Thread.sleep(100);
	}
	@Test
	public void testNegativeSavingsGoal() throws InterruptedException {
		Platform.runLater(() -> {
			double income = 1000;
			double expenses = 800;
			// Assume goal can be negative, which might be handled as an error
			double goal = -100; // For comparison purposes

			controller.calculateSavings(income, expenses);

			assertTrue(Double.parseDouble(controller.lblActualSavings.getText()) > goal, "Actual savings should be considered reached if the goal is negative.");

		});
		Thread.sleep(100);
	}
	@Test
	public void testVariableExpenses() throws InterruptedException {
		double income = 2000;
		for (double expenses = 100; expenses <= 1900; expenses += 100) {
			double finalExpenses = expenses; // Necessary for lambda capture
			Platform.runLater(() -> {
				controller.calculateSavings(income, finalExpenses);

				double expectedSavings = income - finalExpenses;
				assertEquals(String.format("%.2f", expectedSavings), controller.lblActualSavings.getText(), "Actual savings incorrect for expenses: " + finalExpenses);
			});
			Thread.sleep(100); // Adjust timing as necessary for your test environment
		}
	}

}
