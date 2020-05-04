package com.cg.Wallet.dao;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import com.cg.Wallet.model.FundTransfer;
import com.cg.Wallet.model.LoginCredential;
import com.cg.Wallet.model.Transactions;
import com.cg.Wallet.model.WalletAccountBean;
@Repository("dao")
public class WalletDaoImpl implements IWalletDao {

	@PersistenceContext
	private EntityManager entity;
	
	
	//Function to fetch all transactions of particular user
	// Accepts username of the user as parameter 
	//Returns a list of Transaction of that user
	@Override
	public List<Transactions> showTransactions(String username) {
		WalletAccountBean user=account(username);
		String qStr = "SELECT transaction FROM Transactions transaction WHERE account_id=:uid ORDER BY 1 DESC";
		TypedQuery<Transactions> query = entity.createQuery(qStr, Transactions.class);
		query.setParameter("uid", user.getUserid());
		return query.getResultList();
	}
	
	// Function to show all user in database
	// Returns a list of User
	@Override
	public List<WalletAccountBean> showAllUser() {
		String str="SELECT users FROM WalletAccountBean users WHERE users.username != \'admin\' ORDER BY 1";
		TypedQuery<WalletAccountBean> query=entity.createQuery(str,WalletAccountBean.class);
		return query.getResultList();
	}
	
	//	Function to add new user account 
	//	Accepts the Bean class WalletAccount
	//	Returns a boolean value
	//				-> False :- If account hasn't been added
	//				-> True	 :- After account has been added
	@Override
	public boolean addAccount(WalletAccountBean bean) {
		bean.setEmail(bean.getEmail().toLowerCase());
		bean.setUsername(bean.getUsername().toLowerCase());
		entity.persist(bean);
		if(entity.find(WalletAccountBean.class, bean.getUserid()) != null)
		return true;
		else
			return false;
	}
	
	//	Function to update an account
	//	Accepts a bean class WalletAccount
	//	Returns the managed instance that the state was merged to
	@Override
	public boolean updateAccount(WalletAccountBean bean) {
		
		bean.setEmail(bean.getEmail().toLowerCase());
		bean.setUsername(bean.getUsername().toLowerCase());
		Object obj=entity.merge(bean);
		entity.flush();
		if(obj != null)
		return true;
		else
			return false;
	}
	
	// 	Function for transferring fund from one user to another
	//	Accepts a FundTransfer class where receiver's ,sendeer's username and amount is present
	//	Returns a boolean value 
	//		->False :-  If sender don't have enough amount to transfer
	//		->True	:-	If successful transaction happens
	@Override
	public boolean fundTransfer(FundTransfer transfer) {
		WalletAccountBean senderAccount=account(transfer.getSenderUsername());
		WalletAccountBean receiverAccount=account(transfer.getReceiverUsername());
		if(senderAccount.getBalance()<transfer.getMoneyTransfered())
		{
			return false;
		}
		else
		{
			Transactions sendersTransaction = new Transactions();
			sendersTransaction.setAction("Fund Transfer");
			sendersTransaction.setMoneyTransfer(transfer.getMoneyTransfered());
			int updatedBalance = senderAccount.getBalance()-transfer.getMoneyTransfered();
			senderAccount.setBalance(updatedBalance);
			senderAccount.addTransaction(sendersTransaction);
			entity.merge(senderAccount);
			
			Transactions receiversTransaction = new Transactions();
			receiversTransaction.setAction("Credit");
			receiversTransaction.setMoneyTransfer(transfer.getMoneyTransfered());
			updatedBalance = receiverAccount.getBalance() + transfer.getMoneyTransfered();
			receiverAccount.setBalance(updatedBalance);
			receiverAccount.addTransaction(receiversTransaction);
			entity.merge(receiverAccount);
			return true;
		}
	}
	
	//	Function for checking valid username
	//	Accepts username to check in the database
	//	Returns a boolean value
	//			-> False :- If username not exist in database
	//			-> True  :- If username found 
	@Override
	public boolean checkValidUsername(String username) {
		
		String qStr = "SELECT wallet.username FROM WalletAccountBean wallet";
		TypedQuery<String> query = entity.createQuery(qStr, String.class);
		List<String> list = query.getResultList().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
		if (list.contains(username.toLowerCase()))
			return true;
		return false;
	}
	
	//  Function for fetching the user's data
	//	Accepts username to fetch data of that username
	//	Returns the account of the user 
	@Override
	public WalletAccountBean account(String username) {
		String qStr = "SELECT account FROM WalletAccountBean account WHERE account.username=:user";
		TypedQuery<WalletAccountBean> query = entity.createQuery(qStr, WalletAccountBean.class);
		query.setParameter("user", username.toLowerCase());
		return query.getSingleResult();
	}
	
	//	Function for checking the login credentials
	//	Accepts LoginCredential class which contains username and password
	//	Returns a boolean value
	//				->	False :- If credential doesn't match with the database
	//				->	True  :- If credential found in database
	@Override
	public boolean checkValidUser(LoginCredential credential) {
		if (checkValidUsername(credential.getUsername())) {
			String qStr = "SELECT wallet.password FROM WalletAccountBean wallet WHERE wallet.username=:user";
			TypedQuery<String> query = entity.createQuery(qStr, String.class);
			query.setParameter("user", credential.getUsername().toLowerCase());
			String passKey = query.getSingleResult();
			if (credential.getPassword().equals(passKey))
				return true;
		}
		return false;
		
	}
	
	//	Function to deposit money in user's account
	//	Accepts username and amount to be deposited to that user
	//	Returns true after money is deposited
	@Override
	public boolean depositMoney(String username, int amount) {
		WalletAccountBean user=account(username);
			Transactions transaction=new Transactions();
			transaction.setAction("Credit");
			transaction.setMoneyTransfer(amount);
			
			int currBalance=user.getBalance();
			user.setBalance(amount+currBalance);
			user.addTransaction(transaction);
			entity.persist(user);
			return true;
		
	}
	
	// 	Function for withdrawing money from user's account
	//	Accepts username and amount to be withdraw from that user
	//	Returns a boolean value
	//				-> 	False :- If user's current money is less then withdrawing amount
	//				->	True  :- After successful transaction
	@Override
	public boolean withdrawMoney(String username, int amount) {
		WalletAccountBean user=account(username);
		if(user.getBalance()<amount)
			return false;
		else
		{
			Transactions transaction=new Transactions();
			transaction.setAction("Debit");
			transaction.setMoneyTransfer(amount);
			
			int currBalance=user.getBalance();
			user.setBalance(currBalance-amount);
			user.addTransaction(transaction);
			entity.persist(user);
			return true;
		}
	}
	
	//	Function for fetching user current balance
	//	Accepts username to fetch the balance 
	//	Returns the user's current balance
	@Override
	public int showBalance(String username) {
		String qStr = "SELECT wallet.balance FROM WalletAccountBean wallet WHERE wallet.username=:user";
		TypedQuery<Integer> query = entity.createQuery(qStr, Integer.class);
		query.setParameter("user", username.toLowerCase());
		int balance= query.getSingleResult();
		return balance;
	}
	
	//	Function to delete user account
	//	Accepts the userid of the user to delete that account
	//	Returns a boolean value 
	//				-> False :- If user account doesn't exists
	//				-> True  :- After user account found and deleted
	@Override
	public boolean deleteAccount(int userid) {
		WalletAccountBean user=entity.find(WalletAccountBean.class, userid);
		if(user!=null)
		{
			String str="DELETE FROM Transactions transaction WHERE transaction.wallet.userid=:uid";
			Query query=entity.createQuery(str);
			query.setParameter("uid",userid).executeUpdate();
			entity.remove(user);
			return true;
		}
		else
			return false;
		
	}
	
	//	Function to check whether email already exists
	//	Accepts email to check whether it exists in database or not
	//	Returns a boolean value
	//				-> False :- If email not found in database
	//				-> True  :- If email  found in database
	@Override
	public boolean checkEmail(String email) {
		String qStr = "SELECT wallet.email FROM WalletAccountBean wallet";
		TypedQuery<String> query = entity.createQuery(qStr, String.class);
		List<String> list = query.getResultList().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
		if (list.contains(email.toLowerCase()))
			return true;
		return false;
	}
	
	// Function to check whether phone number already exists
	//	Accepts phone number to check whether it exists in database or not
	// Returns a boolean value
	//				-> False :- If phone number not found in database
	//				-> True  :- If phone number found in database
	@Override
	public boolean checkPhoneNumber(String phNo) {
		String qStr = "SELECT wallet.phNo FROM WalletAccountBean wallet";
		TypedQuery<String> query = entity.createQuery(qStr, String.class);
		List<String> list = query.getResultList();
		if (list.contains(phNo))
			return true;
		return false;
	}
}
