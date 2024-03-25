import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedDetailsComponent } from './need-details.component';

describe('HeroDetailsComponent', () => {
  let component: NeedDetailsComponent;
  let fixture: ComponentFixture<NeedDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NeedDetailsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NeedDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
