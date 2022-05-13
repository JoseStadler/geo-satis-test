import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Offender } from './models/offender.model';
import { OffendersActions } from './store/action-types';
import { loadingOffenders, offendersList } from './store/offenders.selectors';

@Component({
  selector: 'app-offenders',
  templateUrl: './offenders.component.html',
  styleUrls: ['./offenders.component.scss'],
})
export class OffendersComponent implements OnInit {
  offenders$ = this.store.select(offendersList);
  loading$ = this.store.select(loadingOffenders);

  constructor(private store: Store) {}

  ngOnInit() {
    this.store.dispatch(OffendersActions.getOffenders());
  }

  addOffender() {}

  editOffender(offender: Offender) {}
}
