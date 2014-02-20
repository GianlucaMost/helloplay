package controllers;

import java.util.Collection;

import models.User;
import play.*;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.*;
import static play.data.Form.*;

public class LoginController extends Controller
{	
	
	public static Result login() {
	    return ok(login.render(Form.form(models.User.class)));

	}
	
	@Transactional
	
	public static Result authenticate()
	{
	    Form<models.User> loginForm = Form.form(models.User.class).bindFromRequest();
	    
	    final DynamicForm form = form().bindFromRequest();
		final String name = form.get("name");
		final String pw = form.get("pw");
	    
	    if (form.hasErrors()) {
	        return badRequest(login.render(loginForm));
	    } else {
	    	if (User.validate(name, pw)) {
		        session().clear();
		        session("name", name);
		        return redirect("/");
	    	}else {
	    		flash("error", "username or password is wrong.");
//				return redirect(routes.LoginController.login());
				return badRequest(login.render(loginForm));
	    	}
	    }
	}
	
	public static Result logout() {
	    session().clear();
	    flash("success", "You've been logged out");
	    return redirect(routes.Application.index());
	}
}