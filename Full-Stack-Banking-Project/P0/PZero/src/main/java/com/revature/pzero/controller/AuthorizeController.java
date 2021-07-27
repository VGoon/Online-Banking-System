package com.revature.pzero.controller;

import com.revature.pzero.models.User;

import io.javalin.http.Context;

public interface AuthorizeController {
	
	//customer logout - clears all cookies
	public void logout(Context ctx);
	
	//customer login - takes customer to customer's portal
	public void customerLogin(Context ctx);
	
	//employee login - takes employee to employee portal
	public void employeeLogin(Context ctx);
	
	//check credential - use to make tokens
	public User checkCredentials(Context ctx);
	
	//create an account
	public void userAccountCreation(Context ctx);
}
