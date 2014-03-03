package controllers;

import java.io.IOException;
import java.util.Collection;

import models.Mannschaft;
import models.User;
import play.*;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;

public class Application extends Controller {
	
	@Transactional
    public static Result index() {
		String name = session().get("name");
		User user = User.findByName(name);
		if (user==null)
		{
			return ok(home.render("Home", Mannschaft.findAll(), null));
		}else if(user.admin==1) {
			return redirect(routes.UserController.users());
		}else {
			return ok(home.render("Home", Mannschaft.findAll(), user));
		}
	}
}
