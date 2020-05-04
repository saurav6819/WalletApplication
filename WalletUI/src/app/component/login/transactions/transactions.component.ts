import { Component, OnInit } from '@angular/core';
import { Transaction } from 'src/app/models/transaction.model';
import { UserService } from 'src/app/service/user.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {

  transactions:Transaction
  transactionFlag:boolean=false
  transactionErrorMessage:string
  constructor(private service:UserService) {
    this.service.showTransactions(localStorage.getItem("username")).subscribe(data=>
      {
        this.transactions=data
        
      },(err:HttpErrorResponse)=>
      {
        this.transactionFlag=true;
        this.transactionErrorMessage=err.error
      })
   }


  ngOnInit(): void {
    
  }

}
