/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import * as moment from 'moment';
import { TimeZoneService } from './time-zone.service';

describe('Service: TimeZone', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TimeZoneService],
    });
  });

  it('should guess the computer time zone', inject(
    [TimeZoneService],
    (service: TimeZoneService) => {
      const timeZone = service.selectedTimeZone;

      expect(timeZone)
        .withContext('The initial timezone was not guessed')
        .toBe(moment.tz.guess());
    }
  ));

  it('should have the list of available time zones', inject(
    [TimeZoneService],
    (service: TimeZoneService) => {
      const timeZones = service.timeZones;

      expect(timeZones)
        .withContext('The timezones are not initialized')
        .toBeTruthy();
      expect(timeZones.length)
        .withContext('The timezones have no data')
        .toBeGreaterThan(0);
    }
  ));

  it('should update the time zone', inject(
    [TimeZoneService],
    (service: TimeZoneService) => {
      const timeZone = service.timeZones[0];
      service.selectedTimeZone = timeZone;

      expect(service.selectedTimeZone)
        .withContext('The timezone was not updated')
        .toBe(timeZone);
    }
  ));
});
