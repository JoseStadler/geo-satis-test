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
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-offenders',
  templateUrl: './offenders.component.html',
  styleUrls: ['./offenders.component.scss'],
})
export class OffendersComponent implements OnInit {
  offenders$ = this.store.select(offendersList);
  loading$ = this.store.select(loadingOffenders);
  offendersPagedList$ = this.store.select(offendersPagedList);

  constructor(
    private store: Store,
    private rxStompService: RxStompService,
    public dialog: MatDialog
  ) {}

  ngOnInit() {
    this.store.dispatch(OffendersActions.getOffenders());
    this.rxStompService
      .watch('/ws-resp/greetings')
      .subscribe((message: Message) => {
        console.log(message.body);
      });
  }

  addOffender() {
    const dialogRef = this.dialog.open(AddEditOffenderComponent, {
      width: '250px',
      // data: {name: this.name, animal: this.animal},
    });

    dialogRef.afterClosed().subscribe((result) => {
      // console.log('The dialog was closed');
      // this.animal = result;
    });

    // const message = `Message generated at ${new Date()}`;
    // this.rxStompService.publish({
    //   destination: '/app-ws/hello',
    //   body: message,
    // });
  }

  editOffender(offender: Offender) {}

  pageChanged(page: number) {
    this.store.dispatch(OffendersActions.changeOffendersPage({ page }));
  }
}
