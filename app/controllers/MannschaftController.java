package controllers;

import static play.data.Form.form;

import java.util.Collection;

import org.mindrot.jbcrypt.BCrypt;

import models.*;

import play.*;
import play.mvc.*;
import play.data.Form;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.DynamicForm;

@Security.Authenticated(Secured.class)
public class MannschaftController extends Controller {
	
	public static class Global {
		
	}
	
	/**
	 * Listet alle Mannschaften auf
	 * @return
	 */
	@Transactional(readOnly=true)
    public static Result Mannschaften() {
		Logger.info("Start");
		Collection<Mannschaft> mannschaften = Mannschaft.findAll();
		Logger.info("Mannschafts size: " + mannschaften.size());
        return ok(home.render("Mannschaften", mannschaften, User.findByName(request().username())));
    }

}