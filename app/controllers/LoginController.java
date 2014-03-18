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
		        flash("success", "Sie sind jetzt angemeldet.");
		        return redirect("/");
	    	}else {
	    		flash("error", "username or password is wrong.");
				return badRequest(login.render(loginForm));
	    	}
	    }
	}
	
	/**
	 * logout the current user
	 * @return
	 */
	public static Result logout() {
	    session().clear();
	    flash("info", "Sie sind jetzt abgemeldet.");
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
		final String pw = form.get("pw");
		final String pwCon = form.get("pwCon");
		
		if(pw.equals(pwCon)){
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
		    			User user = new User(name, pwHash);
		    			User.add(user);
						session().clear();
				        session("name", name);
				        flash("success", "Registrierung erfolgreich.");
				        return redirect(routes.Application.index());
		    		}
		    	}
		    }
		}else{
			flash("error", "Die Passwoerter muessen ubereinstimmen!");
			return redirect(routes.LoginController.register());
		}
	}
}