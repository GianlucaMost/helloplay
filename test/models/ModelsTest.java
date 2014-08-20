package models;

import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication{
	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase()));
	}
	
//	@Test
//	public void createAndRetrieveUser(){
//		User user = new User("hans", "sicher");
//		user.add();
//		assertNotNull(user);
//		assertEquals("peter", user.name);
//	}
	
//	public void tryAuthenticateuser(){
//		User user = new User("hans", "sicher");
//		User.add(user);
//		
//		assertNotNull(User.validate(user.name, user.pw));
//	}
}
