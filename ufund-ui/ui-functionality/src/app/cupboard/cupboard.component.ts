import { Component } from '@angular/core';
import { LoginService } from '../login.service';
import { MessageService } from '../message.service';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-cupboard',
  templateUrl: './cupboard.component.html',
  styleUrl: './cupboard.component.css'
})
export class CupboardComponent {
  username: string = localStorage.getItem("username") ?? "";
  permissionLevel: string = localStorage.getItem("perms") ?? "";
  selectedSort ?: string;
  
  constructor(private loginService: LoginService, 
    private messageService: MessageService, private needService: NeedService) { }
  
    onSelect(sort: string){
      console.log(sort)
      this.selectedSort = sort;
    }
    
    sorting(sortingOption: string){
        this.needService.sortNeeds(sortingOption).subscribe(() => {
          this.needService.emitNewSearchEvent();
        })
    }
  
}
