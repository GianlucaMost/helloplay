package controllers;

import static play.data.Form.form;

import java.util.Collection;

import org.mindrot.jbcrypt.BCrypt;

import dao.TrundeDao;
import dao.TrundeDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
import models.Tipp;
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
	
	private static UserDao userDao = new UserDaoImpl();
	private static TrundeDao trundeDao = new TrundeDaoImpl();
	
//	private static User cU = userDao.findByName(request().username());
	
	/**
	 * Put all users (found by the findAll-method) in a Collection and render the user-view
	 * @return
	 */
	@Transactional(readOnly=true)
	@Security.Authenticated(AdminSecured.class)
    public static Result users() {
		Logger.info("Start searching for all user");
		Collection<User> users = userDao.findAll();
		Logger.info("User size: " + users.size());
        return ok(user.render("Alle Benutzer", users, userDao.findByName(request().username())));
    }
	
	/**
	 * Find an user by the in the URL given id with the finById method and render the oneuser-view
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public static Result finduser(int id) {
		User cU = userDao.findByName(request().username());
		
		Logger.info("Start searching for user with id " + id);
		User u = userDao.findById(id);
		Collection<Tipp> sortedTipps = userDao.findSortedTipps(u);
		String msg = "Der Benutzer mit der id" + id + "existiert nicht!";
		if (u==null) {
			Logger.info(msg);
			return badRequest(msg);
		}else {
			Logger.info("User searched for: " + u.name);
			return ok(oneuser.render("Benutzer mit der id " + id, u, cU, sortedTipps));
		}
	}
	
	@Transactional(readOnly=true)
	public static Result accverwaltung() {
		return ok(accverwaltung.render(userDao.findByName(request().username())));
	}
	
	/**
	 * toggle the admin-byte from the given user (set to 0 when 1 and set to 1 when 0)
	 * @param uid
	 * @return
	 */
	@Transactional
	@Security.Authenticated(AdminSecured.class)
	public static Result switchAdmin(int uid) {
		User u = userDao.findById(uid);
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
		User cU = userDao.findByName(request().username());
		
		User u = userDao.findById(uid);
		Trunde tr = trundeDao.findById(trid);
		u.removeFromTrunde(tr);
//		if(tr.getTrAdmin().equals(u)){
//			tr.setTrAdmin(null);
//		}
		if(u.equals(cU)){
			Logger.info("Benutzer " + u.name + " (" + u.uid + ") hat die TippRunde " + tr.bezeichnung + " verlassen");
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
		return ok(newuser.render("", userForm, userDao.findByName(request().username())));
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
	    	if(userDao.userExist(name)) {
	    		flash("error", "Benutzer " + name + " wurde nicht erstellt. Dieser Name existiert bereits!");
	    		return redirect(routes.UserController.newuser());
	    	}else{
	    		if(name.isEmpty() || pwHash.isEmpty()) {
		    		flash("error", "Benutzername oder Passwort darf nicht leer sein.");
					return redirect(routes.UserController.newuser());
	    		}else {				
					userDao.add(name, pwHash);
		    		flash("success", "Benutzer " + name + " wurde angelegt.");
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
		User user = userDao.findById(id);
		if (user!=null) {
			return ok(update.render(user.uid, user, userDao.findByName(request().username())));
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
		User udUser = userDao.findById(id);
		User curUser = userDao.findByName(request().username());
		if (!userDao.userExist(name) || name.equals(udUser.name)) {
			if (name.isEmpty() || pw.isEmpty()) {
				flash("error", "username or password is empty.");
				return redirect(routes.UserController.updateShow(id));
			}else {
				userDao.update(udUser, name, pwHash);
				flash("success", "Benutzer " + name + " wurde aktuallisiert");
				if (curUser.admin==1) {
					return redirect(routes.UserController.users());
				}else {
					return redirect(routes.UserController.accverwaltung());
				}
			}
		}else {
			flash("warning", "Benutzer wurde nicht aktuallisiert. Benutzername " + name + " bereits vergeben.");
			return redirect(routes.UserController.updateShow(id));
		}
	}
	
	/**
	 * handle the updateName-POST-request
	 * @return
	 */
	@Transactional
	public static Result updateName(int uid) {
		String name = form().bindFromRequest().get("name");
		User udUser = userDao.findById(uid);
		if (!userDao.userExist(name) || name.equals(udUser.name)) {
			if (name.isEmpty()) {
				flash("error", "Benutzername darf nicht leer sein!");
			}else {
				userDao.changeName(udUser, name);
				flash("success", "Benutzername wurde aktuallisiert.");
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
		return ok(changePw.render(userDao.findByName(request().username())));
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
		User udUser = userDao.findById(uid);
		if(udUser.checkPw(pwOld)){
			if (pw.isEmpty()) {
				flash("error", "Das Passwort ist ungueltig");
				return redirect(routes.UserController.changePw(uid));
			}else {
				if(pw.equals(pwCon)){
					userDao.changePw(udUser, pwHash);
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
		User delUser = userDao.findById(id);
		User curUser = userDao.findByName(request().username());
		if (delUser!=null){
			if(delUser.equals(curUser) || curUser.admin==1){
				flash("warning", "Benutzer " + delUser.name + " wurde geloescht.");
				userDao.delete(delUser);
			}
			if (curUser.admin==1){
				return redirect(routes.UserController.users());
			}else{
				return redirect(routes.Application.index());
			}
		}else{
			return badRequest("Der Benutzer mit der id '" + id + "' existiert nicht!");
		}
	}

}