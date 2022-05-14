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
      switchMap((offender) => this.offendersService.saveOffender(offender)),
      map(() => OffendersActions.getOffenders())
    )
  );

  updateOffender$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OffendersActions.updateOffender),
      switchMap((action) =>
        this.openAddEditOffenderModal(action).pipe(
          map((offender) => {
            return {
              ...offender,
              id: action.id,
            };
          })
        )
      ),
      switchMap((offender) =>
        this.offendersService.updateOffender(offender.id, offender)
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

  private openAddEditOffenderModal(offender?: Offender): Observable<Offender> {
    const dialogRef = this.dialog.open(AddEditOffenderComponent, {
      width: '300px',
      data: offender,
    });
    return dialogRef
      .afterClosed()
      .pipe(filter((updatedOffender) => !!updatedOffender));
  }
}
