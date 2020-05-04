import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-deposit-money',
  templateUrl: './deposit-money.component.html',
  styleUrls: ['./deposit-money.component.css']
})
export class DepositMoneyComponent implements OnInit {
  depositMoneyForm:FormGroup
  upiForm:FormGroup
  submitted:boolean=false;
  errorFlag:boolean=false;
  errorMessage:string;
  constructor(private formBuilder:FormBuilder,private service:UserService,private route:Router) {
    this.upiForm=this.formBuilder.group({
      upiId:['',[Validators.required]]
    })
    this.depositMoneyForm=this.formBuilder.group({
      money:['',Validators.required],
      name:['',Validators.required],
      cardNumber:['',[Validators.required,Validators.pattern('[2-6][0-9]{15}')]],
      monthYear:['',Validators.required],
      cvv:['',[Validators.required,Validators.minLength(3)]],
      pin:['',[Validators.required,Validators.minLength(4)]]
    })
   }
  paymentVia:boolean=true;
  ngOnInit(): void {
  }

  deposit()
  {
    this.submitted=true;
    if(this.paymentVia==false)
    {
      if(this.depositMoneyForm.controls.money.invalid || this.upiForm.invalid)
      return;
    }
    else
    {
      if(this.depositMoneyForm.invalid)
      return;
    }
    
      this.service.depositMoney(localStorage.getItem("username"),this.depositMoneyForm.controls.money.value).subscribe(data=>
        {
          alert("Money Deposited")
          this.route.navigate(['/loggedin/showBalance'])
        },(err:HttpErrorResponse)=>
        {
          this.errorFlag=true;
          this.errorMessage=err.error;
        })
  
    
  }
  togglePaymentMethod(payment:boolean)
  {
    this.paymentVia=payment
  }
}
