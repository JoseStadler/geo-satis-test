import { Pipe, PipeTransform } from '@angular/core';
import { DATE_TIME_FORMAT } from '../base.constants';
import { TimeZoneService } from '../services/time-zone.service';
import * as moment from 'moment-timezone';

@Pipe({
  name: 'dateTime',
})
export class DateTimePipe implements PipeTransform {
  constructor(private timeZoneService: TimeZoneService) {}

  transform(value: Date): string {
    return moment(value.getTime())
      .tz(this.timeZoneService.selectedTimeZone)
      .format(DATE_TIME_FORMAT);
  }
}
