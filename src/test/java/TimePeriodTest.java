import models.instances.ExpenseInstance;
import models.instances.IncomeInstance;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import models.TimePeriod;
import org.junit.jupiter.api.Test;


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
	@Test
	public void testProjectedIncome() {
		// Initialize income sources
		List<IncomeInstance> incomeSources = new ArrayList<>();
		IncomeInstance incomeInstance1 = new IncomeInstance();
		incomeInstance1.setProjected(1000.0);
		IncomeInstance incomeInstance2 = new IncomeInstance();
		incomeInstance2.setProjected(2000.0);
		incomeSources.add(incomeInstance1);
		incomeSources.add(incomeInstance2);
		timePeriod.setIncomeSources(incomeSources);

		assertEquals(3000.0, timePeriod.projectedIncome());
	}
	@Test
	public void testActualIncome() {
		// Initialize income sources
		List<IncomeInstance> incomeSources = new ArrayList<>();
		IncomeInstance incomeInstance1 = new IncomeInstance();
		incomeInstance1.setAmount(500.0);
		IncomeInstance incomeInstance2 = new IncomeInstance();
		incomeInstance2.setAmount(1000.0);
		incomeSources.add(incomeInstance1);
		incomeSources.add(incomeInstance2);
		timePeriod.setIncomeSources(incomeSources);

		assertEquals(1500.0, timePeriod.actualIncome());
	}

	@Test
	public void testProjectedExpense() {
		// Initialize expense instances
		List<ExpenseInstance> expenseInstances = new ArrayList<>();
		ExpenseInstance expenseInstance1 = new ExpenseInstance();
		expenseInstance1.setProjectedCost(200.0);
		ExpenseInstance expenseInstance2 = new ExpenseInstance();
		expenseInstance2.setProjectedCost(300.0);
		expenseInstances.add(expenseInstance1);
		expenseInstances.add(expenseInstance2);
		timePeriod.setExpenses(expenseInstances);

		assertEquals(500.0, timePeriod.projectedExpense());
	}

	@Test
	public void testProjectedBalance() {
		// Initialize income and expense instances
		List<IncomeInstance> incomeInstances = new ArrayList<>();
		IncomeInstance incomeInstance1 = new IncomeInstance();
		incomeInstance1.setProjected(1000.0);
		IncomeInstance incomeInstance2 = new IncomeInstance();
		incomeInstance2.setProjected(2000.0);
		incomeInstances.add(incomeInstance1);
		incomeInstances.add(incomeInstance2);
		timePeriod.setIncomeSources(incomeInstances);

		List<ExpenseInstance> expenseInstances = new ArrayList<>();
		ExpenseInstance expenseInstance1 = new ExpenseInstance();
		expenseInstance1.setProjectedCost(200.0);
		ExpenseInstance expenseInstance2 = new ExpenseInstance();
		expenseInstance2.setProjectedCost(300.0);
		expenseInstances.add(expenseInstance1);
		expenseInstances.add(expenseInstance2);
		timePeriod.setExpenses(expenseInstances);

		assertEquals(2500.0, timePeriod.projectedBalance());
	}
	@Test
	public void testActualExpense() {
		// Initialize expense instances
		List<ExpenseInstance> expenseInstances = new ArrayList<>();
		ExpenseInstance expenseInstance1 = new ExpenseInstance();
		expenseInstance1.setCost(100.0);
		ExpenseInstance expenseInstance2 = new ExpenseInstance();
		expenseInstance2.setCost(150.0);
		expenseInstances.add(expenseInstance1);
		expenseInstances.add(expenseInstance2);
		timePeriod.setExpenses(expenseInstances);

		assertEquals(250.0, timePeriod.actualExpense());
	}
}
