import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-show-balance',
  templateUrl: './show-balance.component.html',
  styleUrls: ['./show-balance.component.css']
})
export class ShowBalanceComponent implements OnInit {

  currBalance;
  constructor(private service:UserService) {
    this.service.showBalance(localStorage.getItem("username")).subscribe(data=>
    {
      this.currBalance=data;
    })
   }

  ngOnInit(): void {
  }

}
