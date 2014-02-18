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
//        return ok(index.render("Your new application is ready."));
        return ok(home.render("Home"));
    }

}
