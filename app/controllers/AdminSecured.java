package controllers;

import	play.*;
import	play.mvc.*;
import play.mvc.Http.Context;
import models.*;



public class AdminSecured extends Security.Authenticator {
	
	@Override
	public String getUsername (Context ctx) {
		String name = ctx.session().get("name");
		User cU = User.findByName(name);
		if (cU!=null && cU.admin==1){
			return name;
		}else{
			return null;
		}
	}
	
	@Override
	public Result onUnauthorized(Context ctx) {
//		flash("warning", "Sie muessen sich anmelden/registriere num diesen Inhalt sehen zu koennen!");
		return redirect(routes.Application.index());
	}
}
