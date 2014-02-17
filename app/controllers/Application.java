package controllers;

import java.util.Collection;

import models.User;
import play.*;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;

public class Application extends Controller {

	@Transactional(readOnly=true)
    public static Result index() {
		Logger.info("Start");
		Collection<User> users = User.findAll();
		Logger.info("User size: " + users.size());
        return ok(index.render("Your new application is ready."));
    }

}
