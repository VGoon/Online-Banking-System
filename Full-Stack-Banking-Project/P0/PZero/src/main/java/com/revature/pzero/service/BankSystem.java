package com.revature.pzero.service;

import java.util.List;
import java.util.Map;

import com.revature.pzero.models.Account;
import com.revature.pzero.models.Log;
import com.revature.pzero.models.User;

public interface BankSystem {
	
	//get the user associated with the login credentials 
	User login(String username, String password); // check if a part of database
	
	//get the user associated with the user ID
	User getUserById(int userId);
	
	//get the account associated with the account ID
	Account getAccountById(int accountId);
	
	//get the user associated with the account ID
	User getUserViaAccountNumber(int accountId);
	
	//create a valid new account
	boolean createNewAccount(int userID, Double balance, String nickName, boolean approved);
	
	//create a valid new user
	boolean createNewUser(String fName, String lName, String email, String userType, String username, String password);
	
	//make sure both username and password go with the same user inorder to login
	boolean authenticate(String username, String password);
	
	User verifyUser(int userid);
	
	//check to see if account id is valid
	Account verifyAccount(int accountId);
	
	//obtain list of accounts all under user
	List<Account> getCustomerAccounts(int id);
	
	//check to see if withdraw amount is possible to be done on account
	boolean canWithdraw(Account account, double withdrawAmount);
	
	//withdraw 'withdrawAmount' from account
	boolean withdraw(Account account, double withdrawAmount);
	
	//check to see if depositAmount is possible to be taken from the account
	boolean canDeposit(Account account, double depositAmount);
	
	//deposit 'depositAmount' into the supplied account
	boolean deposit(Account account, double depositAmount);
	
	//check to see if transfer is possible with the accompanying transfer amount
	boolean canTransfer(Account originAccount, Account transferToAccount, double transferAmount);
	
	//transfer money from one account to another
	boolean transfer(Account originAccount, Account transferToAccount, double transferAmount);
	
	//list of accounts that need to be approved (approved = false)
	List<Account> accountsToBeApproved(int userId);
	
	//update account approval to the opposite of what it is.
	boolean approveAccount(Account account);
	
	//list of users that need to be approved
	List<User> getAllUsers();
	
	//update user approval to the opposite of what it is
	boolean approveUser(User user);
	
	//update user password when forgotten
	boolean updateUserPassword(User user);
	
	//close account when account balance = 0 and account if locked
	boolean closeAccount(Account a);
	
	//get list of all accounts
	List<Account> getAllAccounts();
	
	//log when events happen as they happen
	boolean logEvent(String message);
	
	//view the logs
	List<Log> viewLogs();
}
