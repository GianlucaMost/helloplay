package controllers;

import java.util.Collection;

import dao.MannschaftDao;
import dao.MannschaftDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
import models.*;
import play.Logger;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;

@Security.Authenticated(Secured.class)
public class MannschaftController extends Controller {
	
	private static MannschaftDao mannschaftDao = new MannschaftDaoImpl();
	private static UserDao userDao = new UserDaoImpl();
	
	/**
	 * Listet alle Mannschaften auf
	 * @return
	 */
	@Transactional(readOnly=true)
    public static Result mannschaften() {
		User cu = userDao.findByName(request().username());
		return ok(mannschaften_tbl.render(mannschaftDao.findAll(), cu));
    }
	
	@Transactional(readOnly=true)
	public static Result mannschaftShow(int mid) {
		User cu = userDao.findByName(request().username());
		try {
			Mannschaft m = mannschaftDao.findById(mid);
			Collection<Spiel> games = mannschaftDao.findGamesSorted(m);
			return ok(mannschaft.render(m, games, cu));
		} catch (NullPointerException e) {
			Logger.info("Es existiert keine Mannschaft mit der id " + mid);
			flash("error", "Es ist ein Fehler aufgetreten.");
	        return redirect(routes.Application.index());
		}
	}
}