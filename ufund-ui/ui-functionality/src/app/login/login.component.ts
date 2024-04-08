import { Component, OnInit } from '@angular/core';
import { AuthCredentials } from '../login';
import { LoginService } from '../login.service';
import { MessageService } from '../message.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  username: string = localStorage.getItem("username") ?? "";
  password: string = localStorage.getItem("password") ?? "";
  permissionLevel: string = localStorage.getItem("perms") ?? "";
  isLoggedIn: boolean = false;
  

  constructor(private loginService: LoginService, 
    private messageService: MessageService) 
    {
      this.isLoggedIn = window.localStorage.getItem("using") === "true";
     }

  ngOnInit(): void {}


  
  getPermissionLevel(): void { 
    this.loginService.getPermissionLevel(this.username, this.password).subscribe(
      permission => this.permissionLevel = permission
    );
  }


  login(): void {
    this.getPermissionLevel();
    this.loginService.authenticate(this.username, this.password).subscribe({
      next: response => {
        // Handle successful authentication
        this.messageService.add('Authentication successful');
        localStorage.setItem("username", this.username)
        localStorage.setItem("password", this.password)
        localStorage.setItem("perms", this.permissionLevel)
        localStorage.setItem("using", "true")
        this.isLoggedIn = true;
        this.loginService.emitNewLogoutEvent();
      },
      error: err => {
        // Handle authentication error
        this.messageService.add('Authentication failed');
      }
    });
  }

  logout(): void{
    localStorage.setItem("username", "")
    localStorage.setItem("password", "")
    localStorage.setItem("perms", "guest")
    localStorage.setItem("using", "false")
    this.messageService.add(`${this.username} has logged out successfully`);
    this.isLoggedIn = false;
    this.username = ''
    this.password = ''
    this.loginService.emitNewLogoutEvent();
  }
}
