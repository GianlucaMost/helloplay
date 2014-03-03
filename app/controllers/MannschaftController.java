package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public static Result mannschaften() {
		return ok(mannschaften_tbl.render(Mannschaft.findAll(), User.findByName(request().username())));
    }
	
	@Transactional(readOnly=true)
	public static Result mannschaftShow(long mid) {
		return ok(mannschaft.render(Mannschaft.findById(mid), User.findByName(request().username())));
	}
}