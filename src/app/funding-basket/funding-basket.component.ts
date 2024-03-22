import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { MessageService } from '../message.service';
import { interval } from 'rxjs';

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
  checking: Need[] = [];
  selectedNeed?: Need;
  username: string = localStorage.getItem("username") ?? "";
  password: string = localStorage.getItem("password") ?? "";
  permissionLevel: string = localStorage.getItem("perms") ?? "";
  needCheckout?: NeedCheckout;
  checkoutIDs?: number[];
  checkingOut: boolean = false;
  
  constructor(private needCheckoutService: NeedsCheckoutService, private needService: NeedService, 
    private messageService: MessageService) { }


  onSelect(need: Need): void {
      this.selectedNeed = need;
      this.messageService.add(`NeedsComponent: Selected need id=${need.id}`);
  }

  getNeeds(): void {
    if (!this.needCheckout || !this.checkoutIDs) {
      return;
    }
    this.needs = []
    console.log(`${this.checkoutIDs.length}`)
    for (const id of this.checkoutIDs) {
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
    this.needCheckoutService.getFundingBasket(this.username, this.password)
      .subscribe(needCheckout => {
        this.needCheckout = needCheckout;
        this.checkoutIDs = needCheckout?.checkoutIds;
        this.getNeeds();
      });
  }

  remove(need: Need): void {
    //this.needs = this.needs.filter(n => n !== need);
    this.needCheckoutService.removeNeedFromFundingBasket(this.username,need.id,this.password).subscribe(
      needCheckout => {
        this.needCheckout = needCheckout; // Update needCheckout after removing
        this.checkoutIDs = needCheckout?.checkoutIds; 
        this.getFundingBasket()
      }
    )
  }

  checkout(): void {
    this.checkingOut = true;
    this.getNeeds()
    this.checking = this.needs
    if(!this.checkoutIDs){return;}
    this.getFundingBasket()
    this.checkoutIDs.forEach(id => {
      this.needService.getNeedFromCupboard(id).subscribe((need: Need) => {
        const newNeed = {
          id: need.id,
          name: need.name,
          cost: need.cost,
          quantity: --need.quantity,
          description: need.description
        };
        this.remove(newNeed)
      });
    });
    this.checkingOut = false;
  }

  ngOnInit(): void {
    this.getFundingBasket(); 
    
    this.needCheckoutService.addToCartEvent.subscribe(() => {
      // Refresh the funding basket when addToCart event is emitted
      this.getFundingBasket();
    });
  }
}
