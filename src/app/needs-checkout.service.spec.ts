import { TestBed } from '@angular/core/testing';

import { NeedsCheckoutService } from './needs-checkout.service';

describe('NeedsCheckoutService', () => {
  let service: NeedsCheckoutService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NeedsCheckoutService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
