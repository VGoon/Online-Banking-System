package com.revature.pzero.controller;

import com.revature.pzero.models.User;
import com.revature.pzero.service.BankSystem;
import com.revature.pzero.service.BankSystemImpl;

import io.javalin.http.Context;

public class AuthorizeControllerImpl implements AuthorizeController{
	
	private BankSystem bankSystem = new BankSystemImpl();
	
	@Override
	public void logout(Context ctx) {
		System.out.println("Cookies cleared.");
		ctx.clearCookieStore();
	}

	@Override
	public void customerLogin(Context ctx) {
		User user = checkCredentials(ctx);
		
		if(user == null || user.getUserType().equals("Employee")) {
			user = null;
		}
		
		if(user != null) {
			ctx.cookieStore("customerPortal", user.getId());
			ctx.status(201);
			ctx.redirect("http://localhost:9000/CustomerPortal.html");
		}else {
			ctx.status(418);
			ctx.redirect("http://localhost:9000/LoginPageCustomer.html");
		}
	}
	
	@Override
	public void employeeLogin(Context ctx) {
		User user = checkCredentials(ctx);
		
		if(user == null || user.getUserType().equals("Customer")) {
			user = null;
		}
		
		if(user != null) {
			ctx.cookieStore("employeePortal", user.getId());
			ctx.status(201);
			ctx.redirect("http://localhost:9000/EmployeePortal.html");
		}else {
			ctx.status(418);
			ctx.redirect("http://localhost:9000/LoginPageEmployee.html");
		}
	}
	
	@Override
	public void userAccountCreation(Context ctx) {
		String fName = ctx.formParam("FirstName");
		String lName = ctx.formParam("LastName");
		String email = ctx.formParam("Email");
		String username = ctx.formParam("username");
		String password = ctx.formParam("password");
		String userType = ctx.formParam("UserType");
		
		System.out.println(fName + " " + lName + "\n" + email + "\n" + username + "\n" + password + "\n" + userType);
		
		boolean success = bankSystem.createNewUser(fName, lName, email, userType, username, password);
		
		if(userType.equals("Employee")) {
			employeeAccountCreation(ctx, success);
		}else {
			customerAccountCreation(ctx, success);
		}
	}
	
	//helper method to determine where to send user back to
	private void customerAccountCreation(Context ctx, boolean success) {
		if(success) {
			ctx.status(201);
			ctx.redirect("http://localhost:9000/LoginPageCustomer.html");
		}else {
			ctx.status(418);
			ctx.redirect("http://localhost:9000/NewAccountCreationCustomer.html");
		}	
	}

	//helper method to determine where to send user back to
	private void employeeAccountCreation(Context ctx, boolean success) {
		if(success) {
			ctx.status(201);
			ctx.redirect("http://localhost:9000/LoginPageEmployee.html");
		}else {
			ctx.status(418);
			ctx.redirect("http://localhost:9000/NewAccountCreationEmployee.html");
		}
	}	
	
	@Override
	public User checkCredentials(Context ctx) {
		String username = ctx.formParam("username");
		String password = ctx.formParam("password");
		return bankSystem.login(username, password);
	}
	
	
}
