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
  selector: 'app-funding-basket',
  templateUrl: './funding-basket.component.html',
  styleUrl: './funding-basket.component.css'
})
export class FundingBasketComponent implements OnInit {
  needs: Need[] = [];
  needCheckouts?: NeedCheckout;
  selectedFunding?: Need;
  username: string = localStorage.getItem("username") ?? "";
  password: string = localStorage.getItem("password") ?? "";
  permissionLevel: string = localStorage.getItem("perms") ?? "";
  
  constructor(private needCheckoutService: NeedsCheckoutService, private needService: NeedService, 
    private messageService: MessageService) { }


  onSelect(need: Need): void {
      this.selectedFunding = need;
      this.messageService.add(`NeedsComponent: Selected need id=${need.id}`);
  }

  getNeeds(): void {
    if (!this.needCheckouts) {return;}
    for (const id of this.needCheckouts.checkoutIDs) {
      this.needService.getNeedFromCupboard(id).subscribe((need: Need) => {
        const newNeed = {
          id: need.id,
          name: need.name,
          cost: need.cost,
          quantity: need.quantity,
          description: need.description
        };
        
        this.needs.push(newNeed);
      });
    }
  }

  getFundingBasket(): void {
    this.needCheckoutService.getFundingBasket(this.username,this.password)
      .subscribe(needCheckout => this.needCheckouts = needCheckout,); // Push received NeedCheckout into the array
  }

  ngOnInit(): void {
    this.getFundingBasket();
    this.getNeeds();
  }

  addToCart(id: number){
    this.needCheckoutService.addNeedToFundingBasket(this.username, id, this.password).subscribe(
      needCheckout => {
        // Construct a new NeedCheckout object with updated checkoutIds
        const updatedNeedCheckout: NeedCheckout = {
          username: this.username,
          checkoutIDs: [...needCheckout.checkoutIDs, id] 
        };
  
        // Push the updated NeedCheckout object into the needCheckout array
        this.needCheckouts = updatedNeedCheckout;
      });
  }
}
