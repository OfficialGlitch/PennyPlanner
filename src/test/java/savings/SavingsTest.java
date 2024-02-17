package savings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//import org.junit.Test;
//import static org.junit.Assert.*;

public class SavingsTest {

	@Test
	public void testCalculateSavings() {
		// Test case 1: Income > Expenses
		double income1 = 5000.0;
		double expenses1 = 3000.0;
		double expectedSavings1 = 2000.0;
		Assertions.assertEquals(expectedSavings1, Savings.calculateSavings(income1, expenses1), 0.0);

		// Test case 2: Income = Expenses
		double income2 = 4000.0;
		double expenses2 = 4000.0;
		double expectedSavings2 = 0.0;
		Assertions.assertEquals(expectedSavings2, Savings.calculateSavings(income2, expenses2), 0.0);

		// Test case 3: Income < Expenses
		double income3 = 2000.0;
		double expenses3 = 3000.0;
		double expectedSavings3 = -1000.0;
		Assertions.assertEquals(expectedSavings3, Savings.calculateSavings(income3, expenses3), 0.0);
	}
}
