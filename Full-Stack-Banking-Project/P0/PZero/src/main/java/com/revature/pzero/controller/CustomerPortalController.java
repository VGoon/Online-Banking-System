package com.revature.pzero.controller;

import io.javalin.http.Context;

public interface CustomerPortalController {
	
	//obtain user object to send back to fill out portal
	public void viewUserDetails(Context ctx);
	
	//creates a new account that must be approved before eligible for usage
	public void createNewBankAccount(Context ctx);
	
	//store a cookie of which account your withdrawing from
	public void withdrawStore(Context ctx);

	//withdraw money from the bank if available - clear cookie on leave
	public void withdraw(Context ctx);
	
	//store a cookie of which account your depositing into
	public void depositStore(Context ctx);
	
	//deposit money into the bank
	public void deposit(Context ctx);
	
	//transfer money from one account to another
	public void transfer(Context ctx);
	
	//get a specific customer's accounts
	public void customerAccounts(Context ctx);
	
	//get an account that doesn't belong to the user that is logged in
	public void getOutsideAccount(Context ctx);
	
}
