package controllers;

import java.io.IOException;
import java.util.Collection;

import models.User;
import play.*;
import play.mvc.*;
import play.db.jpa.Transactional;
import views.html.*;

public class Application extends Controller {
	
	@Transactional
    public static Result index() {
		return ok(home.render("Home", User.findByName(request().username())));
	}
}
