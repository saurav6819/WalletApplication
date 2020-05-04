import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-fund-transfer',
  templateUrl: './fund-transfer.component.html',
  styleUrls: ['./fund-transfer.component.css']
})
export class FundTransferComponent implements OnInit {

  submitted:boolean=false;
  fundTransferForm:FormGroup
  transferMoney;
  receiverUsername;
  errorFlag:boolean=false;
  errorMessage:string;
  constructor(private service:UserService,private formBuilder:FormBuilder,private route:Router) { 
    this.fundTransferForm=this.formBuilder.group({
      receiverUsername:['',Validators.required],
      money:['',Validators.required]
    });
   
  }

  ngOnInit(): void {
  }

  transfer()
  {
    this.submitted=true;
    if(this.fundTransferForm.invalid)
    return;
    this.transferMoney=this.fundTransferForm.controls.money.value;
    this.receiverUsername=this.fundTransferForm.controls.receiverUsername.value;
    this.service.fundTransfer(localStorage.getItem("username"),this.receiverUsername,this.transferMoney).subscribe(data=>
      {
        alert("Fund Transfered")
        this.route.navigate(['/loggedin/showBalance'])
      },(err:HttpErrorResponse)=>
      {
        this.errorFlag=true;
        this.errorMessage=err.error;
      })
  }

}
