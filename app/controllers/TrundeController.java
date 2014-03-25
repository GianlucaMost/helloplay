package controllers;

import static play.data.Form.form;

import java.awt.Desktop;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.DynamicForm;

@Security.Authenticated(Secured.class)
public class TrundeController extends Controller {
	
	/**
	 * Shows the Trunde-Overview onGET
	 * @return
	 */
	@Transactional
    public static Result showDetail(int trid) {
		Trunde tr = Trunde.findById(trid);
		if(tr!=null){
			return ok(trunde_detail.render(Spiel.findAll(), tr, User.findByName(request().username())));
		}else{
			Logger.info("Diese TippRunde mit der id " + trid + "existiert nicht.");
			return badRequest("Diese TippRunde mit der id '" + trid + "' existiert nicht mehr!");
		}
	}
	
	@Transactional
    public static Result showMain() {
		return ok(trunde_main.render(User.findByName(request().username())));
	}
	
	@Transactional
    public static Result addNew(int uid) {
		final DynamicForm form = form().bindFromRequest();
		final String b = form.get("bezeichnung");
		User u = User.findById(uid);
		Trunde tr = new Trunde(b, u);
		Logger.info("Benutzer " + u.name + " (" + u.uid + ") moechte neue TippRunde " + b + " erstellen.");
		tr.persist();
		u.addTrunde(tr);
		Logger.info("Benutzer " + u.name + " ist nun in TippRunde " + tr.bezeichnung + ".");
		flash("success", "TippRunde " + tr.bezeichnung + " wurde erstellt.");
		return redirect(routes.TrundeController.showDetail(tr.trid));
	}
	
	@Transactional
    public static Result removeTrunde(int trid) {
		Trunde tr = Trunde.findById(trid);
		String tmp = tr.bezeichnung;
		Logger.info("TippRunde " + tr.bezeichnung + "wird geloscht. Admin is " + tr.getTrAdmin().name + " (" + tr.getTrAdmin().uid + ").");
		tr.delete();
		flash("info", "TippRunde " + tmp + " wurde geloescht!");
		return redirect(routes.TrundeController.showMain());
	}
	
	@Transactional
    public static Result joinTrunde(int trid) {
		User u = User.findByName(request().username());
		Trunde tr = Trunde.findById(trid);
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