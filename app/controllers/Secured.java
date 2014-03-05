package controllers;

import	play.*;
import	play.mvc.*;
import play.mvc.Http.Context;
import models.*;

public class Secured extends Security.Authenticator {
	
	@Override
	public String getUsername (Context ctx) {
		return ctx.session().get("name");
	}
	
	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.LoginController.login());
	}
}