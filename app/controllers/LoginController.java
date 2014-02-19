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
//	    return ok(login.render(Form.form(Login.class)));
	    return ok(login.render(Form.form(models.User.class)));

	}
	
	@Transactional
	public static Result authenticate()
	{
//	    Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
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
				return redirect(routes.LoginController.login());
	    	}
	    }
	}
	
//	public static class Login
//	{
//	    public String name;
//	    public String password;
//	    
//	    public String validate()
//		{
////		    if (User.authenticate(name, password) == null)
////		    {
////		      return "Invalid user or password";
////		    }
//		    return null;
//		}
//	}
}