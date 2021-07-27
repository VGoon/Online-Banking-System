package com.revature.pzero.repository;

import java.util.List;
import java.util.Map;

import com.revature.pzero.models.Account;
import com.revature.pzero.models.Log;
import com.revature.pzero.models.User;

public interface Bank {

	//CRUD
	
	//create
	boolean newUser(User u, String userType);
	boolean newAccount(int userId, Account a);
	boolean logEvent(String message);
	
	//read
	User login(String username, String password);
	Account viewAccountByAccountId(int accountId);
	List<Account> viewAccountByUserID(int userID);
	List<Account> viewAllAccounts();
	List<String> viewUsernames();
	List<User> viewAllUsers();
	User viewUserById(int userID);
	User viewUserFromAccountId(int accountId);
	List<Account> viewUnapprovedAccounts();
	List<Log> viewLogs();
	
	//update
	boolean withdraw(Account a);
	boolean deposit(Account a);
	boolean transfer(Account a, Account b, double transferAmount);
	boolean updateAccountApproval(Account a);
	boolean updateUserApproval(User u);
	boolean updateUserPassword(User u);
	
	//delete
	boolean deleteAccount(Account a);
	boolean deleteUser(User u);
}
