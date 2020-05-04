import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import{ FormsModule, ReactiveFormsModule } from '@angular/forms'
import { HttpClientModule } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
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
import { SearchPipe } from './pipe/search.pipe';
import { ServicesComponent } from './component/services/services.component';


@NgModule({
  declarations: [
    AppComponent,
    MainpageComponent,
    ContactComponent,
    HomepageComponent,
    LoginpageComponent,
    SignupComponent,
    LoggedinComponent,
    DepositMoneyComponent,
    FundTransferComponent,
    ShowBalanceComponent,
    TransactionsComponent,
    WithdrawMoneyComponent,
    ViewAccountComponent,
    AdminComponent,
    SearchPipe,
    ServicesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
