package controllers;

import static play.data.Form.form;

import java.util.Collection;

import org.mindrot.jbcrypt.BCrypt;

import models.Trunde;
import models.User;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.DynamicForm;

@Security.Authenticated(Secured.class)
public class UserController extends Controller {
	
	/**
	 * Put all users (found by the findAll-method) in a Collection and render the user-view
	 * @return
	 */
	@Transactional(readOnly=true)
	@Security.Authenticated(AdminSecured.class)
    public static Result users() {
		Logger.info("Start searching for all user");
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
	public static Result finduser(int id) {
		Logger.info("Start searching for user with id " + id);
		User user = User.findById(id);
		if (user==null) {
			Logger.info("Der Benutzer mit der id " + id + "existiert nicht.");
			return badRequest("Der Benutzer mit der id '" + id + "' existiert nicht!");
		}else {
			Logger.info("User searched for: " + user.name);
			return ok(oneuser.render("Benutzer mit der id " + id, user, User.findByName(request().username())));
		}
	}
	
	@Transactional(readOnly=true)
	public static Result accverwaltung() {
		return ok(accverwaltung.render(User.findByName(request().username())));
	}
	
	/**
	 * toggle the admin-byte from the given user (set to 0 when 1 and set to 1 when 0)
	 * @param uid
	 * @return
	 */
	@Transactional
	@Security.Authenticated(AdminSecured.class)
	public static Result switchAdmin(int uid) {
		User u = User.findById(uid);
		u.switchAdmin();
		String refererHeader = request().headers().get("Referer")[0];
		return redirect(refererHeader);
	}
	
	/**
	 * removes the given user(by uid) from the given trunde(by trid)
	 * @param uid
	 * @param trid
	 * @return
	 */
	@Transactional
	public static Result removeFromTrunde(int uid, int trid) {
		User u = User.findById(uid);
		User cU = User.findByName(request().username());
		Trunde tr = Trunde.findById(trid);
		u.removeFromTrunde(tr);
//		if(tr.getTrAdmin().equals(u)){
//			tr.setTrAdmin(null);
//		}
		if(u.equals(cU)){
			Logger.info("Benutzer " + u.name + " hat die TippRunde " + tr.bezeichnung + " verlassen");
			flash("info", "TippRunde " + tr.bezeichnung + " wurde verlassen.");
			return redirect(routes.TrundeController.showMain());
		}else{
			Logger.info("Benutzer " + u.name + " wurde von " + cU.name + " aus der TippRunde " + tr.bezeichnung + " gekickt");
			flash("info", "Benutzer " + u.name + " aus der TippRunde " + tr.bezeichnung + "  geworfen.");
			return redirect(routes.TrundeController.showDetail(tr.trid));
		}
	}
	
	
	/**
	 * render the newuser-view
	 * @return
	 */
	@Transactional
	@Security.Authenticated(AdminSecured.class)
	public static Result newuser() {
		Form<User> userForm = form(User.class);
		return ok(newuser.render("", userForm, User.findByName(request().username())));
	}
	
	/**
	 * handle the newuser-POST-request
	 * @return
	 */
	@Transactional
	@Security.Authenticated(AdminSecured.class)
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
					User.add(new User(name, pwHash));
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
	@Security.Authenticated(AdminSecured.class)
	public static Result updateShow(int id) {
		User user = User.findById(id);
		if (user!=null) {
			return ok(update.render(user.uid, user, User.findByName(request().username())));
		}else {
			return badRequest("Der Benutzer den Sie editieren wollen existiert nicht!");
		}
	}
	
	/**
	 * handle the update-user-POST-request
	 * @return
	 */
	@Transactional
	@Security.Authenticated(AdminSecured.class)
	public static Result update(int id) {
		DynamicForm form = form().bindFromRequest();
		String name = form.get("name");
		String pw = form.get("pw");
		final String pwHash = BCrypt.hashpw(form.get("pw"), BCrypt.gensalt()); 
		User udUser = User.findById(id);
		User curUser = User.findByName(request().username());
		if (!User.userExist(name) || name.equals(udUser.name)) {
			if (name.isEmpty() || pw.isEmpty()) {
				flash("error", "username or password is empty.");
				return redirect(routes.UserController.updateShow(id));
			}else {
				udUser.update(name, pwHash);
				flash("success", "Benutzer " + name + " wurde aktuallisiert");
				if (curUser.admin==1) {
					return redirect(routes.UserController.users());
				}else {
					return redirect(routes.UserController.accverwaltung());
				}
			}
		}else {
			flash("error", "User " + name + " has not been updated. User " + name + " exists already!");
			return redirect("/user/update/" + id);
		}
	}
	
	/**
	 * handle the updateName-POST-request
	 * @return
	 */
	@Transactional
	public static Result updateName(int uid) {
		String name = form().bindFromRequest().get("name");
		User udUser = User.findById(uid);
		if (!User.userExist(name) || name.equals(udUser.name)) {
			if (name.isEmpty()) {
				flash("error", "Benutzername darf nicht leer sein!");
			}else {
				udUser.update(name);
				flash("success", "Benutzername wurde aktuallisiert");
				session().clear();
				session("name", name);
			}
		}else {
			flash("error", "Benutzer " + name + " wurde nicht aktuallisiert (existiert bereits)!");
		}
		return redirect(routes.UserController.accverwaltung());
	}
	
	/**
	 * show the changePw page
	 * @param uid
	 * @return
	 */
	@Transactional
	public static Result changePwShow(int uid) {
		return ok(changePw.render(User.findByName(request().username())));
	}
	
	/**
	 * handle the changePw-POST-request
	 * @return
	 */
	@Transactional
	public static Result changePw(int uid) {
		DynamicForm form = form().bindFromRequest();
		String pwOld = form.get("pwOld");
		String pw = form.get("pw");
		String pwCon = form.get("pwCon");
		final String pwHash = BCrypt.hashpw(form.get("pw"), BCrypt.gensalt()); 
		User udUser = User.findById(uid);
		if(udUser.checkPw(pwOld)){
			if (pw.isEmpty()) {
				flash("error", "Das Passwort ist ungueltig");
				return redirect(routes.UserController.changePw(uid));
			}else {
				if(pw.equals(pwCon)){
					udUser.changePw(pwHash);
					flash("success", "Passwort wurde geaendert");
					return redirect(routes.UserController.accverwaltung());
				}else{
					flash("error", "Die Passwoerter muessen ubereinstimmen!");
					return redirect(routes.UserController.changePw(uid));
				}
			}
		}else{
			flash("error", "Passwort ungueltig!");
			return redirect(routes.UserController.changePw(uid));
		}
	}
	
	/**
	 * delete the user with the given id
	 * @param id
	 * @return
	 */
	@Transactional
	public static Result delete(int id){
		User delUser = User.findById(id);
		User curUser = User.findByName(request().username());
		if (delUser!=null){
			if(delUser.equals(curUser) || curUser.admin==1){
				flash("warning", "user " + delUser.name + " wurde geloescht");
				delUser.delete();
			}
			return redirect("/");
		}else{
			return badRequest("Der Benutzer mit der id '" + id + "' existiert nicht!");
		}
	}

}