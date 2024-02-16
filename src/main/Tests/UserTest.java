package Tests;

import models.money.User;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;



public class UserTest {

	 User user = new User();


	@Test
	public void testSetAndGetUsername() {
		String username = "testUser";
		user.setUsername(username);
		Assert.assertEquals(username, user.getUsername());
	}
	@Test
	public void testSetAndGetCountry() {
		String country = "TestCountry";
		user.setCountry(country);
		Assert.assertEquals(country, user.getCountry());
	}
	@Test
	public void testSetAndGetEmail() {
		String email = "test@example.com";
		user.setEmail(email);
		Assert.assertEquals(email, user.getEmail());
	}
	@Test
	public void testSetAndGetName() {
		String name = "TestName";
		user.setName(name);
		Assert.assertEquals(name, user.getName());
	}



}