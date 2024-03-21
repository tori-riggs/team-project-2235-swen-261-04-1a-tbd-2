import { Component, OnInit } from '@angular/core';
import { AuthCredentials } from '../login';
import { LoginService } from '../login.service';
import { MessageService } from '../message.service';
import { SharedDataService } from '../shared-data.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  username: string = '';
  password: string = '';
  isLoggedIn: boolean = false;
  

  constructor(private loginService: LoginService, 
    private messageService: MessageService, private sharedDataService: SharedDataService) { }

  ngOnInit(): void {}

  
  getPermissionLevel(): string {
    let permissionLevel: string = ''; 
    this.loginService.getPermissionLevel(this.username, this.password)
    .subscribe(permission => permissionLevel = permission);
    console.log(`this is the perm level ${permissionLevel} thingy`);
    
    return permissionLevel;
  }


  login(): void {
    this.loginService.authenticate(this.username, this.password).subscribe({
      next: response => {
        // Handle successful authentication
        this.messageService.add('Authentication successful');
        this.isLoggedIn = true;
        this.sharedDataService.setUsername(this.username);
        this.sharedDataService.setPassword(this.password);

        this.sharedDataService.setPermissionLevel(this.getPermissionLevel());
      },
      error: err => {
        // Handle authentication error
        this.messageService.add('Authentication failed');
      }
    });
  }

  logout(): void{
    this.isLoggedIn = false;
    this.messageService.add(`${this.username} has logged out successfully`);
  }
}
