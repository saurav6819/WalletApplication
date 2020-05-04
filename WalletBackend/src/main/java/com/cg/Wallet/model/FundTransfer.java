package com.cg.Wallet.model;

public class FundTransfer {

	private String senderUsername;
	private String receiverUsername;
	private int moneyTransfered;
	
	public FundTransfer(String senderUsername, String receiverUsername, int moneyTransfered) {
		super();
		this.senderUsername = senderUsername;
		this.receiverUsername = receiverUsername;
		this.moneyTransfered = moneyTransfered;
	}
	public String getSenderUsername() {
		return senderUsername;
	}	
	public String getReceiverUsername() {
		return receiverUsername;
	}
	
	public int getMoneyTransfered() {
		return moneyTransfered;
	}
	
	
}
