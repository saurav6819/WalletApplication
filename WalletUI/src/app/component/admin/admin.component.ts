import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  searchText:any;
  constructor(private service:UserService,private route:Router) {
    if(localStorage.getItem("username")!="admin"){
      alert('Please login ')
    route.navigate(['/main/login'])

    }
   }
  view=true
  users:any;
  ngOnInit() {

    this.service.getAllUsers().subscribe(data=>
      {
        this.users=data;
      })

  }
  
  toggleViewList(){
    this.view=false;
    
    
  }
  logout()
  {
    localStorage.clear()
    this.route.navigate(['/']);
  }
  deleteUser(userid:number)
  {
    this.service.deleteUser(userid).subscribe(data=>
      {
        this.users=data;
      })
  }
  toggleViewCard()
  {
    this.view=true;
    
  }
  
}
