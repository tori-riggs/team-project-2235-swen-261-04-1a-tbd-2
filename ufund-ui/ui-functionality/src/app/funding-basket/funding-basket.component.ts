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
  checkingOut: boolean = false;
  checkoutIDs: { [key: number]: number } = {};
  totalPrice: number = 0;
  
  constructor(private needCheckoutService: NeedsCheckoutService, private needService: NeedService, 
    private messageService: MessageService) { }


  onSelect(need: Need): void {
      this.selectedNeed = need;
      this.messageService.add(`NeedsComponent: Selected need id=${need.id}`);
  }

  getNeeds(): void {
    if(!this.checkoutIDs){return;}
    this.needs = []
    this.totalPrice = 0;
    for(let idStr in this.checkoutIDs){
      let id = parseInt(idStr);
      if (!isNaN(id)) {
        this.needService.getNeedFromCupboard(id)
          .subscribe(need => {
            need.quantity = this.checkoutIDs[id]
            this.totalPrice += (need.quantity*need.cost)
            this.needs.push(need);
          });
      }

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
    this.checking = this.needs
    this.needCheckoutService.checkout(this.username, this.password).subscribe(
      needCheckout =>{
        this.needCheckout = needCheckout; // Update needCheckout after removing
        this.checkoutIDs = needCheckout?.checkoutIds; 
        this.needService.emitNewSearchEvent();
        this.getFundingBasket();
      }
    )

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
