import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.ning.http.client.Request;

import controllers.TippController;

import org.junit.*;

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
	
//	@Test
//	public void tippen() {
//	  running(fakeApplication(), new Runnable() {
//	    public void run() {
//	    	Result result = callAction(
//	    			controllers.routes.ref.TippController.tippen(1, 35)
//	    		);
////	    		assertThat(status(result)).isEqualTo(OK);
////	    		assertThat(contentType(result)).isEqualTo("text/html");
////	    	    assertThat(charset(result)).isEqualTo("utf-8");
//	    }
//	  });
//	}
	
	@Test
	public void tippen() {
	  running(fakeApplication(), new Runnable() {
	    public void run() {
//	    	Result result = callAction(
//	    			controllers.routes.ref.TippController.tippen(1, 35)
//	    		);
//	    	HashMap<String,String> data = new HashMap<String, Object>();
	    	HashMap<String, String> data = new HashMap<String, String>();
	        data.put("toreHeim", "1");
	        data.put("toreGast", "2");
	        
//	        Result ini = callAction(controllers.routes.ref.Application.index());
	        Result result = callAction(
	        	controllers.routes.ref.TippController.tippen(1, 35),
	            fakeRequest().withFormUrlEncodedBody(data).withSession("name", "user").withHeader("Referer", "/")
	        );
	        
//	        String newURL= redirectLocation(result);
	        
//	        Result newResult = route(fakeRequest(GET, newURL));
	        
//	        Result result = routeAndCall(fakeRequest().withFormUrlEncodedBody(data));
	        assertThat(status(result)).isEqualTo(303);
	        assertThat(flash(result).containsKey("tippSuccess")).isTrue();
//	        assertThat(status(newResult)).isEqualTo(200);
//	        assertThat(flash(result).isEmpty()).isTrue();
	    }
	  });
	}
}
