import { Component } from '@angular/core';
import { interval, map, Observable, startWith } from 'rxjs';
import { BRAND_IMAGE } from '../../base.constants';
import { TimeZoneService } from '../../services/time-zone.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  readonly brandImage = BRAND_IMAGE;
  today$: Observable<Date> = this.initializeTimer();

  constructor(public timeZoneService: TimeZoneService) {}

  initializeTimer(): Observable<Date> {
    return interval(1000).pipe(
      startWith(new Date()),
      map(() => new Date())
    );
  }
}
