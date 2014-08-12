package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.NoResultException;

import org.mindrot.jbcrypt.BCrypt;

import dao.*;
import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.db.jpa.Transactional;
import views.html.*;
import play.data.DynamicForm;



@Security.Authenticated(Secured.class)
public class TippController extends Controller {
	
	private static TippDao tippDao = new TippDaoImpl();
	private static SpielDao spielDao = new SpielDaoImpl();
	private static UserDao userDao = new UserDaoImpl();
	
	private static User cU = userDao.findByName(request().username());
	
	@Transactional
	public static Result tippen(int sid, int uid){
		String refererHeader = request().headers().get("Referer")[0];
		
		try {
		    final DynamicForm form = form().bindFromRequest();
			final byte th = Byte.parseByte(form.get("toreHeim"));
			final byte tg = Byte.parseByte(form.get("toreGast"));
			
			Collection<Tipp> tipps = (Collection<Tipp>) Tipp.findAll();
			
			for(Tipp t: tipps){
				if(t.getSpiel().equals(Spiel.findById(sid)) && t.getUser().equals(User.findById(uid)) && th >=0 && tg >=0){
					t.update(th, tg);
					flash("tippSuccess", "Ihr Tipp " + t.getSpiel().getMannschaftHeim().bezeichnung + " - " + t.getSpiel().getMannschaftGast().bezeichnung + " wurde aktuallisiert");
					return redirect(refererHeader + "#stSpielplan");
				}
			}
			
			if (th >=0 && tg >=0){
				Tipp newTipp = new Tipp(User.findById(uid), Spiel.findById(sid), th, tg);
				newTipp.add();
				flash("tippSuccess", "Ihr Tipp " + newTipp.getSpiel().getMannschaftHeim().bezeichnung + " - " + newTipp.getSpiel().getMannschaftGast().bezeichnung + " wurde abgegeben");
			}else {
				flash("tippError", "Bitte gueltige Toranzahl angeben");
			}
		    return redirect(refererHeader + "#stSpielplan");
		} catch (NumberFormatException ex) {
			Logger.info("User: " + User.findById(uid).name + " hat ungueltigen Tipp abgegeben");
			Logger.error(ex.toString());
			flash("tippError", "Ihr Tipp ist ungueltig. Die Werte sind so falsch.");
    		return redirect(refererHeader + "#stSpielplan");
    	}
	}
	
	@Transactional
	public static Result showTipps(){
		Collection<Spiel> games = spielDao.findAll();
		Collection<Tipp> sortedTipps = cU.findSortedTipps();
		return ok(tipps.render(games, cU, sortedTipps));
	}
}