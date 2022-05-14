import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { RxStompService } from 'src/app/shared/rx-stomp/rx-stomp.service';
import { Offender } from './models/offender.model';
import { OffendersActions } from './store/action-types';
import {
  loadingOffenders,
  offendersList,
  offendersPagedList,
} from './store/offenders.selectors';
import { Message } from '@stomp/stompjs';
import { AddEditOffenderComponent } from './modals/add-edit-offender/add-edit-offender.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { filter, Observable } from 'rxjs';
import { OffendersService } from './services/offenders.service';

@Component({
  selector: 'app-offenders',
  templateUrl: './offenders.component.html',
  styleUrls: ['./offenders.component.scss'],
})
export class OffendersComponent implements OnInit {
  offenders$ = this.store.select(offendersList);
  loading$ = this.store.select(loadingOffenders);
  offendersPagedList$ = this.store.select(offendersPagedList);

  constructor(private store: Store, private rxStompService: RxStompService) {}

  ngOnInit() {
    this.store.dispatch(OffendersActions.getOffenders());
    this.rxStompService
      .watch('/ws-resp/greetings')
      .subscribe((message: Message) => {
        console.log(message.body);
      });

    // const message = `Message generated at ${new Date()}`;
    // this.rxStompService.publish({
    //   destination: '/app-ws/hello',
    //   body: message,
    // });
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
