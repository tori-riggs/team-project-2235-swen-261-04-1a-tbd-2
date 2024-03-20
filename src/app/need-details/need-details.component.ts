import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import {Need} from '../need';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-need-details',
  templateUrl: './need-details.component.html',
  styleUrl: './need-details.component.css'
  
})
export class NeedDetailsComponent implements OnInit {
  @Input() need?: Need;
  //need: Need | undefined;

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getNeed();
  }

  getNeed(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.needService.getNeedFromCupboard(id).subscribe(need => this.need = need);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.need) {
      this.needService.updateNeedInCupboard(this.need)
        .subscribe(() => this.goBack());
    }
  }
}
