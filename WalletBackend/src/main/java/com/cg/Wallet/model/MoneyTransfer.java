package com.cg.Wallet.model;

public class MoneyTransfer {

	private String username;
	private int money;
	
	public MoneyTransfer(String username, int money) {
		super();
		this.username = username;
		this.money = money;
	}
	public String getUsername() {
		return username;
	}
	public int getMoney() {
		return money;
	}

	
	
}
