package com.cg.Wallet.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="walletaccount")
@SequenceGenerator(name="userseq",initialValue=101,allocationSize=1)
public class WalletAccountBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="userseq")
	@Column(length=5)
	private int userid;
	@Column(length=20)
	@NotEmpty(message="Name cannot be empty")
	private String name;
	@Column(length=5)
	private int balance;
	@Column(length=10)
	@NotEmpty(message="Phone Number cannot be empty")
	private String phNo;
	@Column(length=20)
	@NotEmpty(message="Password cannot be empty")
	private String password;
	@Column(length=10)
	@NotEmpty(message="username cannot be empty")
	
	private String username;
	@Column(length=6)
	@NotEmpty(message="Gender cannot be empty")
	@Pattern(regexp="Male|Female|Other",message="Gender can be Male,Female or Other")
	private String gender;
	@Column(length=20)
	@NotEmpty(message="Email cannot be empty")
	private String email;
	@OneToMany(mappedBy="wallet",cascade=CascadeType.ALL)
	private Set<Transactions> transactions=new HashSet();
	
	public WalletAccountBean()
	{	}

	public WalletAccountBean(String name, int balance, String phNo, String password, String username,
			String gender, String email) {
		super();
		
		this.name = name;
		this.balance = balance;
		this.phNo = phNo;
		this.password = password;
		this.username = username;
		this.gender = gender;
		this.email = email;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getPhNo() {
		return phNo;
	}

	public void setPhNo(String phNo) {
		this.phNo = phNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	@JsonIgnore
	public Set<Transactions> getTransaction() {
		return transactions;
	}

	public void setTransaction(Set<Transactions> transactions) {
		this.transactions = transactions;
	}
	
	public void addTransaction(Transactions transaction)
	{
		transaction.setWallet(this);
		this.getTransaction().add(transaction);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "WalletAccountBean [userid=" + userid + ", name=" + name + ", balance=" + balance + ", phNo=" + phNo
				+ ", username=" + username + ", gender=" + gender + ", email=" + email + "]";
	}
	
	

}
