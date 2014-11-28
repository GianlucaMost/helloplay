package models;

import java.sql.SQLException;

import models.*;

import org.junit.*;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication{
	@Before
	public void setUp() throws SQLException{
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
