import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/service/user.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-view-account',
  templateUrl: './view-account.component.html',
  styleUrls: ['./view-account.component.css']
})
export class ViewAccountComponent implements OnInit {

  updateForm:FormGroup
  username:string;
  usernameFlag:boolean=false;
  usernameErrorMessage:string;
  email:string;
  emailFlag:boolean=false;
  emailErrorMessage:string;
  phNo:string;
  phNoFlag:boolean=false;
  phNoErrorMessage:string;
  submitted:boolean=false;
   
  user:User  
  constructor(private formBuilder:FormBuilder,private service:UserService,private route:Router) { 

    if(localStorage.getItem("username")==null)
    {
      alert("Please login")
      this.route.navigate(['/home'])
    }
    this.updateForm=this.formBuilder.group({
      userid:[''],
      name: ['', [Validators.required,Validators.pattern("^[A-Z][a-zA-Z]{3,}(?: [A-Z][a-zA-Z]*){0,2}$")]],
      phNo: [''],
      email: [''],
      gender: [''],
      password: ['', [Validators.required,Validators.pattern("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}")]],
      username:[''],
      balance:[''] 
    })
    
  }
  
  ngOnInit() {
    this.service.getAccountDetails(localStorage.getItem("username")).subscribe(data=>
      {
        this.user=data
        this.updateForm.setValue(data)
      })
      this.updateForm.disable();
  }
  
 updateUser()
{
 this.submitted=true;
 if(this.updateForm.invalid)
 return;

    this.service.updateUser(this.updateForm.getRawValue()).subscribe(data=>
      {
        alert(`${this.updateForm.controls.name.value} + your account has been updated`);
        this.route.navigate(['/loggedin']);
      })
  }

enableFields()
{
  this.updateForm.controls.name.enable();
  this.updateForm.controls.password.enable();
}
}
