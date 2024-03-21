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
import { NeedCheckout } from '../needs-checkout';
import { NeedsCheckoutService } from '../needs-checkout.service';

@Component({
  selector: 'app-funding-basket',
  templateUrl: './funding-basket.component.html',
  styleUrl: './funding-basket.component.css'
})
export class FundingBasketComponent implements OnInit {
  needs: Need[] = [];
  needCheckout: NeedCheckout[] = [];
  selectedFunding?: Need;
  username: string = this.sharedDataService.getUsername();
  password: string = this.sharedDataService.getPassword();
  permissionLevel: string = this.sharedDataService.getPermissionLevel();
  
  constructor(private needCheckoutService: NeedsCheckoutService, private needService: NeedService, 
    private messageService: MessageService, private sharedDataService: SharedDataService) { }


  onSelect(need: Need): void {
      this.selectedFunding = need;
      this.messageService.add(`NeedsComponent: Selected need id=${need.id}`);
  }

  /** 
  getFundingBasket(): void {
    this.needCheckoutService.getFundingBasket(this.username)
      .subscribe(needCheckout => this.needCheckout = needCheckout);
  }
  */

  getFundingBasket(): void {
    this.needCheckoutService.getFundingBasket(this.username,this.password)
      .subscribe(needCheckout => this.needCheckout.push(needCheckout)); // Push received NeedCheckout into the array
  }

  ngOnInit(): void {
    this.getFundingBasket();
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
        this.needCheckout.push(updatedNeedCheckout);
      });
  }
}
