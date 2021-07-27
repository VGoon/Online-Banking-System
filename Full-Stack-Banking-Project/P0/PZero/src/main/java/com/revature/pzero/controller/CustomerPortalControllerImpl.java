package com.revature.pzero.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revature.pzero.models.Account;
import com.revature.pzero.models.User;
import com.revature.pzero.service.BankSystem;
import com.revature.pzero.service.BankSystemImpl;

import io.javalin.http.Context;

public class CustomerPortalControllerImpl implements CustomerPortalController {
	
	BankSystem bankSystem = new BankSystemImpl();

	@Override
	public void viewUserDetails(Context ctx) {
		int userId = Integer.parseInt(ctx.cookieStore("customerPortal").toString());

		User user = bankSystem.getUserById(userId);
		if(user != null) {
			ctx.json(user);
			ctx.status(200);
		}else {
			ctx.status(418);
		}	
	}
	
	@Override
	public void createNewBankAccount(Context ctx) {
		int userId = Integer.parseInt(ctx.cookieStore("customerPortal").toString());
		boolean success = bankSystem.createNewAccount(userId, 0.0, "CHECKING", false);
		
		if(success) {
			ctx.status(200);
			ctx.redirect("http://localhost:9000/CustomerPortal.html");
		}else {
			ctx.status(418);
			ctx.redirect("http://localhost:9000/CustomerPortal.html");
		}
	}

	@Override
	public void withdraw(Context ctx) {
		int fromAccountID = Integer.parseInt(ctx.formParam("fromAccount"));
		double amount = Double.parseDouble(ctx.formParam("amount"));
		boolean success = false;
		
		Account withdrawAccount = bankSystem.getAccountById(fromAccountID);
		if(withdrawAccount != null) {
			success = bankSystem.withdraw(withdrawAccount, amount);
			
			if(success) {
				ctx.status(200);
				ctx.redirect("http://localhost:9000/CustomerPortal.html");
			}else {
				ctx.status(418);
				ctx.redirect("http://localhost:9000/WithdrawPage.html");
			}
		}
	}

	@Override
	public void deposit(Context ctx) {
		int fromAccountID = Integer.parseInt(ctx.formParam("fromAccount"));
		double amount = Double.parseDouble(ctx.formParam("amount"));
		boolean success = false;
		
		Account depositAccount = bankSystem.getAccountById(fromAccountID);
		if(depositAccount != null) {
			success = bankSystem.deposit(depositAccount, amount);
			
			if(success) {
				ctx.status(200);
				ctx.redirect("http://localhost:9000/CustomerPortal.html");
			}else {
				ctx.status(418);
				ctx.redirect("http://localhost:9000/DepositPage.html");
			}
		}		
	}

	@Override
	public void transfer(Context ctx) {
		int toAccountID = Integer.parseInt(ctx.formParam("toAccount"));
		int fromAccountID = Integer.parseInt(ctx.formParam("fromAccount"));
		double amount = Double.parseDouble(ctx.formParam("amount"));
		boolean success = false;
		
		Account originAccount = bankSystem.getAccountById(fromAccountID);
		Account transferToAccount = bankSystem.getAccountById(toAccountID);
		
		
		
		if(originAccount != null && transferToAccount != null) {
			
			success = bankSystem.transfer(originAccount, transferToAccount, amount);
			
			if(success) {
				ctx.status(200);
				ctx.redirect("http://localhost:9000/CustomerPortal.html");
			}else {
				ctx.status(418);
				ctx.redirect("http://localhost:9000/TransferPage.html");
			}
		}else {
			ctx.status(418);
		}
	}
	
	@Override 
	public void customerAccounts(Context ctx) {
		int userId = Integer.parseInt(ctx.cookieStore("customerPortal").toString());
		
		List<Account> userAccounts = bankSystem.getCustomerAccounts(userId);
		if(userAccounts != null) {
			ctx.json(userAccounts);
			ctx.status(200);
		}else {
			ctx.status(418);
		}
	}

	@Override
	public void withdrawStore(Context ctx) {
		int id = Integer.parseInt(ctx.formParam("id"));
		
		ctx.cookieStore("withdrawAccountID", id);
		ctx.status(200);	
	}

	@Override
	public void depositStore(Context ctx) {
		int id = Integer.parseInt(ctx.formParam("id"));
		
		ctx.cookieStore("depositAccountID", id);
		ctx.status(200);	
	}

	@Override
	public void getOutsideAccount(Context ctx) {
		try {
			int outsideAccountID = Integer.parseInt(ctx.formParam("outsideAccountInput"));
			System.out.println("aID: " + outsideAccountID);
			Account a = bankSystem.verifyAccount(outsideAccountID);
			if(a != null && a.isApproved() == true) {
				ctx.json(a);
				ctx.status(200);
			}else {
				ctx.status(400);
			}
		}catch(Exception e){
			e.printStackTrace();
			ctx.status(400);
		}
	}

}
