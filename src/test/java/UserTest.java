import models.Category;
import models.money.Expense;
import models.money.User;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.assertEquals;



public class UserTest {

	 User user = new User();


	@Test
	public void testSetAndGetUsername() {
		String username = "testUser";
		user.setUsername(username);
		assertEquals(username, user.getUsername());
	}
	@Test
	public void testSetAndGetCountry() {
		String country = "TestCountry";
		user.setCountry(country);
		assertEquals(country, user.getCountry());
	}
	@Test
	public void testSetAndGetEmail() {
		String email = "test@example.com";
		user.setEmail(email);
		assertEquals(email, user.getEmail());
	}
	@Test
	public void testSetAndGetName() {
		String name = "TestName";
		user.setName(name);
		assertEquals(name, user.getName());
	}
	@Test
	public void testSetAndGetExpenseCategories() {
		ArrayList<Category> categories = new ArrayList<>();
		Category category = new Category();
		categories.add(category);
		user.setExpenseCategories(categories);
		assertEquals(categories, user.getExpenseCategories());
	}
	@Test
	public void testSetAndGetExpenseTypes() {
		ArrayList<Expense> expenses = new ArrayList<>();
		Expense expense = new Expense();
		expenses.add(expense);
		user.setExpenseTypes(expenses);
		assertEquals(expenses, user.getExpenseTypes());
	}


}