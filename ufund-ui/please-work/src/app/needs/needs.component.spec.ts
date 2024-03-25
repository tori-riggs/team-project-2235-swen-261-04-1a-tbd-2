import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedsComponent } from './needs.component';

describe('NeedsComponent', () => {
  let component: NeedsComponent;
  let fixture: ComponentFixture<NeedsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NeedsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NeedsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
