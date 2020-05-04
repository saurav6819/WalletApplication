import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';
import { Transaction } from '../models/transaction.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  baseUrl: string = "http://localhost:6819/api";
  constructor(private http: HttpClient) { }


  updateUser(user: User) {
    return this.http.put(this.baseUrl + '/user/' + user.userid, user)
  }
  getAllUsers() {
    return this.http.get<User[]>(this.baseUrl + '/user');
  }
  showBalance(username) {
    return this.http.get(this.baseUrl + '/user/balance/' + username);
  }
  depositMoney(username, money) {
    return this.http.post(this.baseUrl + '/user/credit', { "username": username, "money": money }, { responseType: 'text' as 'json' })
  }
  showTransactions(username) {
    return this.http.get<Transaction>(this.baseUrl + '/transaction/' + username);
  }
  deleteUser(userid: number) {
    return this.http.delete(this.baseUrl + '/user/' + userid);
  }
  getAccountDetails(username) {
    return this.http.get<User>(this.baseUrl + '/user/' + username);
  }
  addAccount(user: User) {
    return this.http.post(this.baseUrl + '/user', user, { responseType: 'text' as 'json' });
  }

  authorise(username, password) {
    return this.http.post(this.baseUrl + '/authenticate', { "username": username, "password": password }, { responseType: 'text' as 'json' });
  }
  withdrawMoney(username, money) {
    return this.http.post(this.baseUrl + '/user/debit', { "username": username, "money": money }, { responseType: 'text' as 'json' })
  }
  checkValidUsername(username) {
    return this.http.get(this.baseUrl + '/checkUser/' + username);
  }
  fundTransfer(senderUsername, receiverUsername, moneyTransfered) {
    return this.http.post(this.baseUrl + '/fundtransfer', { "senderUsername": senderUsername, "receiverUsername": receiverUsername, "moneyTransfered": moneyTransfered }, { responseType: 'text' as 'json' })
  }
  checkValidEmail(email) {
    return this.http.get(this.baseUrl + '/checkEmail/' + email);
  }
  checkValidPhoneNumber(phNo) {
    return this.http.get(this.baseUrl + '/checkPhoneNumber/' + phNo);
  }
}
