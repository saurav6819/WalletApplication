import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  addForm: FormGroup
  submitted: boolean = false;
  username: string
  usernameErrorMessage: string;
  usernameFlag: boolean = false;
  phNo: string;
  phNoErrorMessage: string;
  phNoFlag: boolean = false;
  email: string;
  emailFlag: boolean = false;
  emailErrorMessage: string;
  constructor(private formBuilder: FormBuilder, private service: UserService, private route: Router) {
    this.addForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.pattern("^[A-Z][a-zA-Z]{3,}(?: [A-Z][a-zA-Z]*){0,2}$")]],
      phNo: ['', [Validators.required, Validators.pattern("[6-9][0-9]{9}")]],
      email: ['', [Validators.required, Validators.email]],
      gender: ['', [Validators.required, Validators.pattern("[A-Z][a-z]{2,6}")]],
      password: ['', [Validators.required, Validators.pattern("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}")]],
      username: ['', [Validators.required,Validators.pattern("\\b[a-zA-Z][a-zA-Z0-9\\-._]{5,}\\b")]]
    })

  }

  ngOnInit(): void {
    localStorage.clear()
  }

  addUser() {
    this.submitted = true;
    if (this.addForm.invalid)
      return;
    this.username = this.addForm.controls.username.value
    this.email = this.addForm.controls.email.value
    this.phNo = this.addForm.controls.phNo.value
    this.service.checkValidUsername(this.username).subscribe(data => {

      this.usernameFlag = false
    }, (err: HttpErrorResponse) => {
      this.usernameFlag = true;
      this.usernameErrorMessage = err.error
    })
    this.service.checkValidEmail(this.email).subscribe(data => {

      this.emailFlag = false
    }, (err: HttpErrorResponse) => {
      this.emailFlag = true;
      this.emailErrorMessage = err.error
    })

    this.service.checkValidPhoneNumber(this.phNo).subscribe(data => {
      if (data == false) {
        this.phNoFlag = false
        if (this.emailFlag == false && this.usernameFlag == false) {
          this.service.addAccount(this.addForm.value).subscribe(data => {
            alert(`${this.addForm.controls.name.value}` + " Your account has been added ")
            localStorage.setItem("username", this.username)
            this.route.navigate(['/loggedin'])
          })
        }
      }
    }, (err: HttpErrorResponse) => {
      this.phNoFlag = true;
      this.phNoErrorMessage = err.error
    })
  }
}
