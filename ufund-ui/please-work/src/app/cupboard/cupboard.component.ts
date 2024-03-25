import { Component } from '@angular/core';
import { LoginService } from '../login.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-cupboard',
  templateUrl: './cupboard.component.html',
  styleUrl: './cupboard.component.css'
})
export class CupboardComponent {
  username: string = localStorage.getItem("username") ?? "";
  permissionLevel: string = localStorage.getItem("perms") ?? "";
  
  constructor(private loginService: LoginService, 
    private messageService: MessageService) { }

  
}
