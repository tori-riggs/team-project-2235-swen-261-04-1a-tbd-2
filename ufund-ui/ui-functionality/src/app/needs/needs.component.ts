import { Component, Input, OnInit } from '@angular/core';
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
  needCheckout?: NeedCheckout;
  checkoutIDs?: number[];
  selectedNeed?: Need;
  username: string = localStorage.getItem("username") ?? "";
  password: string = localStorage.getItem("password") ?? "";
  permissionLevel: string = localStorage.getItem("perms") ?? "";
  editing: boolean = false;
  creating: boolean = false;
  term?: string = "";
  emptyCuboard: boolean = false;
  
  constructor(private needCheckoutService: NeedsCheckoutService, private needService: NeedService, 
    private messageService: MessageService) { }


  onSelect(need: Need): void {
      this.selectedNeed = need;
      this.messageService.add(`NeedsComponent: Selected need id=${need.id}`);
  }

  sorting(sortingOption: string){
    console.log(`${sortingOption}`)
      this.needService.sortNeeds(sortingOption).subscribe(needs => {
        this.needs = needs
        console.log(needs[0]);
      })

      
  }

  getNeeds(): void { 
    this.term = localStorage.getItem("search") ?? "";
    if(this.term != null && this.term != ""){
      console.log(`${this.term}`)
      this.needService.findMatchingNeedsFromCupboard(this.term)
      .subscribe(needs => {
        this.needs = needs
        this.emptyCuboard = this.needs.length == 0;
      });
    }
    else{
      this.needService.getNeedsFromCupboard()
      .subscribe(needs => {
        this.needs = needs
        this.emptyCuboard = this.needs.length == 0;
      });
    }
    this.messageService.add(`permlevel=${this.permissionLevel}`);
  }

  ngOnInit(): void {
      localStorage.setItem("search","")
      this.getNeeds();

      this.needService.newSearchEvent.subscribe(() => {
        this.getNeeds()
      })

      this.needService.newSortEvent.subscribe(
        () => {
          var sort = localStorage.getItem("sort")
          if(sort){
            this.sorting(sort)
          }
          
        }
      )
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
    if (isNaN(parseInt(cost)) || isNaN(parseInt(quantity)) || !name || !description || cost <=0 || quantity <= 0) { return; }
    this.needService.createNeedInCupboard({ name, cost, quantity, description } as Need, this.username, this.password)
      .subscribe(need => {
        this.needs.push(need);
        this.emptyCuboard = false;
      });
  }

  addToCart(need: Need){
    console.log(`${need.id}`)
    this.needCheckoutService.addNeedToFundingBasket(this.username, this.password, need.id, 1).subscribe(
      checkout => {
        this.needCheckout = checkout; // Update needCheckout after adding
        this.needCheckoutService.emitAddToCartEvent();
      });
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(n => n !== need);
    this.needService.deleteNeedFromCupboard(need.id, this.username, this.password).subscribe(
      ()=>{this.needService.emitNewSearchEvent();})
  }
}
