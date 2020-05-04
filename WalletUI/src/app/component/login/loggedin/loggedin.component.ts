import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-loggedin',
  templateUrl: './loggedin.component.html',
  styleUrls: ['./loggedin.component.css']
})
export class LoggedinComponent implements OnInit {

  user:User;
  myDate=new Date();
  hr=this.myDate.getHours();
  
  greet;
   todaysDate = new Date();
   private showDefaultMessage = true; 
  constructor(private service:UserService,private route:Router) {
    setInterval(()=>{
      this.todaysDate = new Date();
    },1000);
    this.service.getAccountDetails(localStorage.getItem("username")).subscribe(data=>
      {
        this.user=data
        console.log(data)
      })
      
    if(this.hr<12)
      this.greet="Good Morning ";
   else if (this.hr >= 12 && this.hr <= 17)
     this.greet = 'Good Afternoon ' 
  else if (this.hr >= 17 && this.hr <= 24)
     this.greet = 'Good Evening '
  

   }

  ngOnInit(): void {
    if (localStorage.getItem("username") === null) {
      alert("Login Please")
      this.route.navigate(['/']);
  }
}
logout()
  {
    localStorage.clear();
    this.route.navigate(["/"]);
  }
  toggleDefaultMessage(state: boolean) {
    this.showDefaultMessage = state;
}
}
