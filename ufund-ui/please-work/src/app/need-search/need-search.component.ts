import { Component, OnInit } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import {
  debounceTime, distinctUntilChanged, filter, switchMap
} from 'rxjs/operators';

import { Need } from '../need';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-need-search',
  templateUrl: './need-search.component.html',
  styleUrl: './need-search.component.css'
})
export class NeedSearchComponent {
  needs$!: Observable<Need[]>;
  private searchTerms = new Subject<string>();

  constructor(private needService: NeedService) {}

  search(term: string): void {
    localStorage.setItem("search",term);
    this.searchTerms.next(term);
    this.needService.emitNewSearchEvent();
  }

  ngOnInit(): void {
    this.needs$ = this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      filter((term: string) => !!term), // get rid of annoying backspace "" terms, where everything is a var
      switchMap((term: string) => 
        this.needService.findMatchingNeedsFromCupboard(term)),
    );
  }
}
