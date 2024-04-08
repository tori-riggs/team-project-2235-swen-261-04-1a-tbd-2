import { Component } from '@angular/core';
import { LoginService } from '../login.service';
import { MessageService } from '../message.service';
import { NeedService } from '../need.service';
import { Need } from '../need';

@Component({
  selector: 'app-cupboard',
  templateUrl: './cupboard.component.html',
  styleUrl: './cupboard.component.css'
})
export class CupboardComponent {
  username: string = localStorage.getItem("username") ?? "";
  permissionLevel: string = localStorage.getItem("perms") ?? "";

  
  constructor(private loginService: LoginService, 
    private messageService: MessageService, private needService: NeedService) { }
    
    sorting(sortingOption: string) {
      localStorage.setItem("sort",sortingOption);
      this.needService.emitNewSortEvent();
    }
  
}
