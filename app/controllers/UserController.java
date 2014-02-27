package controllers;

import static play.data.Form.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;


//import org.apache.bcel.generic.ReturnaddressType;




import org.mindrot.jbcrypt.BCrypt;

//import controllers.LoginController.Login;
import models.User;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.DynamicForm;

public class UserController extends Controller {
	
	public static class Global {
		public static long lUpdateId;
	}
	
	/**
	 * Put all users (found by the findAll-method) in a Collection and render the user-view
	 * @return
	 */
	@Transactional(readOnly=true)
	@Security.Authenticated(Secured.class)
    public static Result users() {
		Logger.info("Start");
		Collection<User> users = User.findAll();
		Logger.info("User size: " + users.size());
        return ok(user.render("Alle Benutzer", users, User.findByName(request().username())));
    }
	
	/**
	 * Find an user by the in the URL given id with the finById method and render the oneuser-view
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	@Security.Authenticated(Secured.class)
	public static Result finduser(long id) {
		Logger.info("Start");
		User user = User.findById(id);
		Logger.info("User searched for: " + user);
		if (user==null) {
			return badRequest("Der Benutzer mit der id '" + id + "' existiert nicht!");
		}else {
			return ok(oneuser.render("Benutzer mit der id " + id, user, User.findByName(request().username())));
		}
	}
	
	
	
	/**
	 * render the newuser-view
	 * @return
	 */
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result newuser() {
		Form<User> userForm = form(User.class);
		return ok(newuser.render("", userForm, User.findByName(request().username())));
	}
	
	/**
	 * handle the newuser-POST-request
	 * @return
	 */
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result save() {
		final DynamicForm form = form().bindFromRequest();
		final String name = form.get("name");
		final String pwHash = BCrypt.hashpw(form.get("pw"), BCrypt.gensalt());
		
	    if (form.hasErrors()) {
	        return badRequest("Mit den eingegebenen Werten stimmt etwas nicht.");
	    }else{
	    	if(User.userExist(name)) {
	    		flash("error", "User " + name + " has not been created. User " + name + " exists already!");
	    		return redirect(routes.UserController.newuser());
	    	}else{
	    		if(name.isEmpty() || pwHash.isEmpty()) {
		    		flash("error", "username or password is emty.");
					return redirect(routes.UserController.newuser());
	    		}else {				
					User.add(name, pwHash);
		    		flash("success", "User " + name + " has been created");
		    		return redirect(routes.UserController.users());
	    		}
	    	}
	    }
	}
	
	/**
	 * show the update-user-page
	 * @param id
	 * @return
	 */
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result updateShow(long id) {
		User user = User.findById(id);
		Global.lUpdateId=id;
		if (user!=null) {
			return ok(update.render("Benutzer mit id '" + user.id + "' bearbeiten", user, User.findByName(request().username())));
		}else {
			return badRequest("Der Benutzer den Sie editieren wollen existiert nicht!");
		}
	}
	
	/**
	 * handle the update-user-POST-request
	 * @return
	 */
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result update() {
		DynamicForm form = form().bindFromRequest();
		String name = form.get("name");
		String pw = form.get("pw");
		final String pwHash = BCrypt.hashpw(form.get("pw"), BCrypt.gensalt()); 
		User udUser = User.findById(Global.lUpdateId);
		if (!User.userExist(name) || name.equals(udUser.name)) {
			if (name.isEmpty() || pw.isEmpty()) {
				flash("error", "username or password is empty.");
				return redirect(routes.UserController.updateShow(Global.lUpdateId));
			}else {
				udUser.update(name, pwHash);
				flash("success", "Benutzer " + name + " wurde aktuallisiert");
				return redirect(routes.UserController.users());
			}
		}else {
			flash("error", "User " + name + " has not been updated. User " + name + " exists already!");
			return redirect("/user/update/" + Global.lUpdateId);
		}
	}
	
	/**
	 * delete a user by his id
	 * @param id
	 * @return
	 */
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result delete(long id){
		User user = User.findById(id);
		if (user==null)
		{
			return badRequest("Der Benutzer mit der id '" + id + "' existiert nicht!");
		}else{
			flash("warning", "user " + user.name + " wurde geloescht");
			user.delete();
			return redirect(routes.UserController.users());
		}
	}

}