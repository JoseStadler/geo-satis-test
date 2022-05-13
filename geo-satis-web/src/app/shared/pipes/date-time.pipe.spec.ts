/* tslint:disable:no-unused-variable */

import { TestBed, async } from '@angular/core/testing';
import { TimeZoneService } from '../services/time-zone.service';
import { DateTimePipe } from './date-time.pipe';

describe('Pipe: DateTime', () => {
  it('create an instance', () => {
    let pipe = new DateTimePipe(new TimeZoneService());
    expect(pipe).toBeTruthy();
  });

  it('should format the date to MM/DD/YYYY HH:mm:ss', () => {
    let pipe = new DateTimePipe(new TimeZoneService());

    const formattedDate = pipe.transform(new Date());
    expect(formattedDate)
      .withContext('Date was not formatted correctly')
      .toMatch(
        /(?:0[1-9]|1[012])\/(?:0[1-9]|[12][0-9]|3[01])\/(?:\d{4})\s(2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])\b/
      );
  });
});
