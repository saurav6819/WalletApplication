package com.cg.Wallet.controller;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.Wallet.exception.BadCredentialException;
import com.cg.Wallet.exception.InvalidAttributeException;
import com.cg.Wallet.exception.WalletBalanceException;
import com.cg.Wallet.model.FundTransfer;
import com.cg.Wallet.model.LoginCredential;
import com.cg.Wallet.model.MoneyTransfer;
import com.cg.Wallet.model.Transactions;
import com.cg.Wallet.model.WalletAccountBean;
import com.cg.Wallet.service.IWalletService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class WalletController {

	@Autowired
	private IWalletService service;
	
	private Logger logger = LogManager.getLogger();
	
	//	Get Mapping for Fetching all transactions of particular user
	@GetMapping(value="/transaction/{username}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> showTransactions(@PathVariable String username) throws Exception
	{
		
		List<Transactions> list=service.showTransactions(username);
		logger.info(username+" fetches all transaction");
		return new ResponseEntity<Object>(list,HttpStatus.OK);
		
	}
	
	// Put Mapping for updating the user details
	@PutMapping(value="/user/{userid}",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> updateAccount(@RequestBody WalletAccountBean bean) throws InvalidAttributeException
	{
		logger.info(bean.getUsername()+" updates his account");	
		Boolean status=service.updateAccount(bean);
			return new ResponseEntity<Boolean>(status,HttpStatus.OK);

		
	}
	
	//	Post Mapping for transferring fund from one user to another
	@PostMapping(value="/fundtransfer",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> fundTransfer(@RequestBody FundTransfer transfer) throws InvalidAttributeException, WalletBalanceException
	{
		logger.info(transfer.getSenderUsername()+" transferring "+transfer.getMoneyTransfered()+" to "+transfer.getReceiverUsername());
		Boolean status=service.fundTransfer(transfer);
		return new ResponseEntity<Boolean>(status,HttpStatus.OK);
}

	//	Post Mapping for depositing money in user's account
	@PostMapping(value="user/credit",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> depositMoney(@RequestBody MoneyTransfer moneyTransfer) throws WalletBalanceException, InvalidAttributeException
	{
		Boolean status=service.depositMoney(moneyTransfer);
		logger.info(moneyTransfer.getUsername()+" deposits "+moneyTransfer.getMoney());
		return new ResponseEntity<Boolean>(status,HttpStatus.OK);

	}
	
	//	Post Mapping for withdrawing money from user's account
	@PostMapping(value="user/debit",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> withdrawMoney(@RequestBody MoneyTransfer moneyTransfer) throws WalletBalanceException, InvalidAttributeException
	{
		Boolean status=service.withdrawMoney(moneyTransfer);
		logger.info(moneyTransfer.getUsername()+" withdraws "+moneyTransfer.getMoney());
		return new ResponseEntity<Object>(status,HttpStatus.OK);
						
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<WalletAccountBean>> showUsers() throws Exception
	{
		List<WalletAccountBean> list=service.showAllUser();
		logger.info("Admin requests for all User");
		return new ResponseEntity<List<WalletAccountBean>>(list,HttpStatus.OK);
	}
	
	//	Post Mapping for adding new user account
	@PostMapping(value="/user",consumes= MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addAccount(@Valid @RequestBody WalletAccountBean bean) 
	{
		
		service.addAccount(bean);
		logger.info(bean.getName()+"'s Account added");
		return new ResponseEntity<Boolean>(true,HttpStatus.OK);
	}
	
	//  Post Mapping for checking the login credential
	@PostMapping(value="/authenticate",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkLoginRequest(@RequestBody LoginCredential login) throws BadCredentialException
	{
		Boolean status=service.checkValidUser(login);
		logger.info(login.getUsername()+" has logged in to his account");
		return new ResponseEntity<Boolean>(status,HttpStatus.OK);

	}
	
	// Get Mapping for validating the username
	@GetMapping(value="/checkUser/{username}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkUsername(@PathVariable String username) throws InvalidAttributeException
	{
		Boolean status=service.checkValidUsername(username);
		logger.warn(username+" validating");
		return new ResponseEntity<Boolean>(status,HttpStatus.OK);
		
		
	}
	
	// Get Mapping for fetching user current balance
	@GetMapping(value="/user/balance/{username}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> showBalance(@PathVariable String username) throws InvalidAttributeException
	{
		int balance=service.showBalance(username);
		logger.info(username+ " Fetches current balance");
		return new ResponseEntity<Object>(balance,HttpStatus.OK);

	}
	
	// Function to fetch user's data from his username
	@GetMapping(value="/user/{username}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getUser(@PathVariable String username) throws InvalidAttributeException
	{
			WalletAccountBean user=service.account(username);
			logger.warn(username+" fetches his account details");
			return new  ResponseEntity<Object>(user,HttpStatus.OK);
	}
	
	// Delete Mapping for deleting user's data using user id
	@DeleteMapping(value="/user/{userid}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<WalletAccountBean>> deleteUser(@PathVariable int userid) throws Exception
	{	
		
		List<WalletAccountBean> list=service.deleteAccount(userid);
		logger.warn(userid+" account has been deleted");
		return new ResponseEntity<List<WalletAccountBean>>(list,HttpStatus.OK);
	
	}
	
	// Get Mapping to check whether given email already exists
	@GetMapping(value="/checkEmail/{email}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkEmail(@PathVariable String email) throws InvalidAttributeException
	{
		
		Boolean status=service.checkEmail(email);
		logger.warn(email+" validating");
		return new ResponseEntity<Boolean>(status,HttpStatus.OK);

	}
	
	//	Get Mapping to check whether given phone number already exists
	@GetMapping(value="/checkPhoneNumber/{phNo}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkPhoneNumber(@PathVariable String phNo) throws InvalidAttributeException
	{
		Boolean status=service.checkPhoneNumber(phNo);
		logger.warn(phNo+" validating");
		return new ResponseEntity<Boolean>(status,HttpStatus.OK);

	}
}
