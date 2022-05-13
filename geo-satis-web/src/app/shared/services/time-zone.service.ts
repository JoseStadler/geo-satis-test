import { Injectable } from '@angular/core';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root',
})
export class TimeZoneService {
  readonly timeZones: string[] = moment.tz.names();
  private _selectedTimeZone: string = moment.tz.guess();

  constructor() {}

  public set selectedTimeZone(selectedTimeZone: string) {
    this._selectedTimeZone = selectedTimeZone;
  }

  public get selectedTimeZone(): string {
    return this._selectedTimeZone;
  }
}
