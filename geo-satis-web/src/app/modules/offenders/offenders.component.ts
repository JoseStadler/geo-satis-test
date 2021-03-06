import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Offender } from './models/offender.model';
import { OffendersActions } from './store/action-types';
import {
  loadingOffenders,
  offendersList,
  offendersPagedList,
} from './store/offenders.selectors';

@Component({
  selector: 'app-offenders',
  templateUrl: './offenders.component.html',
  styleUrls: ['./offenders.component.scss'],
})
export class OffendersComponent implements OnInit {
  offenders$ = this.store.select(offendersList);
  loading$ = this.store.select(loadingOffenders);
  offendersPagedList$ = this.store.select(offendersPagedList);

  constructor(private store: Store) {}

  ngOnInit() {
    this.store.dispatch(OffendersActions.getOffenders());
  }

  addOffender() {
    this.store.dispatch(OffendersActions.saveNewOffender());
  }

  editOffender(offender: Offender) {
    this.store.dispatch(OffendersActions.updateOffender(offender));
  }

  pageChanged(page: number) {
    this.store.dispatch(OffendersActions.changeOffendersPage({ page }));
  }
}
