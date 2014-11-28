import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.ning.http.client.Request;

import controllers.TippController;
import models.Trunde;

import org.junit.*;
import org.specs2.specification.Fixture;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import views.html.home;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

	@Before
	public void setUp() throws SQLException{
		start(fakeApplication(inMemoryDatabase("default", ImmutableMap.of("MODE", "MySQL"))));

//		String sql = "DROP TABLE IF EXISTS mannschaft)";
//		java.sql.Connection conn = play.db.DB.getConnection();
//		try {
//			java.sql.Statement stmt = conn.createStatement();
//			try {
//				stmt.execute(sql);
//			} finally {
//				stmt.close();
//			}
//		} finally {
//			conn.close();
//		}
	}
	
//	@Test
//	public void createUser() {
//		HashMap<String, String> data = new HashMap<String, String>();
//	    data.put("name", "user");
//	    data.put("pw", "user");
//	    
//	    Result resPo = callAction(
//	    	controllers.routes.ref.UserController.save()
//	    );
//	    
//	    assertThat(status(resPo)).isEqualTo(303);
//	    assertThat(flash(resPo).containsKey("success")).isTrue();
//	}
	
//	@Test
//	public void tippen() {
//		HashMap<String, String> data = new HashMap<String, String>();
//		//positives Beispiel
//	    data.put("toreHeim", "1");
//	    data.put("toreGast", "1");
//	    
//	    Result resPo = callAction(
//	    	controllers.routes.ref.TippController.tippen(1, 35),
//	        fakeRequest().withFormUrlEncodedBody(data).withSession("name", "user").withHeader("Referer", "/")
//	    );
//	    
//	    assertThat(status(resPo)).isEqualTo(303);
//	    assertThat(flash(resPo).containsKey("tippSuccess")).isTrue();
//	    
//	    //negatives Beispiel
//	    data.put("toreHeim", "-1");
//	    data.put("toreGast", "-0");
//	    
//	    Result resNeg = callAction(
//	    	controllers.routes.ref.TippController.tippen(1, 35),
//	        fakeRequest().withFormUrlEncodedBody(data).withSession("name", "user").withHeader("Referer", "/")
//	    );
//	    
//	    assertThat(flash(resNeg).containsKey("tippError")).isTrue();
//	}
	
	@Test
	public void createTrunde() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("bezeichnung", "hallo");
		
		Result result = callAction(
			controllers.routes.ref.TrundeController.addNew(1),
			fakeRequest().withFormUrlEncodedBody(data).withSession("name", "user")
		);
						
		assertThat(flash(result).containsKey("success")).isTrue();
	} 
	
	@Test
	public void showMannschaf() {
		Result res = callAction(controllers.routes.ref.MannschaftController.mannschaften(), fakeRequest().withSession("name", "user"));
		assertThat(status(res)).isEqualTo(200);
	}	
}





