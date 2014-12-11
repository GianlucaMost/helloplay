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

	@BeforeClass
	public static void setUp() throws SQLException {
		start(fakeApplication(inMemoryDatabase("default", ImmutableMap.of("MODE", "MySQL"))));
//		start(fakeApplication(inMemoryDatabase("default")));
		
		// Initial Datensaetze
		String createUser = "INSERT INTO user VALUES (1,'user','$2a$10$Icg8ysl4avuzHhY5nrwVhuGVG882i.OhkplBllbbv.X7zSm9kHZgm',0,0)";
		String createMannschaften = "INSERT INTO `mannschaft` VALUES (1,'Brasilien','A',1,1,0,0,3,1,3,NULL),(2,'Kroatien','A',0,0,0,0,0,0,0,NULL)";
		String createSpiel = "INSERT INTO spiel VALUES (1,1,2,0,0,'Sao Paulo','2020-06-12 20:00:00','2020-06-12 22:00:00',0,'gg1')";
		java.sql.Connection conn = play.db.DB.getConnection();
		try {
			java.sql.Statement stmt = conn.createStatement();
			try {
				stmt.execute(createUser);
				stmt.execute(createMannschaften);
				stmt.execute(createSpiel);
			} finally {
				stmt.close();
			}
		} finally {
			conn.close();
		}
	}
	
	/*
	 * Neuen Benutzer anlegen
	 */
	@Test
	public void register() {
		HashMap<String, String> data = new HashMap<String, String>();
	    data.put("name", "user");
	    data.put("pw", "user");
	    
	    Result res = callAction(
	    	controllers.routes.ref.LoginController.register(),
	    	fakeRequest().withFormUrlEncodedBody(data)
	    );
	    
	    assertThat(status(res)).isEqualTo(303);
	    assertThat(flash(res).containsKey("error")).isTrue();
	}
	
	/*
	 * Benutzer loeschen
	 */
	@Test
	public void deleteUser() {
		Result res = callAction(
				controllers.routes.ref.UserController.delete(1),
				fakeRequest().withSession("name", "user")
		);
		
		assertThat(status(res)).isEqualTo(303);
		
	}
	
	/*
	 * tipp abgeben / aktualisieren
	 */
	@Test
	public void tippen() {
		HashMap<String, String> data = new HashMap<String, String>();
		//positives Beispiel
	    data.put("toreHeim", "1");
	    data.put("toreGast", "1");
	    
	    Result resPo = callAction(
	    	controllers.routes.ref.TippController.tippen(1, 1),
	        fakeRequest().withFormUrlEncodedBody(data).withSession("name", "user").withHeader("Referer", "/")
	    );
	    
	    assertThat(status(resPo)).isEqualTo(303);
	    assertThat(flash(resPo).containsKey("tippSuccess")).isTrue();
	    
	    //negatives Beispiel
	    data.put("toreHeim", "-1");
	    data.put("toreGast", "-0");
	    
	    Result resNeg = callAction(
	    	controllers.routes.ref.TippController.tippen(1, 1),
	        fakeRequest().withFormUrlEncodedBody(data).withSession("name", "user").withHeader("Referer", "/")
	    );
	    
	    assertThat(flash(resNeg).containsKey("tippError")).isTrue();
	}
	
	/*
	 * Neue Tipprunde erstellen
	 */
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





