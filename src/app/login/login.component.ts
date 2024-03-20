import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor() {}

  login() {
    // Here you can access this.username and this.password
    // and perform authentication or any other actions
    console.log('Username:', this.username);
    console.log('Password:', this.password);
  }
}
