package com.revature.pzero.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import com.revature.pzero.models.Account;
import com.revature.pzero.models.User;
import com.revature.pzero.repository.Bank;
import com.revature.pzero.repository.BankImpl;
import com.revature.pzero.service.BankSystem;
import com.revature.pzero.service.BankSystemImpl;

public class BankSystemTests {
	
	
	@Mock
	Bank mockBank;
	
	BankSystem testBankSystem;
	
	@Before
	public void beforeTests() {
		mockBank = mock(BankImpl.class);
		testBankSystem = new BankSystemImpl(mockBank);
	}
	
	@After
	public void afterTests() {}
	
	@Test
	public void loginTest() {
		String validUsername = "username";
		String validPassword = "password";
		String invalidUsername = "badUsername";
		String invalidPassword = "badPassword";
	
		User u = new User(0, "Tom", "Nook", "hello@email.com", "Customer", validUsername, validPassword, true);
		
		when(mockBank.login(null, null)).thenReturn(null);
		when(mockBank.login(null, validPassword)).thenReturn(null);
		when(mockBank.login(validUsername, null)).thenReturn(null);
		when(mockBank.login(invalidUsername, invalidPassword)).thenReturn(null);
		when(mockBank.login(validUsername, invalidPassword)).thenReturn(null);
		when(mockBank.login(invalidUsername, validPassword)).thenReturn(null);
		when(mockBank.login(validUsername, validPassword)).thenReturn(u);
		
		assertNull(testBankSystem.login(null, null));
		assertNull(testBankSystem.login(null, validPassword));
		assertNull(testBankSystem.login(invalidUsername, invalidPassword));
		assertNull(testBankSystem.login(validUsername, invalidPassword));
		assertNull(testBankSystem.login(invalidUsername, validPassword));
		assertEquals(u, testBankSystem.login(validUsername, validPassword));
	}
	
	@Test
	public void createNewUserTest() {
		String username = "username50";
		String password = "password";
		String userType = "Customer";
		String email = "blahblah@email.com";
		String usernameThatIsTaken = "username2";
		List<String> usernames = new ArrayList();
		usernames.add(usernameThatIsTaken);
		
		when(mockBank.viewUsernames()).thenReturn(usernames);
		when(mockBank.newUser(new User(-1, "firstName", "lastName", "newuser@email.com", userType, usernameThatIsTaken, password, false), "Customer")).thenReturn(false);
		
		when(mockBank.viewUsernames()).thenReturn(null);
		when(mockBank.newUser(new User(-1, "firstName", "lastName", email, userType, username, password, false), "Customer")).thenReturn(true);
		
		assertFalse(testBankSystem.createNewUser(null, null, null, null,  null, null));
		assertFalse(testBankSystem.createNewUser(null, null, null, null,  null, password));
		assertFalse(testBankSystem.createNewUser(null, null, null, null, username, password));
		assertFalse(testBankSystem.createNewUser(null, null, null, userType, username, password));
		assertFalse(testBankSystem.createNewUser(null, null, email, userType, username, password));
		assertFalse(testBankSystem.createNewUser(null, "lastName", email, userType, username, password));
		assertFalse(testBankSystem.createNewUser("firstName", null, email, userType, username, password));
		assertFalse(testBankSystem.createNewUser("", "lastName", email, userType, username, password));
		assertFalse(testBankSystem.createNewUser("firstName", "", email, userType, username, password));
		assertFalse(testBankSystem.createNewUser("firstName", "lastName",email, null, username, password));
		assertFalse(testBankSystem.createNewUser("firstName", "lastName", email, userType, null, password));
		assertFalse(testBankSystem.createNewUser("firstName", "lastName", email, userType, username, null));
		assertFalse(testBankSystem.createNewUser("firstName", "lastName", email, userType, usernameThatIsTaken, password));
		assertTrue(testBankSystem.createNewUser("firstName", "lastName", email, userType, username, password));
	}
	
	@Test
	public void authenticateTest() {		
		String validUsername = "username";
		String validPassword = "password";
		String invalidUsername = "badUsername";
		String invalidPassword = "badPassword";
		
		when(mockBank.login(validUsername, validPassword)).thenReturn(null); //account isn't approved
		when(mockBank.login(invalidUsername, invalidPassword)).thenReturn(null); //login denied
		when(mockBank.login(validUsername+1, validPassword+1)).thenReturn(new User(-1, "firstName", "lastName", "validemail@email.com", "Customer", validUsername+1, validPassword+1, true)); 
		
		assertFalse(testBankSystem.authenticate(invalidUsername, invalidPassword));
		assertFalse(testBankSystem.authenticate(validUsername, validPassword));
		assertFalse(testBankSystem.authenticate("", invalidPassword));
		assertFalse(testBankSystem.authenticate(validUsername, ""));
		assertFalse(testBankSystem.authenticate(null, validPassword));
		assertFalse(testBankSystem.authenticate(validUsername, null));
		assertFalse(testBankSystem.authenticate("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", validPassword));
		assertFalse(testBankSystem.authenticate(validUsername, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
		assertFalse(testBankSystem.authenticate("                       !@#@$Hhhhhhhhhhhhhhhh", validPassword));
		assertTrue(testBankSystem.authenticate(validUsername+1, validPassword+1));
	}
	
	@Test 
	public void verifyAccountTest() {
		Account a = new Account();
		
		when(mockBank.viewAccountByAccountId(-1)).thenReturn(null);
		when(mockBank.viewAccountByAccountId(300)).thenReturn(null); //doesn't exist
		when(mockBank.viewAccountByAccountId(-9)).thenReturn(null);
		when(mockBank.viewAccountByAccountId(0)).thenReturn(a);
		when(mockBank.viewAccountByAccountId(10)).thenReturn(a);
		
		assertNull(testBankSystem.verifyAccount(-1));
		assertNull(testBankSystem.verifyAccount(300));
		assertNull(testBankSystem.verifyAccount(-9));
		assertNull(testBankSystem.verifyAccount(Integer.MAX_VALUE+1));
		assertEquals(a, testBankSystem.verifyAccount(0));
		assertEquals(a, testBankSystem.verifyAccount(10));
	}

	@Test 
	public void canWithdrawTest() {		
		Account a = new Account(0, 120.30, "Nickname", true);
		Account badA = new Account(0, 0.0, "", true); //balance is 0. Can't withdraw anything.
		Account badATwo = new Account(0, 100.0, "", false); //not approved, no operations can be done
		
		assertFalse(testBankSystem.canWithdraw(badA, 5.0));
		assertFalse(testBankSystem.canWithdraw(badA, 0.0));
		assertFalse(testBankSystem.canWithdraw(badATwo, 10.0));
		assertFalse(testBankSystem.canWithdraw(badATwo, 0.0));
		assertFalse(testBankSystem.canWithdraw(a, -5.0));
		assertFalse(testBankSystem.canWithdraw(a,  200.10));
		assertTrue(testBankSystem.canWithdraw(a, 120.30));
		assertTrue(testBankSystem.canWithdraw(a, 20.30));
		
	}
	
	@Test 
	public void depositTest() {
		Account a = new Account(0, 120.30, "Nickname", true);
		Account badA = new Account(0, Double.MAX_VALUE, "", true); //balance is 0. Can't withdraw anything.
		Account badATwo = new Account(0, 100.0, "", false); //not approved, no operations can be done

		assertFalse(testBankSystem.canDeposit(a, -5.0));
		assertFalse(testBankSystem.canDeposit(a, Double.MAX_VALUE+1.0));
		assertFalse(testBankSystem.canDeposit(badATwo, 10.0));
		assertFalse(testBankSystem.canDeposit(badA, 0.01));
		assertTrue(testBankSystem.canDeposit(a, 10.0));
		assertFalse(testBankSystem.canDeposit(a, Double.MAX_VALUE - a.getBalance() + 1));
	}
	
	@Test
	public void transferTest() {
		Account okayAccount = new Account(0, 100.0, "", true);
		Account badAccount = new Account(1, 10.0, "", false);
		Account lowAccount = new Account(2, 10.0, "", true);
		
		when(mockBank.transfer(null, null, 100.0)).thenReturn(false);
		when(mockBank.transfer(null, okayAccount, 50.0)).thenReturn(false);
		when(mockBank.transfer(okayAccount, null, 10.0)).thenReturn(false);
		when(mockBank.transfer(okayAccount, badAccount, 10.0)).thenReturn(false);
		when(mockBank.transfer(badAccount, okayAccount, 10.0)).thenReturn(false);
		when(mockBank.transfer(okayAccount, lowAccount, 10.0)).thenReturn(true);
		when(mockBank.transfer(lowAccount, okayAccount, 20.0)).thenReturn(false);
		when(mockBank.transfer(okayAccount, lowAccount, -10.0)).thenReturn(false);
		when(mockBank.transfer(okayAccount, lowAccount, Double.MAX_VALUE-okayAccount.getBalance()+1.0)).thenReturn(false);
		when(mockBank.transfer(okayAccount, new Account(), 10.0)).thenReturn(false);
		when(mockBank.transfer(okayAccount, lowAccount, 55.09)).thenReturn(true);
		
		assertFalse(testBankSystem.transfer(null, null, 100.0));
		assertFalse(testBankSystem.transfer(null, okayAccount, 50.0));
		assertFalse(testBankSystem.transfer(okayAccount, null, 10.0));
		assertFalse(testBankSystem.transfer(okayAccount, badAccount, 10.0));
		assertFalse(testBankSystem.transfer(badAccount, okayAccount, 10.0));
		assertFalse(testBankSystem.transfer(lowAccount, okayAccount, 20.0));
		assertFalse(testBankSystem.transfer(okayAccount, lowAccount, -10.0));
		assertFalse(testBankSystem.transfer(okayAccount, lowAccount, Double.MAX_VALUE-okayAccount.getBalance()+1.0));
		assertFalse(testBankSystem.transfer(okayAccount, new Account(), 10.0));
		assertTrue(testBankSystem.transfer(okayAccount, lowAccount, 10.0));
		assertTrue(testBankSystem.transfer(okayAccount, lowAccount, 55.09));
		
	}
	
	
	@Test
	public void getUserByIdTest(){
		User u = new User();
		
		when(mockBank.viewUserById(0)).thenReturn(u); 
		when(mockBank.viewUserById(-1)).thenReturn(null);
		when(mockBank.viewUserById(-9)).thenReturn(null);
		when(mockBank.viewUserById(324)).thenReturn(u);
		when(mockBank.viewUserById(5000)).thenReturn(null);
		
		assertEquals(u, testBankSystem.getUserById(0));
		assertNull(testBankSystem.getUserById(-1));
		assertNull(testBankSystem.getUserById(-9));
		assertEquals(u, testBankSystem.getUserById(324));
		assertNull(testBankSystem.getUserById(5000));
	}
	
	@Test 
	public void getAccountByIdTest(){
		Account a = new Account();
		
		when(mockBank.viewAccountByAccountId(0)).thenReturn(a); 
		when(mockBank.viewAccountByAccountId(-1)).thenReturn(null);
		when(mockBank.viewAccountByAccountId(-9)).thenReturn(null);
		when(mockBank.viewAccountByAccountId(324)).thenReturn(a);
		when(mockBank.viewAccountByAccountId(5000)).thenReturn(null);
		
		assertEquals(a, testBankSystem.getAccountById(0));
		assertNull(testBankSystem.getAccountById(-1));
		assertNull(testBankSystem.getAccountById(-9));
		assertEquals(a, testBankSystem.getAccountById(324));
		assertNull(testBankSystem.getAccountById(5000));
	}
	
	@Test
	public void getUserViaAccountNumber(){
		User u = new User();
		
		when(mockBank.viewUserFromAccountId(0)).thenReturn(u); 
		when(mockBank.viewUserFromAccountId(-1)).thenReturn(null);
		when(mockBank.viewUserFromAccountId(-9)).thenReturn(null);
		when(mockBank.viewUserFromAccountId(324)).thenReturn(u);
		when(mockBank.viewUserFromAccountId(5000)).thenReturn(null);
		
		assertEquals(u, testBankSystem.getUserViaAccountNumber(0));
		assertNull(testBankSystem.getUserViaAccountNumber(-1));
		assertNull(testBankSystem.getUserViaAccountNumber(-9));
		assertEquals(u, testBankSystem.getUserViaAccountNumber(324));
		assertNull(testBankSystem.getUserViaAccountNumber(5000));
	}
}
