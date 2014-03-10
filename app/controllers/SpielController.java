package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Collection;
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
public class SpielController extends Controller {
	
	
}