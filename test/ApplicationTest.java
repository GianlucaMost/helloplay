import java.sql.SQLException;
import java.util.HashMap;
import com.google.common.collect.ImmutableMap;
import org.junit.*;
import play.mvc.*;
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
		String createUser = "INSERT INTO user VALUES (1,'user','$2a$10$Icg8ysl4avuzHhY5nrwVhuGVG882i.OhkplBllbbv.X7zSm9kHZgm',0,0),(2,'admin','$2a$10$Icg8ysl4avuzHhY5nrwVhuGVG882i.OhkplBllbbv.X7zSm9kHZgm',0,1)";
//		String createMannschaften = "INSERT INTO `mannschaft` VALUES (1,'Brasilien','A',1,1,0,0,3,1,3,NULL),(2,'Kroatien','A',0,0,0,0,0,0,0,NULL)";
//		String createSpiel = "INSERT INTO spiel VALUES (1,1,2,0,0,'Sao Paulo','2020-06-12 20:00:00','2020-06-12 22:00:00',0,'gg1')";
		java.sql.Connection conn = play.db.DB.getConnection();
		try {
			java.sql.Statement stmt = conn.createStatement();
			try {
				stmt.execute(createUser);
//				stmt.execute(createMannschaften);
//				stmt.execute(createSpiel);
			} finally {
				stmt.close();
			}
		} finally {
			conn.close();
		}
	}
	
	/*
	 * Neuen Benutzer registrieren
	 */
	@Test
	public void register() {
		String value;
		Result res;
		HashMap<String, String> data = new HashMap<String, String>();	    
	    
		//case1, alle Daten ok
	    data.put("name", "eindeutig");
	    data.put("pw", "test");
	    data.put("pwCon", data.get("pw"));
	    res = registerRes(data);
	    assertThat(status(res)).as("register_http-state_case1-right-data").isEqualTo(303);
	    assertThat(flash(res).containsKey("success")).as("register_flashmessage_case1-right-data").isTrue();
	    value = flash(res).get("success");
	    assertThat(value.equals("Registrierung erfolgreich.")).as("Die flash-description ist bei der korrekten Anmeldung ist anders als erwartet").as("register_flash-message_case1-right-data").isTrue();
	    	    
	    data.clear();
	    
	    //case2, der Benutzername ist bereits vergeben
	    data.put("name", "user");
	    data.put("pw", "user");
	    data.put("pwCon", data.get("pw"));
		res = registerRes(data);
	    assertThat(status(res)).as("register_http-state_case2-name-in-use").isEqualTo(303);
	    assertThat(flash(res).containsKey("error")).as("register_flash-key_case2-name-in-use").isTrue();
	    value = flash(res).get("error");
	    assertThat(value.equals("Benutzer user wurde nicht erstellt. Dieser Name existiert bereits!")).as("register_flash-message_case2-name-in-use").isTrue();
	    
	    data.clear();
	    
	    //case3, das Passwort-Feld ist leer
	    data.put("name", "test");
	    data.put("pw", "");
	    data.put("pwCon", data.get("pw"));
		res = registerRes(data);
	    assertThat(status(res)).as("register_http-state_case3-pw-empty").isEqualTo(303);
	    assertThat(flash(res).containsKey("error")).as("register_flash-key_case3-pw-empty").isTrue();
	    value = flash(res).get("error");
	    assertThat(value.equals("Benutzername oder Passwort ist leer.")).as("register_flash-message_case3-pw-empty").isTrue();
		
	    data.clear();
	    
	    //case4, das Namens-Feld ist leer
		data.put("name", "");
	    data.put("pw", "test");
	    data.put("pwCon", data.get("pw"));
		res = registerRes(data);
	    assertThat(status(res)).as("register_http-state_case4-name-empty").isEqualTo(303);
	    assertThat(flash(res).containsKey("error")).as("register_flash-key_case4-name-empty").isTrue();
	    value = flash(res).get("error");
	    assertThat(value.equals("Benutzername oder Passwort ist leer.")).as("register_flash-message_case3-name-empty").isTrue();
		
	    data.clear();
	    
	    //case5, sowohl das Passwort- als auch das Namens-Feld ist leer
		data.put("name", "");
	    data.put("pw", "");
	    data.put("pwCon", data.get("pw"));
		res = registerRes(data);
	    assertThat(status(res)).as("register_http-state_case5-name-and-pw-empty").isEqualTo(303);
	    assertThat(flash(res).containsKey("error")).as("register_flash-key_case5-name-and-pw-empty").isTrue();
	    value = flash(res).get("error");
	    assertThat(value.equals("Benutzername oder Passwort ist leer.")).as("register_flash-message_case5-name-and-pw-empty").isTrue();
		
	    data.clear();
	    
	    //case6, die Passwortbestaetigung stimmt nicht mit dem Passwort ueberein
		data.put("name", "test");
		data.put("pw", "test");
		data.put("pwCon", "falsch");
		res = registerRes(data);
	    assertThat(status(res)).as("register_http-state_case6-pwCon-wrong").isEqualTo(303);
	    assertThat(flash(res).containsKey("error")).as("register_http-state_case6-pwCon-wrong").isTrue();
	    value = flash(res).get("error");
	    assertThat(value.equals("Die Passwörter müssen übereinstimmen!")).as("register_flash-message_case6-pwCon-wrong").isTrue();
	    
	    // Hilfsklasse schreiben, welche in einer for durchlaufen wird?!
	}
	
	public Result registerRes(HashMap<String, String> data) {
		Result res = callAction(
		    controllers.routes.ref.LoginController.register(),
		   	fakeRequest().withFormUrlEncodedBody(data)
		);
		return res;
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
	
	/*
	 * GRUNDFUNKTIONEN (views)
	 */
	
	@Test
	public void showMainpage() {
		//angemeldet als user
		Result res = callAction(controllers.routes.ref.Application.index(), fakeRequest().withSession("name", "user"));
		assertThat(status(res)).as("showMainpage_http-state_case1-logged-in-as-user").isEqualTo(200);
		assertThat(session(res)).as("showMainpage_session_case1-logged-in-as-user").isNotNull();
		//nicht angemeldet
		res = callAction(controllers.routes.ref.Application.index());
		assertThat(status(res)).as("showMainpage_http-state_case2-not-logged-in").isEqualTo(200);
		assertThat(session(res)).as("showMainpage_session_case2-not-logged-in").isNullOrEmpty();
		//angemeldet als admin
		res = callAction(controllers.routes.ref.Application.index(), fakeRequest().withSession("name", "admin"));
		assertThat(status(res)).as("showMainpage_http-state_case3-logged-in-as-admin").isEqualTo(303);
		assertThat(session(res)).as("showMainpage_session_case3-logged-in-as-admin").isNotNull();
	}
	
	@Test
	public void showMannschaf() {
		Result res = callAction(controllers.routes.ref.MannschaftController.mannschaften(), fakeRequest().withSession("name", "user"));
		assertThat(status(res)).isEqualTo(200);
	}	
	
	@Test
	public void findUser() {
		Result res = callAction(controllers.routes.ref.UserController.finduser(1), fakeRequest().withSession("name", "test"));
		assertThat(status(res)).as("findUser_http-state-case1-all-right").isEqualTo(200);
		res = callAction(controllers.routes.ref.UserController.finduser(-1), fakeRequest().withSession("name", "test"));
		assertThat(status(res)).as("findUser_http-state-case2-wrong-id").isEqualTo(400);
	}
	
	@Test
	public void userList() {
		Result res = callAction(controllers.routes.ref.UserController.users(), fakeRequest().withSession("name", "admin"));
		assertThat(status(res)).as("userList_http-state-case1-logged-in-as-admin").isEqualTo(200);
		res = callAction(controllers.routes.ref.UserController.users(), fakeRequest().withSession("name", "user"));
		assertThat(status(res)).as("userList_http-state-case2-logged-in-as-normal-user").isEqualTo(303);
	}
	
	@Test
	public void newUser() {
		Result res = callAction(controllers.routes.ref.UserController.newuser(), fakeRequest().withSession("name", "admin"));
		assertThat(status(res)).as("newUser_http-state-case1-logged-in-as-admin").isEqualTo(200);
		res = callAction(controllers.routes.ref.UserController.newuser(), fakeRequest().withSession("name", "user"));
		assertThat(status(res)).as("newUser_http-state-case2-logged-in-as-normal-user").isEqualTo(303);
	}
	
	@Test
	public void userUpdatePage() {
		Result res = callAction(controllers.routes.ref.UserController.updateShow(1), fakeRequest().withSession("name", "admin"));
		assertThat(status(res)).as("userUpdatePage_http-state-case1-admin-and-correct-id").isEqualTo(200);
		res = callAction(controllers.routes.ref.UserController.updateShow(-1), fakeRequest().withSession("name", "admin"));
		assertThat(status(res)).as("userUpdatePage_http-state-case2-admin-and-incorrect-id").isEqualTo(400);
		res = callAction(controllers.routes.ref.UserController.updateShow(1), fakeRequest().withSession("name", "user"));
		assertThat(status(res)).as("userUpdatePage_http-state-case3-logged-in-as-normal-user").isEqualTo(303);
	}
	
	@Test
	public void changePw() {
		Result res = callAction(controllers.routes.ref.UserController.changePwShow(), fakeRequest().withSession("name", "user"));
		assertThat(status(res)).as("changePw_http-state-case1-logged-in").isEqualTo(200);
		res = callAction(controllers.routes.ref.UserController.changePwShow());
		assertThat(status(res)).as("changePw_http-state-case2-not-logged-in").isEqualTo(303);
	}
	
}





