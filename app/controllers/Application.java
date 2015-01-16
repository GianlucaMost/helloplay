package controllers;

import dao.*;
import models.*;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;

public class Application extends Controller {
	
	private static MannschaftDao mannschaftDao = new MannschaftDaoImpl();
	private static UserDao userDao = new UserDaoImpl();
	private static SpielDao spielDao = new SpielDaoImpl();
	
	@Transactional
    public static Result index() {
		String name = session().get("name");
		User cU = userDao.findByName(name);
		if (cU==null) {
			return ok(home.render(spielDao.findAll(), mannschaftDao.findAll(), null));
		}else if(cU.admin==1) {
			return redirect(routes.UserController.users());
		}else {
			return ok(home.render(spielDao.findAll(), mannschaftDao.findAll(), cU));
		}
	}
}
