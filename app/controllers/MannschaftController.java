package controllers;

import java.util.Collection;

import dao.MannschaftDao;
import dao.MannschaftDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
import models.*;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;

@Security.Authenticated(Secured.class)
public class MannschaftController extends Controller {
	
	private static MannschaftDao mannschaftDao = new MannschaftDaoImpl();
	private static UserDao userDao = new UserDaoImpl();
	private static User cu = userDao.findByName(request().username());
	
	/**
	 * Listet alle Mannschaften auf
	 * @return
	 */
	@Transactional(readOnly=true)
    public static Result mannschaften() {
		return ok(mannschaften_tbl.render(mannschaftDao.findAll(), cu));
    }
	
	@Transactional(readOnly=true)
	public static Result mannschaftShow(int mid) {
		Mannschaft m = mannschaftDao.findById(mid);
		Collection<Spiel> games = mannschaftDao.findGamesSorted(m);
//		User cu = userDao.findByName(request().username());
		return ok(mannschaft.render(m, games, cu));
	}
}