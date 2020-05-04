package com.cg.Wallet.dao;

import java.util.List;

import com.cg.Wallet.model.FundTransfer;
import com.cg.Wallet.model.LoginCredential;
import com.cg.Wallet.model.Transactions;
import com.cg.Wallet.model.WalletAccountBean;

public interface IWalletDao {
	public List<WalletAccountBean> showAllUser();
	public boolean addAccount(WalletAccountBean bean);
	public boolean updateAccount(WalletAccountBean bean);
	public boolean deleteAccount(int userid);
	public boolean fundTransfer(FundTransfer transfer);
	public boolean checkValidUsername(String username);
	public WalletAccountBean account(String username);
	public boolean checkValidUser(LoginCredential credential);
	public int showBalance(String username);
	public boolean checkEmail(String email);
	public boolean checkPhoneNumber(String phNo);
	List<Transactions> showTransactions(String username);
	public boolean depositMoney(String username, int amount);
	public boolean withdrawMoney(String username, int amount);
}
