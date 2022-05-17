/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { OffendersService } from './offenders.service';

xdescribe('Service: Offenders', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OffendersService],
    });
  });

  it('should ...', inject([OffendersService], (service: OffendersService) => {
    expect(service).toBeTruthy();
  }));
});
