package controllers;

import static play.data.Form.form;

import java.util.Collection;
import dao.SpielDao;
import dao.SpielDaoImpl;
import dao.TrundeDao;
import dao.TrundeDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
import models.*;
import play.*;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.DynamicForm;

@Security.Authenticated(Secured.class)
public class TrundeController extends Controller {
	
	private static TrundeDao trundeDao = new TrundeDaoImpl();
	private static UserDao userDao = new UserDaoImpl();
	private static SpielDao spielDao = new SpielDaoImpl();
	
	/**
	 * Shows the Trunde-Overview onGET
	 * @return
	 */
	@Transactional
    public static Result showDetail(int trid) {
		String msg = "Dies TippRunde mit der id " + trid + "existiert nicht.";
		Trunde tr = trundeDao.findById(trid);
		Collection<User> sortedMember = trundeDao.findSortedMember(tr);
		Collection<Spiel> games = spielDao.findAll();
		User cU = userDao.findByName(request().username());
		if(tr!=null){
			return ok(trunde_detail.render(games, tr, cU, sortedMember));
		}else{
			Logger.info(msg);
			return badRequest(msg);
		}
	}
	
	@Transactional
    public static Result showMain() {
		User cU = userDao.findByName(request().username());
		return ok(trunde_main.render(cU));
	}
	
	@Transactional
    public static Result addNew(int uid) {
		final DynamicForm form = form().bindFromRequest();
		final String b = form.get("bezeichnung");
		User u = userDao.findById(uid);
		Trunde tr = new Trunde(b, u);
		Logger.info("Benutzer " + u.name + " (" + u.uid + ") moechte neue TippRunde " + b + " erstellen.");
		trundeDao.persist(tr);
		u.addTrunde(tr);
		Logger.info("Benutzer " + u.name + " ist nun in TippRunde " + tr.bezeichnung + ".");
		flash("success", "TippRunde " + tr.bezeichnung + " wurde erstellt.");
		return redirect(routes.TrundeController.showDetail(tr.trid));
	}
	
	@Transactional
    public static Result removeTrunde(int trid) {
		Trunde tr = trundeDao.findById(trid);
		String tmp = tr.bezeichnung;
		Logger.info("TippRunde " + tr.bezeichnung + "wird geloscht. Admin is " + tr.getTrAdmin().name + " (" + tr.getTrAdmin().uid + ").");
		trundeDao.delete(tr);
		flash("info", "TippRunde " + tmp + " wurde geloescht!");
		return redirect(routes.TrundeController.showMain());
	}
	
	@Transactional
    public static Result joinTrunde(int trid) {
		User u = userDao.findByName(request().username());
		Trunde tr = trundeDao.findById(trid);
		if (!u.getTrunden().contains(tr)){
			u.addTrunde(tr);
			Logger.info(u.name + " tritt der TippRunde " + tr.bezeichnung + " (" + tr.trid + ") bei.");
			flash("success", "Sie sind der TippRunde " + tr.bezeichnung + " beigetreten.");
		}else{
			Logger.info(u.name + " (" + u.uid + ") befindet sich bereits in TippRunde " + tr.bezeichnung + " (" + tr.trid + ").");
			flash("error",	"Sie befinden sich bereits in der TippRunde " + tr.bezeichnung);
		}
		return redirect(routes.TrundeController.showMain());
	}
}