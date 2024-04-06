import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NeedsComponent } from './needs/needs.component';
import { FormsModule } from '@angular/forms';
import { UpperCasePipe } from '@angular/common';
import { NeedDetailsComponent } from './need-details/need-details.component';
import { MessagesComponent } from './messages/messages.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HttpClientModule } from '@angular/common/http';
import { NeedSearchComponent } from './need-search/need-search.component';
import { LoginComponent } from './login/login.component';
import { CupboardComponent } from './cupboard/cupboard.component';
import { FundingBasketComponent } from './funding-basket/funding-basket.component';
import { SupportComponent } from './support/support.component';
import { InboxComponent } from './inbox/inbox.component';

@NgModule({
  declarations: [
    AppComponent,
    NeedsComponent,
    NeedDetailsComponent,
    MessagesComponent,
    DashboardComponent,
    NeedSearchComponent,
    LoginComponent,
    CupboardComponent,
    FundingBasketComponent,
    SupportComponent,
    InboxComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    UpperCasePipe,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
