import org.testng.annotations.Test;
import java.util.ArrayList;
import static org.testng.Assert.assertEquals;

import models.TimePeriod;


public class TimePeriodTest {
TimePeriod	timePeriod = new TimePeriod();

	@Test
	public void testSetAndGetYear() {
		int year = 2023;
		timePeriod.setYear(year);
		assertEquals(year, timePeriod.getYear());
	}

	@Test
	public void testSetAndGetMonth() {
		int month = 5;
		timePeriod.setMonth(month);
		assertEquals(month, timePeriod.getMonth());
	}
}
