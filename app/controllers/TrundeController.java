package controllers;

import static play.data.Form.form;

import java.net.HttpURLConnection;
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
    public static Result showOverview(int trid) {
		Trunde tr = Trunde.findById(trid);
		if(tr!=null){
			return ok(trunde_overview.render(Spiel.findAll(), tr, User.findByName(request().username())));
		}else{
			Logger.info("Diese TippRunde mit der id " + trid + "existiert nicht.");
			return badRequest("Diese TippRunde mit der id '" + trid + "' existiert nicht mehr!");
		}
	}
}