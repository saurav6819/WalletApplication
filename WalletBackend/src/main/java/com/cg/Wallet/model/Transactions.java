package com.cg.Wallet.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="transactions")
@SequenceGenerator(name="transactionseq",initialValue=3201,allocationSize=1)
public class Transactions {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="transactionseq")
	private int transactionid;
	private String action;
	private int moneyTransfer;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "account_id")
	private WalletAccountBean wallet;
	
	
	public int getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(int transactionid) {
		this.transactionid = transactionid;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getMoneyTransfer() {
		return moneyTransfer;
	}

	public void setMoneyTransfer(int moneyTransfer) {
		this.moneyTransfer = moneyTransfer;
	}
	public WalletAccountBean getWallet() {
		return wallet;
	}

	public void setWallet(WalletAccountBean wallet) {
		this.wallet = wallet;
	}

	@Override
	public String toString() {
		return "Transactions [transactionid=" + transactionid + ", action=" + action + ", moneyTransfer="
				+ moneyTransfer + "]";
	}

	


	
	
}
