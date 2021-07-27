package com.revature.pzero;

import java.sql.Connection;
import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.revature.pzero.controller.AuthorizeController;
import com.revature.pzero.controller.AuthorizeControllerImpl;
import com.revature.pzero.controller.CustomerPortalController;
import com.revature.pzero.controller.CustomerPortalControllerImpl;
import com.revature.pzero.controller.EmployeePortalController;
import com.revature.pzero.controller.EmployeePortalControllerImpl;

import io.javalin.Javalin;

public class MainDriver {
	
	private static AuthorizeController aControl;
	private static EmployeePortalController eControl;
	private static CustomerPortalController cControl;
	
	
	private static final String LOGOUT = "logout-path";
	
	private static final String CUSTOMER_LOGIN_PATH = "customer-login";
	private static final String CUSTOMER_SIGNUP = "customer-signup";
	private static final String CUSTOMER_DETAILS = "customer-details";
	private static final String CUSTOMER_ACCOUNTS = "customer-accounts";
	private static final String CUSTOMER_WITHDRAW_STORE = "customer-withdraw-store";
	private static final String CUSTOMER_WITHDRAW = "customer-withdraw";
	private static final String CUSTOMER_DEPOSIT_STORE = "customer-deposit-store";
	private static final String CUSTOMER_DEPOSIT = "customer-deposit";
	private static final String CUSTOMER_OUTSIDE_ACCOUNT = "outside-account";
	private static final String CUSTOMER_TRANSFER = "customer-transfer";
	private static final String CUSTOMER_NEW_ACCOUNT = "customer-request-account";
	
	private static final String EMPLOYEE_LOGIN_PATH = "employee-login";
	private static final String EMPLOYEE_DETAILS = "employee-details";
	private static final String EMPLOYEE_SIGNUP = "employee-signup";
	private static final String EMPLOYEE_PORTAL_LOADUP_USERS = "employee-portal-users";
	private static final String EMPLOYEE_PORTAL_LOADUP_ACCOUNTS = "employee-portal-accounts";
	private static final String EMPLOYEE_PORTAL_VIEW_USER_REQUEST = "employee-portal-viewuser";
	private static final String EMPLOYEE_PORTAL_VIEW_USER_PAGE = "employee-portal-view-user-page";
	private static final String EMPLOYEE_PORTAL_USER_DETAILS = "employee-portal-view-user-page-userdetails";
	private static final String EMPLOYEE_PORTAL_TOGGLE_APPROVAL = "employee-toggle-approval";
	private static final String EMPLOYEE_PORTAL_DELETE_ACCOUNT = "employee-delete-account";
	private static final String EMPLOYEE_BACK_BUTTON = "employee-back-button";
	
	
	private static final String LOG_EVENT = "logging-events";

	public static void main(String[] args) {
		aControl = new AuthorizeControllerImpl();
		eControl = new EmployeePortalControllerImpl();
		cControl = new CustomerPortalControllerImpl();
		
		Javalin app = Javalin.create( 
				config -> {
			config.addStaticFiles("/public");
		}).start(9000);
		
		//System methods
		app.get(LOG_EVENT, ctx -> eControl.viewLog(ctx));
		app.post(EMPLOYEE_LOGIN_PATH, ctx -> aControl.employeeLogin(ctx));
		app.post(EMPLOYEE_SIGNUP, ctx -> aControl.userAccountCreation(ctx));
		app.get(LOGOUT, ctx -> aControl.logout(ctx));
		
		//Customer login methods
		app.post(CUSTOMER_LOGIN_PATH, ctx -> aControl.customerLogin(ctx));
		app.post(CUSTOMER_SIGNUP, ctx -> aControl.userAccountCreation(ctx));
		app.get(CUSTOMER_DETAILS, ctx -> cControl.viewUserDetails(ctx));
		app.get(CUSTOMER_ACCOUNTS, ctx -> cControl.customerAccounts(ctx));
		app.post(CUSTOMER_WITHDRAW_STORE, ctx -> cControl.withdrawStore(ctx));
		app.post(CUSTOMER_WITHDRAW, ctx -> cControl.withdraw(ctx));
		app.post(CUSTOMER_DEPOSIT_STORE, ctx -> cControl.depositStore(ctx));
		app.post(CUSTOMER_DEPOSIT, ctx -> cControl.deposit(ctx));
		app.post(CUSTOMER_OUTSIDE_ACCOUNT, ctx -> cControl.getOutsideAccount(ctx));
		app.post(CUSTOMER_TRANSFER, ctx -> cControl.transfer(ctx));
		app.get(CUSTOMER_NEW_ACCOUNT, ctx -> cControl.createNewBankAccount(ctx));
		
		//Employee login methods	
		app.get(EMPLOYEE_DETAILS, ctx -> eControl.viewUserDetails(ctx));
		app.get(EMPLOYEE_PORTAL_LOADUP_USERS, ctx -> eControl.portalLoadUp(ctx));
		app.get(EMPLOYEE_PORTAL_LOADUP_ACCOUNTS, ctx -> eControl.portalLoadUpAccounts(ctx));
		app.post(EMPLOYEE_PORTAL_VIEW_USER_REQUEST, ctx -> eControl.storeUser(ctx)); //called when a user presses a row in the table
		app.get(EMPLOYEE_PORTAL_USER_DETAILS, ctx -> eControl.viewUser(ctx)); 
		app.get(EMPLOYEE_PORTAL_VIEW_USER_PAGE, ctx -> eControl.populateUserPage(ctx));
		app.post(EMPLOYEE_PORTAL_TOGGLE_APPROVAL, ctx -> eControl.toggleApproval(ctx));
		app.post(EMPLOYEE_PORTAL_DELETE_ACCOUNT, ctx -> eControl.closeBankAccount(ctx));
		app.get(EMPLOYEE_BACK_BUTTON, ctx -> eControl.clearCookies(ctx));
	}
	
}
