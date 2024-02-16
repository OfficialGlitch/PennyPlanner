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
		assertEquals(username, user.getUsername());
	}

}