package controllers;

import java.util.Collection;
import java.security.MessageDigest;

import org.mindrot.jbcrypt.*;

import dao.UserDao;
import dao.UserDaoImpl;
import models.User;
import play.*;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.*;
import static play.data.Form.*;

public class LoginController extends Controller
{	
	private static UserDao userDao = new UserDaoImpl();
	
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
	    	if (userDao.validate(name, pw)) {
		        session().clear();
		        session("name", name);
	    		Logger.info("user " + name + " logged in");
		        flash("success", "Sie sind jetzt angemeldet.");
		        return redirect(routes.Application.index());
	    	}else {
	    		Logger.info("Jemand hat versucht, sich mit dem username " + name + " anzumelden. Falsches Passwort!");
	    		flash("error", "Benutzername oder Passwort ist falsch.");
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
		Logger.info("Jemand hat sich abgemeldet");
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
	public static Result register_old() {
		final DynamicForm form = form().bindFromRequest();
		final String name = form.get("name");
		final String pw = form.get("pw");
		final String pwCon = form.get("pwCon");
		
		if(pw.equals(pwCon)) {
			final String pwHash = BCrypt.hashpw(form.get("pw"), BCrypt.gensalt());
		
		    if (form.hasErrors()) {
		        return badRequest("Mit den eingegebenen Werten stimmt etwas nicht.");
		    }else {
		    	if(userDao.userExist(name)) {
		    		flash("error", "Benutzer " + name + " wurde nicht erstellt. Dieser Name existiert bereits!");
		    		return redirect(routes.LoginController.showRegister());
		    	}else{
		    		if(name.isEmpty() || pwHash.isEmpty()) {
			    		flash("error", "username or password is emty.");
						return redirect(routes.LoginController.register());
		    		}else {				
		    			userDao.add(name, pwHash);
						session().clear();
				        session("name", name);
				        flash("success", "Registrierung erfolgreich.");
				        return redirect(routes.Application.index());
		    		}
		    	}
		    }
		}else {
			flash("error", "Die Passwörter müssen übereinstimmen!");
			return redirect(routes.LoginController.register());
		}
	}
	
	
	/**
	 * handle the register-POST-request
	 * @return
	 */
	@Transactional
	public static Result register() {
		final DynamicForm form = form().bindFromRequest();
		
		if (form.hasErrors()) {
	        return badRequest("Mit den eingegebenen Werten stimmt etwas nicht.");
	    }else {	
	    	final String name = form.get("name");
			final String pw = form.get("pw");
			final String pwCon = form.get("pwCon");
			
			if(name.isEmpty() || pw.isEmpty()) {
				flash("error", "Benutzername oder Passwort ist leer.");
				return redirect(routes.LoginController.register());
			}else {
		    	if(userDao.userExist(name)) {
		    		flash("error", "Benutzer " + name + " wurde nicht erstellt. Dieser Name existiert bereits!");
		    		return redirect(routes.LoginController.showRegister());
		    	}else{
		    		if(pw.equals(pwCon)) {
		    			final String pwHash = BCrypt.hashpw(form.get("pw"), BCrypt.gensalt());
		    			userDao.add(name, pwHash);
						session().clear();
				        session("name", name);
				        flash("success", "Registrierung erfolgreich.");
				        return redirect(routes.Application.index());
		    		}else {				
		    			flash("error", "Die Passwörter müssen übereinstimmen!");
						return redirect(routes.LoginController.register());
		    		}
		    	}
			}
	    }
	}
}