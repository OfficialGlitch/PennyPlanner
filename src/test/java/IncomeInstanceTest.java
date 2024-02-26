import models.TimePeriod;
import models.instances.IncomeInstance;
import models.money.Income;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncomeInstanceTest {

	IncomeInstance instance = new IncomeInstance();

	@Test
	public void testSetAndGetAmount() {
		double testAmount = 200.0;
		instance.setAmount(testAmount);
		assertEquals(testAmount, instance.getAmount(), "The amount should match what was set");
	}

	@Test
	public void testSetAndGetProjected() {
		double projectedAmount = 150.0;
		instance.setProjected(projectedAmount);
		assertEquals(projectedAmount, instance.getProjected(), "The projected amount should match what was set");
	}
}
