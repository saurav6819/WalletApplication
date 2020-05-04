import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-withdraw-money',
  templateUrl: './withdraw-money.component.html',
  styleUrls: ['./withdraw-money.component.css']
})
export class WithdrawMoneyComponent implements OnInit {
  errorFlag:boolean=false;
  submitted:boolean=false;
  errorMessage:string;
  constructor(private service:UserService,private formBuilder:FormBuilder,private route:Router) { }
  debitMoneyForm:FormGroup
  currentMoney;
  ngOnInit() {
    this.debitMoneyForm=this.formBuilder.group({
      money:['',Validators.required]
    })
  }
  withdraw()
  {
    this.submitted=true;
    if(this.debitMoneyForm.invalid)
    return;
    this.service.withdrawMoney(localStorage.getItem("username"),this.debitMoneyForm.controls.money.value).subscribe(data=>
      {
        alert("Money Withdrawn");
        this.route.navigate(['/loggedin/showBalance'])
      },(err:HttpErrorResponse)=>
      {
        this.errorFlag=true;
        this.errorMessage=err.error;
      })
  }
}
