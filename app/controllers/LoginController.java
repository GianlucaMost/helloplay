package controllers;

import java.util.Collection;
import java.security.MessageDigest;

import org.mindrot.jbcrypt.*;

import models.User;
import play.*;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.*;
import static play.data.Form.*;

public class LoginController extends Controller
{	
	/**
	 * show the login-page
	 * @return
	 */
	public static Result login() {
	    return ok(login.render(Form.form(models.User.class)));
	}
	
	/**
	 * handle the login-POST-request
	 * @return
	 */
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
		        flash("success", "You are logged in.");
		        return redirect("/");
	    	}else {
	    		flash("error", "username or password is wrong.");
				return badRequest(login.render(loginForm));
	    	}
	    }
	}
	
	/**
	 * logging out the current logged in user
	 * @return
	 */
	public static Result logout() {
	    session().clear();
	    flash("success", "You've been logged out.");
	    return redirect(routes.Application.index());
	}
	
	/**
	 * render the register-view
	 * @return
	 */
	@Transactional
	public static Result showRegister() {
		Form<User> userForm = form(User.class);
		return ok(register.render(userForm));
	}
	
	/**
	 * handle the register-POST-request
	 * @return
	 */
	@Transactional
	public static Result register() {
		final DynamicForm form = form().bindFromRequest();
		final String name = form.get("name");
		final String pwHash = BCrypt.hashpw(form.get("pw"), BCrypt.gensalt());
		
	    if (form.hasErrors()) {
	        return badRequest("Mit den eingegebenen Werten stimmt etwas nicht.");
	    }else{
	    	if(User.userExist(name)) {
	    		flash("error", "User " + name + " has not been created. User " + name + " exists already!");
	    		return redirect(routes.LoginController.showRegister());
	    	}else{
	    		if(name.isEmpty() || pwHash.isEmpty()) {
		    		flash("error", "username or password is emty.");
					return redirect(routes.LoginController.register());
	    		}else {				
					User.add(name, pwHash);
					session().clear();
			        session("name", name);
			        flash("success", "You are logged in.");
			        return redirect(routes.Application.index());
	    		}
	    	}
	    }
	}
}