import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { OffendersActions } from './action-types';
import { delay, filter, first, map, switchMap, tap } from 'rxjs/operators';
import { forkJoin, Observable, of } from 'rxjs';
import { Store } from '@ngrx/store';
import { offendersCurrentPage, offendersPageSize } from './offenders.selectors';
import { RxStompService } from 'src/app/shared/rx-stomp/rx-stomp.service';
import { OffendersService } from '../services/offenders.service';
import { MatDialog } from '@angular/material/dialog';
import { AddEditOffenderComponent } from '../modals/add-edit-offender/add-edit-offender.component';
import { Offender } from '../models/offender.model';
import { OffenderModalResult } from '../models/offender-modal-result.model';

@Injectable()
export class OffendersEffects {
  getOffenders$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OffendersActions.getOffenders),
      tap(() => this.startLoader()),
      switchMap(() => this.getCurrentPageAndPageSize()),
      switchMap(([currentPage, size]) =>
        this.offendersService.findOffendersPaged(currentPage, size)
      ),
      map((offendersList) =>
        OffendersActions.getOffendersFinished(offendersList)
      ),
      tap(() => this.finishLoader())
    )
  );

  pageChanged$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OffendersActions.changeOffendersPage),
      map(() => OffendersActions.getOffenders())
    )
  );

  saveNewOffender$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OffendersActions.saveNewOffender),
      switchMap(() => this.openAddEditOffenderModal()),
      switchMap((modalResultData) =>
        this.offendersService.saveOffender(
          modalResultData.offender,
          modalResultData.picture
        )
      ),
      map(() => OffendersActions.getOffenders())
    )
  );

  updateOffender$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OffendersActions.updateOffender),
      switchMap((action) =>
        this.openAddEditOffenderModal(action).pipe(
          map((modalResultData) => {
            return {
              ...modalResultData,
              offender: {
                ...modalResultData.offender,
                id: action.id,
              },
            };
          })
        )
      ),
      switchMap((modalResultData) =>
        this.offendersService.updateOffender(
          modalResultData.offender.id,
          modalResultData.offender,
          modalResultData.picture
        )
      ),
      map(() => OffendersActions.getOffenders())
    )
  );

  // logout$ = createEffect(() =>
  //     this.actions$
  //         .pipe(
  //             ofType(AuthActions.logout),
  //             tap(action => {
  //                 localStorage.removeItem('user');
  //                 this.router.navigateByUrl('/login');
  //             })
  //         )
  // , {dispatch: false});

  constructor(
    private actions$: Actions,
    private store: Store,
    private rxStompService: RxStompService,
    private offendersService: OffendersService,
    public dialog: MatDialog
  ) {}

  private startLoader() {
    return this.store.dispatch(OffendersActions.loadingStarted());
  }

  private finishLoader() {
    return this.store.dispatch(OffendersActions.loadingFinished());
  }

  private getCurrentPageAndPageSize() {
    return forkJoin([
      this.store.select(offendersCurrentPage).pipe(first()),
      this.store.select(offendersPageSize).pipe(first()),
    ]);
  }

  private openAddEditOffenderModal(
    offender?: Offender
  ): Observable<OffenderModalResult> {
    const dialogRef = this.dialog.open(AddEditOffenderComponent, {
      width: '300px',
      data: offender,
    });
    return dialogRef
      .afterClosed()
      .pipe(filter((updatedOffender) => !!updatedOffender));
  }
}
