import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { MessageService } from '../message.service';

import {
  /* . . . */
  NgFor,
  /* . . . */
} from '@angular/common';
import { NeedCheckout } from '../needs-checkout';
import { NeedsCheckoutService } from '../needs-checkout.service';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrl: './needs.component.css'
})
export class NeedsComponent implements OnInit {
  needs: Need[] = [];
  needCheckout: NeedCheckout[] = [];
  selectedNeed?: Need;
  username: string = localStorage.getItem("username") ?? "";
  password: string = localStorage.getItem("password") ?? "";
  permissionLevel: string = localStorage.getItem("perms") ?? "";
  editing: boolean = false;
  creating: boolean = false;
  
  constructor(private needCheckoutService: NeedsCheckoutService, private needService: NeedService, 
    private messageService: MessageService) { }


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
      this.needService.updateNeedInCupboard(need, this.username, this.password)
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

  add(name: string, cost: any, quantity: any, description: string): void {
    name = name.trim();
    description = description.trim();
    //
    if (isNaN(parseInt(cost)) || isNaN(parseInt(quantity)) || !name || !description ) { return; }
    this.needService.createNeedInCupboard({ name, cost, quantity, description } as Need, this.username, this.password)
      .subscribe(need => {
        this.needs.push(need);
      });
  }

  addToCart(id: number){
    this.needCheckoutService.addNeedToFundingBasket(this.username, id, this.password).subscribe(
      needCheckout => {
        // Construct a new NeedCheckout object with updated checkoutIds
        const updatedNeedCheckout: NeedCheckout = {
          username: this.username,
          checkoutIDs: [...needCheckout?.checkoutIDs ?? [], id]
        };
  
        // Push the updated NeedCheckout object into the needCheckout array
        this.needCheckout.push(updatedNeedCheckout);
      });
      console.log("worked?");
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(n => n !== need);
    this.needService.deleteNeedFromCupboard(need.id, this.username, this.password).subscribe();
  }
}
