import models.TimePeriod;
import models.instances.ExpenseInstance;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
public class ExpenseInstanceTest {

	ExpenseInstance expenseInstance = new ExpenseInstance();

	@Test
	public void testSetAndGetID() {
		int id = 123;
		expenseInstance.setID(id);
		assertEquals(id, expenseInstance.getID());
	}

	@Test
	public void testSetAndGetMonth() {
		TimePeriod timePeriod = new TimePeriod();
		expenseInstance.setMonth(timePeriod);
		assertEquals(timePeriod, expenseInstance.getMonth());
	}
}
