package com.revature.pzero.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revature.pzero.models.Account;
import com.revature.pzero.models.Log;
import com.revature.pzero.models.User;
import com.revature.pzero.util.ConnectionPoint;

public class BankImpl implements Bank{

	public BankImpl() {
		super();
	}

	@Override
	public boolean newUser(User u, String userType) {
		boolean success = false;
		
		String sqlQuery = "insert into user_table(first_name, last_name, email, user_type, account_username, account_password, user_approved) values (?, ?, ?, ?, ?, ?, ?);";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setString(1, u.getFirstName());
			cStatement.setString(2, u.getLastName());
			cStatement.setString(3, u.getEmail());
			cStatement.setString(4, userType);
			cStatement.setString(5, u.getUserName());
			cStatement.setString(6,  u.getUserPassword());
			cStatement.setBoolean(7,  true); //upon creation all users are frozen until approval
			
			cStatement.execute();
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean newAccount(int userId, Account a) {
		boolean success = false;
		
		String sqlID = "select nextval(pg_get_serial_sequence('account_table', 'account_id')) as new_id;";
		String sqlQuery = "insert into account_table values (?, ?, ?, ?);";
		String sqlQueryJunctionTable = "insert into account_to_user values (?,?);";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlID);
			ResultSet rs = cStatement.executeQuery();
			
			int id = -1;
			if(rs.next()) {
				id = rs.getInt("new_id");
			}
			
			if(id == -1)
				return false;
			
			cStatement = conn.prepareCall(sqlQuery);				
				
			cStatement.setInt(1, id);
			cStatement.setDouble(2, a.getBalance());
			cStatement.setString(3, a.getNickName());
			cStatement.setBoolean(4, a.isApproved());
			cStatement.execute();
				
			cStatement = conn.prepareCall(sqlQueryJunctionTable);
			cStatement.setInt(1, userId);
			cStatement.setInt(2, id);
			cStatement.execute();
				
			success = true;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	@Override
	public User login(String username, String password) {
		User user = null;
		String sqlQuery = "select * from user_table where account_username = ? and account_password = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setString(1, username);
			cStatement.setString(2, password);
			
			ResultSet result = cStatement.executeQuery();
			
			if(result.next()) {
				user = new User(result.getInt("user_id"),
						result.getString("first_Name"),
						result.getString("last_Name"),
						result.getString("email"),
						result.getString("user_type"),
						result.getString("account_username"),
						result.getString("account_password"),
						result.getBoolean("user_approved"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public Account viewAccountByAccountId(int accountId) {
		Account account = null;
		String sqlQuery = "select * from account_table where account_id = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setInt(1, accountId);
			
			ResultSet result = cStatement.executeQuery();
			
			if(result.next()) {
				account = new Account(result.getInt("account_id"),
						result.getDouble("account_balance"),
						result.getString("account_name"),
						result.getBoolean("account_approved"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return account;
	}

	@Override
	public List<Account> viewAccountByUserID(int userID) {
		List<Account> listOfAccounts = null;
		String sqlQuery = "select * from account_table where account_id in (select account_id from account_to_user where user_id = ?);";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setInt(1, userID);
			
			ResultSet result = cStatement.executeQuery();
			listOfAccounts = new ArrayList<Account>();
			
			while(result.next()) {
				Account a = new Account(
						result.getInt("account_id"),
						result.getDouble("account_balance"),
						result.getString("account_name"),
						result.getBoolean("account_approved"));
				
				listOfAccounts.add(a);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return listOfAccounts;
	}

	@Override
	public List<Account> viewAllAccounts() {
		List<Account> listOfAccounts = null;
		String sqlQuery = "select * from account_table;";

		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			
			ResultSet result = cStatement.executeQuery();
			listOfAccounts = new ArrayList<Account>();
			
			while(result.next()) {
				Account a = new Account(
						result.getInt("account_id"),
						result.getDouble("account_balance"),
						result.getString("account_name"),
						result.getBoolean("account_approved"));
				
				listOfAccounts.add(a);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return listOfAccounts;
	}
	
	@Override
	public List<User> viewAllUsers() {
		List<User> listOfAccounts = null;
		String sqlQuery = "select * from user_table;";

		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			
			ResultSet result = cStatement.executeQuery();
			listOfAccounts = new ArrayList<User>();
			
			while(result.next()) {
				User user = new User(result.getInt("user_id"),
						result.getString("first_Name"),
						result.getString("last_Name"),
						result.getString("email"),
						result.getString("user_type"),
						result.getString("account_username"),
						result.getString("account_password"),
						result.getBoolean("user_approved"));
				
				listOfAccounts.add(user);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return listOfAccounts;
	}

	@Override
	public boolean withdraw(Account a) {
		return moneyAdjustment(a);
	}

	@Override
	public boolean deposit(Account a) {
		return moneyAdjustment(a);
	}
	
	private boolean moneyAdjustment(Account a) {
		boolean success = false;
		
		String sqlQuery = "update account_table set account_balance = ? where account_id = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setDouble(1, a.getBalance());
			cStatement.setInt(2,  a.getId());
			
			cStatement.execute();
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean transfer(Account a, Account b, double transferAmount) {
		boolean success = false;
		
		String sqlQuery = "update account_table set account_balance = ? where account_id = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatementAccountOne = conn.prepareCall(sqlQuery);
			cStatementAccountOne.setDouble(1, a.getBalance());
			cStatementAccountOne.setInt(2, a.getId());
			
			CallableStatement cStatementAccountTwo = conn.prepareCall(sqlQuery);
			cStatementAccountTwo.setDouble(1, b.getBalance());
			cStatementAccountTwo.setInt(2, b.getId());
			
			cStatementAccountOne.execute();
			cStatementAccountTwo.execute();
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean deleteAccount(Account a) {
		boolean success = false;
		
		String sqlQuery = "delete from account_to_user where account_id = ?;";
		String sqlQuery2 = "delete from account_table where account_id = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setInt(1, a.getId());
			
			
			CallableStatement cStatementTwo = conn.prepareCall(sqlQuery2);
			cStatementTwo.setInt(1, a.getId());
			
			cStatement.execute();
			cStatementTwo.execute();
			
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean deleteUser(User u) {
		boolean success = false;
		
		String sqlQuery = "delete from account_to_user where user_id = ?;";
		String sqlQuery2 = "delete from user_table where user_id = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setInt(1, u.getId());
			
			CallableStatement cStatementTwo = conn.prepareCall(sqlQuery2);
			cStatementTwo.setInt(1,  u.getId());
			
			cStatement.execute();
			cStatementTwo.execute();
			
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public List<String> viewUsernames() {
		boolean success = false;
		List<String> usernameList = null;
		
		String sqlQuery = "select account_username from user_table;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			
			ResultSet result = cStatement.executeQuery();
			usernameList = new ArrayList();
			
			while(result.next()) {
				usernameList.add(result.getString("account_username"));
			}
			
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return usernameList;
	}

	@Override
	public User viewUserById(int userID) {
		boolean success = false;
		User user = null;
		
		String sqlQuery = "select * from user_table where user_id = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setInt(1, userID);
			
			ResultSet result = cStatement.executeQuery();
			
			if(result.next()) {
				user = new User(result.getInt("user_id"),
						result.getString("first_name"),
						result.getString("last_name"),
						result.getString("email"),
						result.getString("user_type"),
						result.getString("account_username"),
						result.getString("account_password"),
						result.getBoolean("user_approved"));
			}
			
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public User viewUserFromAccountId(int accountId) {
		boolean success = false;
		User user = null;
		
		String sqlQuery = "select * from user_table where user_id = (select user_id from account_to_user where account_id = ?);";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setInt(1, accountId);
			
			ResultSet result = cStatement.executeQuery();
			
			if(result.next()) {
				user = new User(result.getInt("user_id"),
						result.getString("first_name"),
						result.getString("last_name"),
						result.getString("email"),
						result.getString("user_type"),
						result.getString("account_username"),
						result.getString("account_password"),
						result.getBoolean("user_approved"));
			}
			
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public List<Account> viewUnapprovedAccounts() {
		boolean success = false;
		List<Account> usernameList = null;
		
		String sqlQuery = "select * from account_table where account_approved = false;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			
			ResultSet result = cStatement.executeQuery();
			usernameList = new ArrayList();
			
			while(result.next()) {
				usernameList.add(new Account(
						result.getInt("account_id"),
						result.getDouble("account_balance"),
						result.getString("account_name"),
						result.getBoolean("account_approved")));
			}
			
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return usernameList;
	}

	@Override
	public boolean updateAccountApproval(Account a) {
		boolean success = false;
		
		String sqlQuery = "update account_table set account_approved = ? where account_id = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setBoolean(1, a.isApproved());
			cStatement.setInt(2, a.getId());
			
			cStatement.execute();
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	@Override
	public boolean updateUserApproval(User u) {
		boolean success = false;
		
		String sqlQuery = "update user_table set user_approved = ? where user_id = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setBoolean(1, u.isUserApproved());
			cStatement.setInt(2, u.getId());
			
			cStatement.execute();
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	@Override
	public boolean updateUserPassword(User u) {
		boolean success = false;
		
		String sqlQuery = "update user_table set account_password = ? where user_id = ?;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setString(1, u.getUserPassword());
			cStatement.setInt(2, u.getId());
			
			cStatement.execute();
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean logEvent(String message) {
		boolean success = false;
		String sqlQuery = "insert into bank_log(message) values (?);";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			cStatement.setString(1, message);
			cStatement.execute();
			success = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public List<Log> viewLogs() {
		List<Log> logs = null;
		String sqlQuery = "select * from bank_log order by logid desc;";
		
		try(Connection conn = ConnectionPoint.getConnection()){
			CallableStatement cStatement = conn.prepareCall(sqlQuery);
			
			ResultSet rs = cStatement.executeQuery();
			logs = new ArrayList<Log>();
			while(rs.next()) {
				logs.add(new Log(rs.getInt("logId"), rs.getString("message")));
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return logs;
	}


	
	
}