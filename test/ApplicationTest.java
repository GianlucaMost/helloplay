import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
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
  
//    public void renderTemplate() {
//        Content html = views.html.index.render("Your new application is ready.");
//        assertThat(contentType(html)).isEqualTo("text/html");
//        assertThat(contentAsString(html)).contains("Your new application is ready.");
//    }

//	@Test
//	public void renderTemplate() {
//	  Content html = views.html.index.render("Coco");
//	  //home.render(spielDao.findAll(), mannschaftDao.findAll(), cU)
//	  Content html = views.html.home.render(arg0, arg1, arg2)
//	  assertThat(contentType(html)).isEqualTo("text/html");
//	  assertThat(contentAsString(html)).contains("Coco");
//	}

	/**
	 * Testet die tipp-funktion
	 */
	@Test
	public void tippen() {
//		inMemoryDatabase("default", ImmutableMap.of("MODE", "MySQL"));
		
	/*
	 * Starte eine fakeApplication
	 */
	running(fakeApplication(inMemoryDatabase("default", ImmutableMap.of("MODE", "MySQL", "IGNORECASE", "TRUE"))), new Runnable() {
		public void run() {
	    	//Input simulation
	    	HashMap<String, String> data = new HashMap<String, String>();
	    	//positives Beispiel
	        data.put("toreHeim", "1");
	        data.put("toreGast", "1");
	        
	        Result resPo = callAction(
	        	controllers.routes.ref.TippController.tippen(1, 35),
	            fakeRequest().withFormUrlEncodedBody(data).withSession("name", "user").withHeader("Referer", "/")
	        );
	        
	        assertThat(status(resPo)).isEqualTo(303);
	        assertThat(flash(resPo).containsKey("tippSuccess")).isTrue();
	        
	        //negatives Beispiel
	        data.put("toreHeim", "-1");
	        data.put("toreGast", "-0");
	        
	        Result resNeg = callAction(
	        	controllers.routes.ref.TippController.tippen(1, 35),
	            fakeRequest().withFormUrlEncodedBody(data).withSession("name", "user").withHeader("Referer", "/")
	        );
	        
	        assertThat(flash(resNeg).containsKey("tippError")).isTrue();
	    }
	  });
	}
	
//	@Test
//	public void createTrunde() {
//		running(
//			fakeApplication(inMemoryDatabase(
//			"test",
//			ImmutableMap.of("MODE", "MySQL", "IGNORECASE", "TRUE"))),
//			new Runnable() {
//				public void run() {
//					//Input simulation
//				   	HashMap<String, String> data = new HashMap<String, String>();
//				   	//positives Beispiel
//				   	data.put("bezeichnung", "hallo");
//				        
//					Result result = callAction(
//						controllers.routes.ref.TrundeController.addNew(35),
//						fakeRequest().withFormUrlEncodedBody(data).withSession("name", "user")
//					);
//						
//					assertThat(flash(result).containsKey("success")).isTrue();
//				}
//			}
//		);
//	} 
	
	@Test
	public void removeTrunde() {
		
	}
	
	
	
	
	
	
	
	
	
	
}
