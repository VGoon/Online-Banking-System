package com.revature.pzero.controller;

import io.javalin.http.Context;

public interface EmployeePortalController {
	
	//obtain user object to send back to fill out portal
	public void viewUserDetails(Context ctx);
	
	//display all customers in the system
	public void portalLoadUp(Context ctx);
	
	//display all accounts in the system
	public void portalLoadUpAccounts(Context ctx);	
	
	//obtain the user selected from employee portal
	public void storeUser(Context ctx);
	
	public void viewUser(Context ctx);
	
	//view the selected user's account
	public void populateUserPage(Context ctx);
	
	//close a bank account if frozen and empty
	public void closeBankAccount(Context ctx);
	
	//view activity log
	public void viewLog(Context ctx);
	
	//turn on/off account lock
	public void toggleApproval(Context ctx);
	
	//clear cookies
	public void clearCookies(Context ctx);	
}
