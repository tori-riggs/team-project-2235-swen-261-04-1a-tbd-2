import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { MessageService } from '../message.service';
import { SharedDataService } from '../shared-data.service';

import {
  /* . . . */
  NgFor,
  /* . . . */
} from '@angular/common';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrl: './needs.component.css'
})
export class NeedsComponent implements OnInit {
  needs: Need[] = [];
  selectedNeed?: Need;
  username: string = this.sharedDataService.getUsername();
  permissionLevel: string = this.sharedDataService.getPermissionLevel();
  editing: boolean = false;
  creating: boolean = false;
  
  constructor(private needService: NeedService, 
    private messageService: MessageService, private sharedDataService: SharedDataService) { }

  onSelect(need: Need): void {
      this.selectedNeed = need;
      this.messageService.add(`NeedsComponent: Selected need id=${need.id}`);
  }

  getNeeds(): void {
    this.needService.getNeedsFromCupboard()
      .subscribe(needs => this.needs = needs);
      this.messageService.add(`permlevel=${this.permissionLevel}`);
  }

  ngOnInit(): void {
    this.getNeeds();
  }
  
  update(need: Need): void {
    if (need) {
      this.needService.updateNeedInCupboard(need)
        .subscribe();
    }
    this.editing = false;
  }

  beginEdit(need: Need){
    this.editing = true;
    this.selectedNeed = need;
  }

  beginCreation(){
    this.creating = true;
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.needService.createNeedInCupboard({ name } as Need)
      .subscribe(need => {
        this.needs.push(need);
      });
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(n => n !== need);
    this.needService.deleteNeedFromCupboard(need.id).subscribe();
  }
}
