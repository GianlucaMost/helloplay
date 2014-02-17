package controllers;

import static play.data.Form.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

//import controllers.LoginController.Login;
import models.User;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.DynamicForm;

public class UserController extends Controller
{
	/**
	 * Put all users (found out from the findAll-method) in a Collection and render the user-view
	 * @return
	 */
	@Transactional(readOnly=true)
    public static Result users()
	{
		Logger.info("Start");
		Collection<User> users = User.findAll();
		Logger.info("User size: " + users.size());
        return ok(user.render("Alle Benutzer anzeigen", users));
    }
	
	/**
	 * Find an user by the in the URL given id with the finById method and render the oneuser-view
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public static Result finduser(long id)
	{
		Logger.info("Start");
		User user = User.findById(id);
		Logger.info("User searched for: " + user);
		return ok(oneuser.render("Bestimmten Benutzer anzeigen", user));
		
	}
	/**
	 * render the newuser-view
	 * @return
	 */
	@Transactional
	public static Result newuser()
	{
		Form<User> userForm = form(User.class);
		return ok(newuser.render(userForm));
	}
	
	/**
	 * handle the newuser-POST-request
	 * @return
	 */
	@Transactional
	public static Result save()
	{
		final DynamicForm form = form().bindFromRequest();
		final String name = form.get("name");
		final String pw = form.get("pw");
		User.add(name, pw);
	    if (form.hasErrors()) {
	        return badRequest();
	    } else
	    {	    
	        flash("success", "User " + form.get("name") + " has been created");
			
			Collection<User> users = User.findAll();
			return ok(user.render("Alle Benutzer anzeigen", users));
	    }
	}
	/**
	 * show the update-user-page
	 * @param id
	 * @return
	 */
	public static Result updatePage(long id)
	{
		User user = User.findById(id);
		return ok(update.render(user));
	}
	
	/**
	 * handle the update-user-POST-request
	 * @return
	 */
	public static Result update()
	{
		return ok();
	}
	
	@Transactional
	public static Result delete(long id)
	{
		User user = User.findById(id);
		user.delete();
		return ok(delete.render("Benutzer loeschen", user));
	}

}