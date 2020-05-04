package com.cg.Wallet.service;

import java.util.List;

import com.cg.Wallet.exception.BadCredentialException;
import com.cg.Wallet.exception.InvalidAttributeException;
import com.cg.Wallet.exception.WalletBalanceException;
import com.cg.Wallet.model.FundTransfer;
import com.cg.Wallet.model.LoginCredential;
import com.cg.Wallet.model.MoneyTransfer;
import com.cg.Wallet.model.Transactions;
import com.cg.Wallet.model.WalletAccountBean;

public interface IWalletService {
public List<WalletAccountBean> showAllUser() throws Exception;
public boolean addAccount(WalletAccountBean bean);
public boolean updateAccount(WalletAccountBean bean) throws InvalidAttributeException;
public List<WalletAccountBean> deleteAccount(int userid) throws InvalidAttributeException;
public boolean checkValidUsername(String username) throws  InvalidAttributeException;
public WalletAccountBean account(String username) throws InvalidAttributeException;
public boolean checkValidUser(LoginCredential credential) throws BadCredentialException;
public boolean fundTransfer(FundTransfer transfer) throws InvalidAttributeException, WalletBalanceException;
public int showBalance(String username) throws InvalidAttributeException;
public boolean checkEmail(String email) throws InvalidAttributeException;
public boolean checkPhoneNumber(String phNo) throws InvalidAttributeException;
List<Transactions> showTransactions(String username) throws InvalidAttributeException, Exception;
boolean depositMoney(MoneyTransfer moneyTransfer) throws WalletBalanceException, InvalidAttributeException;
boolean withdrawMoney(MoneyTransfer moneyTransfer) throws WalletBalanceException, InvalidAttributeException;
}
