package controllers;

import dao.UserDao;
import dao.UserDaoImpl;
import	play.*;
import	play.mvc.*;
import play.mvc.Http.Context;
import models.*;



public class AdminSecured extends Security.Authenticator {
	
	private static UserDao userDao = new UserDaoImpl();
	
	@Override
	public String getUsername (Context ctx) {
		String name = ctx.session().get("name");
		User cU = userDao.findByName(name);
		if (cU!=null && cU.admin==1){
			return name;
		}else{
			return null;
		}
	}
	
	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Application.index());
	}
}
