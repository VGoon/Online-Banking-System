package com.revature.pzero.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.revature.pzero.MainDriver;
import com.revature.pzero.models.Account;
import com.revature.pzero.models.Log;
import com.revature.pzero.models.User;
import com.revature.pzero.repository.Bank;
import com.revature.pzero.repository.BankImpl;

public class BankSystemImpl implements BankSystem{

	private Bank bank;
	final static Logger loggy = Logger.getLogger(BankSystem.class);
	
	
	public BankSystemImpl() {
		this.bank = new BankImpl();
	}
	
	
	public BankSystemImpl(Bank bank) {
		this.bank = bank;
		loggy.setLevel(Level.WARN);
	}

	@Override
	public User login(String username, String password) {
		User user = null;
		
		if(username == null || username.isBlank()) {
			loggy.info("Username invalid.");
			return null;
		}else if(password == null || password.isBlank()) {
			loggy.info("Password invalid");
			return null;
		}
		
		user = bank.login(username, password);
		
		return user;
	}
	
	private boolean checkCreateNewAccount(int userId, Double balance) {
		if(balance == null || balance != 0.00) {
			loggy.info("Balance invalid.");
			return false;
		}	
		if(verifyUser(userId) == null) {
			loggy.info("Account creation failed.");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean createNewAccount(int userid, Double balance, String nickName, boolean approved) {
		if(checkCreateNewAccount(userid, balance) == false) 
			return false;
		
		boolean success = false;
		
		Account account = new Account(-1, balance, nickName, approved);
		success = bank.newAccount(userid, account);
		if(success)
			logEvent("Customer #" + userid + " has requested a bank account.");
		else
			loggy.info("Bank account creation failed.");
		
		return success;
	}
	
	private boolean checkCreateNewUser(String fName, String lName, String email, String userType, String username, String password) {
		if(fName == null || fName.isBlank()) {
			loggy.info("First name invalid.");
			return false;
		}else if(lName == null || lName.isBlank()) {
			loggy.info("Last name invalid.");
			return false;
		}else if(userType == null || userType.isBlank()) {
			loggy.error("Customer type empty.");
			return false;
		}else if(username == null || username.isBlank()) {
			loggy.info("Username invalid.");
			return false;
		}else if(password == null || password.isBlank()) {
			loggy.info("Password invalid.");
			return false;
		}else if(email == null || email.isBlank()) {
			loggy.info("Email invalid.");
			return false;
		}
		
		//check to see if user name is already being used
		List<String> listOfAllUserNames = bank.viewUsernames();
		if(listOfAllUserNames != null && listOfAllUserNames.isEmpty() != true) {
			if(listOfAllUserNames.contains(username)) {
				loggy.info("Username is already taken. Please choose another. -> createNewUser (BankSystemImpl)");
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean createNewUser(String fName, String lName, String email, String userType, String username, String password) {
		if(checkCreateNewUser(fName, lName, email, userType, username, password) == false)
			return false;
		
		boolean success = false;
		User user = new User(-1, fName, lName, email, userType, username, password, true);
		success = bank.newUser(user, userType);
		
		if(success)
			logEvent("New Customer registered. USERNAME: " + username + ".");
		else
			loggy.info("User Account creation failed.");
		
		return success;
	}

	@Override
	public boolean authenticate(String username, String password) {
		if(username == null || username.isBlank()) {
			loggy.info("Username invalid.");
			return false;
		}else if(password == null || password.isBlank()) {
			loggy.info("Password invalid.");
			return false;
		}
		
		boolean success = false;
		
		User user = bank.login(username, password);
		if(user != null) {
			success = true;
		}else
			loggy.info("Authentication failed.");
		
		return success;
	}

	
	@Override
	public User verifyUser(int userid) {
		if(userid <= -1) {
			return null;
		}
		
		User user = null;
		user = bank.viewUserById(userid);
		return user;
	}
	
	@Override
	public Account verifyAccount(int accountId) {
		if(accountId <= -1) {
			loggy.debug("Account provided invalid.");
			return null;
		}
		
		Account account = null;
		account = bank.viewAccountByAccountId(accountId);
		return account;
	}

	@Override
	public List<Account> getCustomerAccounts(int userId) {
		List<Account> listOfCustomerAccounts = null;
		if(userId == -1) {
			loggy.debug("User id provided failed.");
			return null;
		}
		
		listOfCustomerAccounts = bank.viewAccountByUserID(userId);
		if(listOfCustomerAccounts != null)
			loggy.info("RETRIEVED CUSTOMER #" + userId + " ACCOUNT");
		
		return listOfCustomerAccounts;
	}
	
	public boolean canWithdraw(Account account, double withdrawAmount) {
		if(account == null || account.isApproved() == false) {
			loggy.info("Account either invalid or locked.");
			return false;
		}
		
		if(withdrawAmount > account.getBalance() || withdrawAmount <= 0.0) {
			loggy.info("Withdraw amount invalid.");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean withdraw(Account account, double withdrawAmount) {
		//check to see if can withdraw first
		if(canWithdraw(account, withdrawAmount)) {
			account.setBalance(account.getBalance() - withdrawAmount);
			boolean success = bank.withdraw(account);
			if(success)
				logEvent("Account #" + account.getId() + " has withdrawn $" + withdrawAmount + ".");
			else
				loggy.debug("Account withdraw error.");
			
			return success;
		}
		
		return false;
	}
	
	public boolean canDeposit(Account account, double depositAmount) {
		if(account == null || account.isApproved() == false) {
			loggy.warn("Account invalid.");
			return false;
		}
		if(depositAmount <= 0.0) {
			loggy.info("Deposit amount invalid.");
			return false;
		}
		
		if(depositAmount + account.getBalance() > 500000.00) {
			loggy.info("Deposit balance + amount exceeds $500,000");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean deposit(Account account, double depositAmount) {
		if(canDeposit(account, depositAmount)) {
			account.setBalance(account.getBalance() + depositAmount);
			boolean success = bank.deposit(account);
			if(success)
				logEvent("Account #" + account.getId() + " deposited $" + depositAmount + ".");
			else
				loggy.warn("Account deposit error.");
			
			return success;
		}
		return false;
	}
	
	//check to make sure not a negative amount and etc
	@Override
	public boolean canTransfer(Account originAccount, Account transferToAccount, double transferAmount) {
		if(transferToAccount == null) {
			loggy.info("Desired account to transfer to is invalid.");
			return false;
		}
		
		if(originAccount == null) {
			loggy.info("Account given is invalid.");
			return false;
		}
		
		if(transferAmount > originAccount.getBalance()) {
			loggy.info("Insufficient funds.");
			return false;
		}
		
		if(originAccount.getId() == transferToAccount.getId()) {
			loggy.info("Transfering to the same account. Transaction voided.");
			return false;
		}
		
		if(originAccount.isApproved() == false || transferToAccount.isApproved() == false) {
			loggy.info("Account(s) is locked. Account must be unlocked before attempting any transactions.");
			return false;
		}
		
		if(transferAmount + transferToAccount.getBalance() > 500000.00) {
			loggy.info("An account max if $500,000.00. Please create a new account if and splint the amount between two accounts.");
		}
		
		return true;
	}

	@Override
	public boolean transfer(Account originAccount, Account transferToAccount, double transferAmount) {		
		if(canTransfer(originAccount, transferToAccount, transferAmount)) {
			originAccount.setBalance(originAccount.getBalance() - transferAmount);
			transferToAccount.setBalance(transferToAccount.getBalance() + transferAmount);
			
			boolean success = bank.transfer(originAccount, transferToAccount, transferAmount);
			
			if(success) {
				loggy.info("TRANSFER FROM ACCOUNT #" + originAccount.getId() + " OF AMOUNT " +transferAmount + " TO ACCOUNT #" + transferToAccount.getId());
				logEvent("Account #" + originAccount.getId() + " has transfered $"+ transferAmount + " to Account #" + transferToAccount.getId() + ".");
			}
				
			return success;
		}
		
		return false;
	}
	
	@Override
	public User getUserById(int userId) {
		User user = null;
		if(userId == -1) {
			return null;
		}
			
		user = bank.viewUserById(userId);		
		return user;
	}

	@Override
	public Account getAccountById(int accountId) {
		if(accountId == -1) {
			return null;
		}
		
		Account account = bank.viewAccountByAccountId(accountId);		
		return account;
	}

	@Override
	public User getUserViaAccountNumber(int accountId) {
		if(accountId == -1) {
			return null;
		}
		
		User user = bank.viewUserFromAccountId(accountId);		
		return user;
	}

	@Override
	public List<Account> accountsToBeApproved(int userId){
		List<Account> unapprovedAccounts = null;
		
		User user = bank.viewUserById(userId);
		if(user == null || user.getUserType().equals("Customer")) {
			return null;
		}
		
		unapprovedAccounts = bank.viewUnapprovedAccounts();		
		
		return unapprovedAccounts;
	}

	@Override
	public boolean approveAccount(Account account) {
		account.setApproved(!account.isApproved());	
		
		if(account.isApproved() == true)
			logEvent("Account #" + account.getId() + " has been approved.");
		else
			logEvent("Account #" + account.getId() + " has been frozen.");
		
		return bank.updateAccountApproval(account);
	}
	
	@Override
	public List<User> getAllUsers(){
		return bank.viewAllUsers();
	}

	@Override
	public boolean approveUser(User user) {
		user.setUserApproved(!user.isUserApproved());	
		return bank.updateUserApproval(user);
	}
	
	public boolean updateUserPassword(User user) {
		return bank.updateUserPassword(user);
	}

	@Override
	public boolean closeAccount(Account a) {
		if(a!= null && (a.getBalance() > 0 || a.isApproved() == true)) {
			loggy.info("Account must have no balance and must be locked before it can be closed.");
			return false;
		}
		
		int id = a.getId();
		boolean success = bank.deleteAccount(a);
		
		if(success)
			logEvent("Account #" + id + " has been closed.");
		else
			loggy.info("Account #" + id + " failed to be closed.");
		
		return bank.deleteAccount(a);
	}

	@Override
	public List<Account> getAllAccounts() {
		return bank.viewAllAccounts();
	}
	
	@Override
	public boolean logEvent(String message) {
		return bank.logEvent(message);
	}

	@Override
	public List<Log> viewLogs() {
		return bank.viewLogs();
	}
	
	
}
