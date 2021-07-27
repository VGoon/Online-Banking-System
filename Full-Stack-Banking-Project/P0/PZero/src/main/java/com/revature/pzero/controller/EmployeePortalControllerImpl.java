package com.revature.pzero.controller;

import java.util.List;
import java.util.Map;

import com.revature.pzero.models.Account;
import com.revature.pzero.models.Log;
import com.revature.pzero.models.User;
import com.revature.pzero.service.BankSystem;
import com.revature.pzero.service.BankSystemImpl;

import io.javalin.http.Context;

public class EmployeePortalControllerImpl implements EmployeePortalController {
	
	private BankSystem bankSystem = new BankSystemImpl();
	
	@Override
	public void viewUserDetails(Context ctx) {
		int userId = Integer.parseInt(ctx.cookieStore("employeePortal").toString());

		User user = bankSystem.getUserById(userId);
		if(user != null) {
			ctx.json(user);
			ctx.status(200);
		}else {
			ctx.status(418);
		}	
	}
	
	@Override
	public void portalLoadUp(Context ctx) {
		List<User> allUsers = bankSystem.getAllUsers();
		if(allUsers != null) {
			ctx.json(allUsers);
			ctx.status(200);
		}else {
			ctx.status(418);
		}
		
	}
	
	@Override
	public void portalLoadUpAccounts(Context ctx) {
		List<Account> allAccounts = bankSystem.getAllAccounts();
		if(allAccounts != null) {
			ctx.json(allAccounts);
			ctx.status(200);
		}else {
			ctx.status(418);
		}
	}

	@Override
	public void storeUser(Context ctx) {
		int id = Integer.parseInt(ctx.formParam("id"));
		
		ctx.cookieStore("viewUser", id);
		ctx.status(200);
		ctx.redirect("http://localhost:9000/EmployeeViewUserAccount.html");
	}
	
	@Override
	public void viewUser(Context ctx) {
		int id = ctx.cookieStore("viewUser");
		User user = bankSystem.getUserById(id);
		if(user != null) {
			ctx.json(user);
			ctx.status(200);
		}else {
			ctx.status(418);
		}	
	}
	
	@Override
	public void populateUserPage(Context ctx) {
		int strId = ctx.cookieStore("viewUser");
		
		List<Account> userAccounts = bankSystem.getCustomerAccounts(strId);
		if(userAccounts != null) {
			ctx.json(userAccounts);
			ctx.status(200);
		}else {
			ctx.status(418);
		}
	}


	@Override
	public void closeBankAccount(Context ctx) {
		int accountId = Integer.parseInt(ctx.formParam("id"));
		Account a = bankSystem.getAccountById(accountId);
		boolean success = bankSystem.closeAccount(a);
		
		if(success) {
			ctx.status(200);
		}else {
			ctx.status(418);
		}
	}

	@Override
	public void viewLog(Context ctx) {
		List<Log> log = bankSystem.viewLogs();
		if(log != null) {
			ctx.json(log);
			ctx.status(200);
		}else {
			ctx.status(418);
		}
	}
	
	@Override
	public void toggleApproval(Context ctx) {
		int accountId = Integer.parseInt(ctx.formParam("id"));
		Account a = bankSystem.getAccountById(accountId);
		boolean success = bankSystem.approveAccount(a);
		
		if(success) {
			ctx.status(200);
		}else {
			ctx.status(418);
		}
	}

	@Override
	public void clearCookies(Context ctx) {
		ctx.removeCookie("viewUser");
		ctx.status(200);
	}
	
}
