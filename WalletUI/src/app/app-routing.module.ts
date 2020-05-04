import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainpageComponent } from './component/mainpage/mainpage.component';
import { ContactComponent } from './component/contact/contact.component';
import { HomepageComponent } from './component/homepage/homepage.component';
import { LoginpageComponent } from './component/loginpage/loginpage.component';
import { SignupComponent } from './component/signup/signup.component';
import { LoggedinComponent } from './component/login/loggedin/loggedin.component';
import { DepositMoneyComponent } from './component/login/deposit-money/deposit-money.component';
import { FundTransferComponent } from './component/login/fund-transfer/fund-transfer.component';
import { ShowBalanceComponent } from './component/login/show-balance/show-balance.component';
import { TransactionsComponent } from './component/login/transactions/transactions.component';
import { WithdrawMoneyComponent } from './component/login/withdraw-money/withdraw-money.component';
import { ViewAccountComponent } from './component/login/view-account/view-account.component';
import { AdminComponent } from './component/admin/admin.component';
import { ServicesComponent } from './component/services/services.component';


const routes: Routes = [
  {path:'main',component:MainpageComponent,
  children:[
    {path:'home',component:HomepageComponent},
    {path:'services',component:ServicesComponent},
    {path:'contact',component:ContactComponent},
    {path:'login',component:LoginpageComponent},
    {path:'signup',component:SignupComponent}
  ]},
  {path:'',redirectTo:'main/home',pathMatch:'full'},
  {path:'loggedin',component:LoggedinComponent,
  children:[
    {path:'deposit',component:DepositMoneyComponent},
      {path:'fundTransfer',component:FundTransferComponent},
      {path:'showBalance',component:ShowBalanceComponent},
      {path:'transaction',component:TransactionsComponent},
      {path:'withdraw',component:WithdrawMoneyComponent}
  ]},{path:'view',component:ViewAccountComponent},
  {path:'admin',component:AdminComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
