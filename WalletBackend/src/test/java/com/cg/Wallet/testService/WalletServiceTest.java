package com.cg.Wallet.testService;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cg.Wallet.exception.BadCredentialException;
import com.cg.Wallet.exception.InvalidAttributeException;
import com.cg.Wallet.exception.WalletBalanceException;
import com.cg.Wallet.model.FundTransfer;
import com.cg.Wallet.model.LoginCredential;
import com.cg.Wallet.model.MoneyTransfer;
import com.cg.Wallet.model.Transactions;
import com.cg.Wallet.model.WalletAccountBean;
import com.cg.Wallet.service.IWalletService;

import org.apache.logging.log4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WalletServiceTest {

	private static Logger logger;

	@Autowired
	private IWalletService service;
	
	@BeforeAll
	static void setUpBeforeClass() {
		logger = LogManager.getLogger();
		System.out.println("Fetching resources for testing ...");
	}
	
	@BeforeEach
	void setup() {
		System.out.println("Test Case Started");
	}

	@AfterEach
	void tearDown() {
		System.out.println("Test Case Over");	
	}
	
	@AfterAll
	static void setUpAfterClass() {
		System.out.println("All test cases ended.");
	}
	
	@Test
	@DisplayName("Test for Fetching Transactions")
	public void userHavingTransactionTest() throws Exception{
		
		//Test for, if user have transactions
		List<Transactions> list=service.showTransactions("ssuman7");
		assertFalse(list.isEmpty());
		
		// Test for, if user don't have any transaction
		assertThrows(InvalidAttributeException.class,()->
		{
			service.showTransactions("gjot_02");
		});
		
		//Test for invalid user
		assertThrows(InvalidAttributeException.class,()->
		{
			service.showTransactions("king");
		});
	}
	
	@Test
	@DisplayName("Test for fetching all user")
	public void showAllUserTest() throws Exception{
		List<WalletAccountBean> list=service.showAllUser();
		assertFalse(list.isEmpty());
		
	}
	
	
	@Test
	@DisplayName("Test for adding an account")
	public void addAccountTest()
	{
		WalletAccountBean bean=new WalletAccountBean("Sam",0,"9097032922","Password@1","sam_02","Male","sam123@gmail.com");
		assertEquals(true,service.addAccount(bean));
	}
	
	
	@Test
	@DisplayName("Test for updating an account")
	public void updateAccountHavingNoAccountTest() throws InvalidAttributeException
	{
		// If user having no account in database
		WalletAccountBean bean=new WalletAccountBean("Sam",0,"9097032922","Password#1","sam2","Male","sam123@gmail.com");
		bean.setUserid(1001);
		assertThrows(InvalidAttributeException.class,()->{
			service.updateAccount(bean);
		});
		
		// If user has an account in database
		WalletAccountBean bean2=new WalletAccountBean("Saurav Suman",11600,"9097032922","Space@6819","ssuman7","Male","sauravsuman@gmail.com");
		bean2.setUserid(101);
		assertEquals(true,service.updateAccount(bean2));
		
	}
	
	
	@Test
	@DisplayName("Test for validating username")
	public void validatingUsernameTest() throws InvalidAttributeException
	{
		// If username doesn't exist
		assertEquals(true,service.checkValidUsername("shekhar"));
		
		// If username exist
		assertThrows(InvalidAttributeException.class,()->{
			service.checkValidUsername("ssuman7");
			});
		
	}
	
	@Test
	@DisplayName("Test for fetching user details using username")
	public void fetchAccountTest() throws InvalidAttributeException
	{
		//If User exists
		WalletAccountBean bean=service.account("ssuman7");
		assertNotEquals(null, bean);
		
		//If User doesn't exists
		assertThrows(InvalidAttributeException.class,()->{
			service.account("shekhar");
			});
		
	}
	
	@Test
	@DisplayName("Test for authenticating the user")
	public void checkLoginCredentialTest() throws InvalidAttributeException, BadCredentialException
	{
		// Test for Valid credential
		LoginCredential credential=new LoginCredential("ssuman7","Space@6819");
		assertEquals(true,service.checkValidUser(credential));
		 
		// Test for Invalid Credential
		assertThrows(BadCredentialException.class,()->
		{
			LoginCredential credential2=new LoginCredential("sam","Sam@1234");
			service.checkValidUser(credential2);
		});
		
	}
	
	@Test
	@DisplayName("Test for Fund Transfer")
	public void fundTransferTest() throws InvalidAttributeException, WalletBalanceException
	{
		//Test for, valid Fund Transfer
		FundTransfer transfer=new FundTransfer("ssuman7","rp1304",5000);
		assertEquals(true,service.fundTransfer(transfer));
		
		// Test for ,if username doesn't exist
		assertThrows(InvalidAttributeException.class,()->{
			FundTransfer transfers=new FundTransfer("ssuman7","sam1234",5000);
			service.fundTransfer(transfers);
		});
		
		// 	Test for,if user tries to transfer to own account
		assertThrows(InvalidAttributeException.class,()->{
			FundTransfer transfers=new FundTransfer("ssuman7","ssuman7",5000);
			service.fundTransfer(transfers);
		});
		
		// Test for, if user tries to transfer more than 10000
		assertThrows(WalletBalanceException.class,()->{
			FundTransfer transfers=new FundTransfer("ssuman7","rp1304",12000);
			service.fundTransfer(transfers);
		});
		
		// Test for, if user tries to transfer 0 or less than that 
		assertThrows(WalletBalanceException.class,()->{
			FundTransfer transfers=new FundTransfer("ssuman7","rp1304",0);
			service.fundTransfer(transfers);
		});
		
	}
	
	@Test
	@DisplayName("Test for Deposit Money")
	public void depositMoneyTest() throws InvalidAttributeException, WalletBalanceException
	{
		//Test for, valid Deposit money
		MoneyTransfer transfer=new MoneyTransfer("div009",5000);
		assertEquals(true,service.depositMoney(transfer));
		
		//Test for, if user's wallet limit exceeds
		assertThrows(WalletBalanceException.class,()->{
			MoneyTransfer transfers=new MoneyTransfer("sonali.1009",5000);
			service.depositMoney(transfers);
		});
		
		// Test for, If user tries to deposit more then 6000
		assertThrows(WalletBalanceException.class,()->{
			MoneyTransfer transfers=new MoneyTransfer("gjot_02",6000);
			service.depositMoney(transfers);
		});
		// Test for, If user tries to deposit 0 or less than that
		assertThrows(WalletBalanceException.class,()->{
			MoneyTransfer transfers=new MoneyTransfer("rp1304",0);
			service.depositMoney(transfers);
		});
		
		// Test for Inavalid user
		assertThrows(InvalidAttributeException.class,()->{
			MoneyTransfer transfers=new MoneyTransfer("sam1234",0);
			service.depositMoney(transfers);
		});
	}
	
	@Test
	@DisplayName("Test for Withdraw Money")
	public void withdrawMoneyTest() throws InvalidAttributeException, WalletBalanceException
	{
		// Test for, valid Deposit money
		MoneyTransfer transfer=new MoneyTransfer("ssuman7",5000);
		assertEquals(true,service.withdrawMoney(transfer));
		
		// Test for,if user don't have enough balance
		assertThrows(WalletBalanceException.class,()->{
			MoneyTransfer transfers=new MoneyTransfer("amit007",5000);
			service.withdrawMoney(transfers);
		});
		
		// Test for, if user tries to withdraw more than 6000 
		assertThrows(WalletBalanceException.class,()->{
			MoneyTransfer transfers=new MoneyTransfer("rp1304",6000);
			service.withdrawMoney(transfers);
		});
		
		// Test for, if user tries to withdraw 0 or less than that
		assertThrows(WalletBalanceException.class,()->{
			MoneyTransfer transfers=new MoneyTransfer("rp1304",0);
			service.withdrawMoney(transfers);
		});
		
		// Test for inavlid user 
		assertThrows(InvalidAttributeException.class,()->{
			MoneyTransfer transfers=new MoneyTransfer("sam1234",0);
			service.withdrawMoney(transfers);
		});
	}
	
	@Test
	@DisplayName("Test for fetching user's balance")
	public void fetchUserBalanceTest() throws InvalidAttributeException
	{
		// Test for valid user
		int balance=service.showBalance("ssuman7");
		assertNotEquals(0,balance);
		
		// Test for inavlid user
		assertThrows(InvalidAttributeException.class,()->{
			service.showBalance("sam1234");
			});
		
	}
	
	@Test
	@DisplayName("Test to validate Email Id")
	public void validateEmailTest() throws InvalidAttributeException
	{
		// Test for, if email not exists
		boolean bool=service.checkEmail("burnyoudown@gmail.com");
		assertEquals(false,bool);
		
		// Test if email already exists
		assertThrows(InvalidAttributeException.class,()->{
			service.checkEmail("sauravsuman6819@gmail.com");
			});
		
	}
	
	@Test
	@DisplayName("Test to validate Phone Number")
	public void validatePhoneNumberTest() throws InvalidAttributeException
	{
		// Test for, if phone number not exists
		boolean bool=service.checkPhoneNumber("8409586389");
		assertEquals(false,bool);
		
		// Test if phone number already exists
		assertThrows(InvalidAttributeException.class,()->{
			service.checkPhoneNumber("9097032922");
			});
		
	}
	
	@Test
	@DisplayName("Test to Delete an account")
	public void deleteAccountTest() throws InvalidAttributeException
	{
		// Test for, if user exists
		List<WalletAccountBean> list=service.deleteAccount(325);
		assertFalse(list.isEmpty());
		
		// Test for, if user doen't exist
		assertThrows(InvalidAttributeException.class,()->{
			service.deleteAccount(100);
		});
		
	}
	
	
}
