import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-loginpage',
  templateUrl: './loginpage.component.html',
  styleUrls: ['./loginpage.component.css']
})
export class LoginpageComponent implements OnInit {

loginForm:FormGroup
submitted:boolean=false;
username:string;
password:string;
matchCredential:boolean=false;
errorMessage:string;
  constructor(private route:Router,private formBuilder:FormBuilder,private service:UserService) {
    this.loginForm=this.formBuilder.group({
      username:['',Validators.required],
      password:['',Validators.required]
    })
   }

  ngOnInit(): void {
    localStorage.clear();
  }
  login()
  {
    this.submitted=true;
    this.username=this.loginForm.controls.username.value
    this.password=this.loginForm.controls.password.value
    if(this.loginForm.invalid)
    return;
    if(this.username.toLowerCase()=="admin")
    {
      this.service.authorise(this.username,this.password).subscribe(data=>
        {
          if(data=="true")
          {
            localStorage.setItem("username","admin")
            this.route.navigate(['/admin'])
          }
        },(err:HttpErrorResponse)=>
        {
            this.matchCredential=true;
            this.errorMessage=err.error;
        })
    }
    else{
      this.service.authorise(this.username,this.password).subscribe(data=>
        {
          if(data=="true")
          {
            localStorage.setItem("username",this.username)
            this.route.navigate(['/loggedin'])
           
          }     
        },(err:HttpErrorResponse)=>
        {
          this.matchCredential=true;
            this.errorMessage=err.error;
        })

    }
    
  }

}
