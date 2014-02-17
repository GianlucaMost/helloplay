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
	
	public static Result authenticate()
	{
//	    Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
	    Form<models.User> loginForm = Form.form(models.User.class).bindFromRequest();
	    if (loginForm.hasErrors()) {
	        return badRequest(login.render(loginForm));
	    } else {
	        session().clear();
	        session("name", loginForm.get().name);
	        return redirect(routes.Application.index());
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