import { Component } from '@angular/core';
import { LoginService } from '../login.service';
import { MessageService } from '../message.service';
import { SharedDataService } from '../shared-data.service';

@Component({
  selector: 'app-cupboard',
  templateUrl: './cupboard.component.html',
  styleUrl: './cupboard.component.css'
})
export class CupboardComponent {
  username: string = this.sharedDataService.getUsername();
  
  constructor(private loginService: LoginService, 
    private messageService: MessageService,private sharedDataService:SharedDataService) { }

  
}
