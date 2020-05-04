package com.cg.Wallet.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.Wallet.dao.IWalletDao;
import com.cg.Wallet.exception.BadCredentialException;
import com.cg.Wallet.exception.InvalidAttributeException;
import com.cg.Wallet.exception.WalletBalanceException;
import com.cg.Wallet.model.FundTransfer;
import com.cg.Wallet.model.LoginCredential;
import com.cg.Wallet.model.MoneyTransfer;
import com.cg.Wallet.model.Transactions;
import com.cg.Wallet.model.WalletAccountBean;

@Transactional
@Service("service")
public class WalletServiceImpl implements IWalletService {

	@Autowired
	private IWalletDao dao;

	private Logger logger = LogManager.getLogger();	
	
	@Override
	public List<Transactions> showTransactions(String username) throws Exception {

		if(dao.checkValidUsername(username))
			{List<Transactions> list = dao.showTransactions(username);
			if(list.size()==0) {
				logger.warn("No transactions available for "+username);
				throw new InvalidAttributeException("No Transaction Available Yet");
			}
			else
				return list;
			}
		else {
			logger.error(username+" Doesn't exist ");
			throw new InvalidAttributeException("User Doesn't exist...");
		}
		
	}
	
	@Override
	public List<WalletAccountBean> showAllUser() throws Exception {
		List<WalletAccountBean> list=dao.showAllUser();
			return list;
	}
	
	@Override
	public boolean addAccount(WalletAccountBean bean) {
		return dao.addAccount(bean);
	}
	
	@Override
	public boolean updateAccount(WalletAccountBean bean) throws InvalidAttributeException {
		if(dao.checkValidUsername(bean.getUsername()))
		return dao.updateAccount(bean);
		else {
			logger.error(bean.getUsername()+" Doesn't exist");
			throw new InvalidAttributeException("Username Doesn't Exist..!!!");
		}
	}
	
	@Override
	public boolean checkValidUsername(String username) throws InvalidAttributeException {
		Boolean status= dao.checkValidUsername(username);
		if(status) {
			logger.error(username +" Already exist");
			throw new InvalidAttributeException("Username Already Exist..!!!");
		}
		else
			return true;
			
	}
	
	@Override
	public WalletAccountBean account(String username) throws InvalidAttributeException {
		if(dao.checkValidUsername(username))
		return dao.account(username);
		else {
			logger.warn("Request for fetching account details... "+username +" doesn't exist");
			throw new InvalidAttributeException("Username Doesn't Exist..!!!");
		}
	}
	
	@Override
	public boolean checkValidUser(LoginCredential credential) throws BadCredentialException {
		Boolean status= dao.checkValidUser(credential);
		if(status)
			return true;
		else {
			logger.warn("Validating "+credential.getUsername()+"  ...Username doesn't exist");
			throw new BadCredentialException("Wrong Username or Password");
		}
	}	
	
	@Override
	public boolean fundTransfer(FundTransfer transfer) throws InvalidAttributeException, WalletBalanceException {
		
		if(transfer.getReceiverUsername().equalsIgnoreCase(transfer.getSenderUsername())) {
			logger.warn(transfer.getSenderUsername()+" Trying to fund transfer to own account");
			throw new InvalidAttributeException("Cannot deposit to own Account");
		}
		else if(dao.checkValidUsername(transfer.getReceiverUsername()) && dao.checkValidUsername(transfer.getSenderUsername()))
		{
			if(transfer.getMoneyTransfered()>10000) {
				logger.warn(transfer.getSenderUsername()+" Trying to transfer more than 10000");
				throw new WalletBalanceException("Cannot transfer more than 10000...!!!");
			}
			else if(transfer.getMoneyTransfered()<=0) {
				logger.warn(transfer.getSenderUsername()+" Trying to fund transfer 0 or less then that");
				throw new WalletBalanceException("Cannot transfer 0 or less than that...!!!");
			}
			else if((dao.showBalance(transfer.getReceiverUsername())+transfer.getMoneyTransfered())>15000)
			{	
				logger.warn(transfer.getReceiverUsername()+"'s wallet limit reached");
				throw new WalletBalanceException("Receiver's wallet limit reached");
			}
				
			Boolean status=dao.fundTransfer(transfer);
			 if(status)
				return true;
			else {
				logger.error(transfer.getSenderUsername()+" has insufficient Balance available to transfer");
				throw new WalletBalanceException("Insufficient Balance Available..!!!");
			}
		}
		else {
			logger.error(transfer.getReceiverUsername()+" Doesn't Exist");
			throw new InvalidAttributeException("Username Doesn't Exist..!!!");
		}
		
		

	}
	
	@Override
	public boolean depositMoney(MoneyTransfer moneyTransfer) throws WalletBalanceException, InvalidAttributeException {

		if(dao.checkValidUsername(moneyTransfer.getUsername()))
		{
			if((dao.showBalance(moneyTransfer.getUsername())+moneyTransfer.getMoney())>15000)
			{
				logger.warn(moneyTransfer.getUsername()+" Wallet Limit Exceeded");
				throw new WalletBalanceException("Wallet limit exceeded");
			}
			else if(moneyTransfer.getMoney()>5000) {
				logger.warn(moneyTransfer.getUsername()+" Trying to deposit money greater than 5000");
				throw new WalletBalanceException("Cannot deposit money greater than 5000 at once");
			}
			else if(moneyTransfer.getMoney()<=0) {
				logger.warn(moneyTransfer.getUsername()+" Trying to deposit 0 or less than that");
				throw new WalletBalanceException("Cannot deposit 0 or less than that");
			}
			else
				return dao.depositMoney(moneyTransfer.getUsername(), moneyTransfer.getMoney());
		}
		else {
			logger.error(moneyTransfer.getUsername()+" Doesn't Exist");
			throw new InvalidAttributeException("Username Doesn't Exist..!!!");
		}
		
	}
	
	@Override
	public boolean withdrawMoney(MoneyTransfer moneyTransfer) throws WalletBalanceException, InvalidAttributeException {

		if(dao.checkValidUsername(moneyTransfer.getUsername()))
		{
			if(moneyTransfer.getMoney()>5000) {
				logger.error(moneyTransfer.getUsername()+" Cannot withdraw money greater than 5000 at once");
				throw new WalletBalanceException(" Cannot withdraw money greater than 5000 at once");
			}
			else if(moneyTransfer.getMoney()<=0) {
				logger.error(moneyTransfer.getUsername()+" Cannot withdraw 0 or less than that");
				throw new WalletBalanceException(" Cannot withdraw 0 or less than that");
			}
			Boolean status=dao.withdrawMoney(moneyTransfer.getUsername(), moneyTransfer.getMoney());
			if(status)
				return true;
			else {
				logger.error(moneyTransfer.getUsername()+" has insufficient balance to withdraw");
				throw new WalletBalanceException("Insufficient balance to withdraw");
			}
		}
		else
		{
			logger.error(moneyTransfer.getUsername()+" Doesn't Exist..!!!");
			throw new InvalidAttributeException("Username Doesn't Exist..!!!");
		}
	}
	
	@Override
	public int showBalance(String username) throws InvalidAttributeException {
		
		if(dao.checkValidUsername(username))
			return dao.showBalance(username);
		else
		{logger.error(username+" Doesn't Exist..!!!");
			throw new InvalidAttributeException("Username Doesn't Exist..!!!");
		}
	}
	
	@Override
	public List<WalletAccountBean> deleteAccount(int userid) throws InvalidAttributeException {
		Boolean status=dao.deleteAccount(userid);
		 if(status)
		 return dao.showAllUser();
		 else			 {
			 logger.error(userid +" Doesn't Exist..!!!");
			 throw new InvalidAttributeException("Username Doesn't Exist..!!!");
		 }
	}
	
	@Override
	public boolean checkEmail(String email) throws InvalidAttributeException {

		Boolean status= dao.checkEmail(email);
		if(status) {
			logger.error(email+" already exists..!!!");
			throw new InvalidAttributeException("Email already exists..!!!");
		}
		else
			return false;
	}
	
	@Override
	public boolean checkPhoneNumber(String phNo) throws InvalidAttributeException {
		if(phNo.length()!=10)
		{	logger.error(phNo+" Invalid Phone Number..!!!");
			throw new InvalidAttributeException(" Invalid Phone Number..!!!");
		}
		Boolean status= dao.checkPhoneNumber(phNo);
		if(status)
		{
			logger.error(phNo+" Phone Number already exists..!!!");
			throw new InvalidAttributeException(" Phone Number already exists..!!!");
		}
		else
			return false;
	}
	
}
