import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { OffendersActions } from './action-types';
import { delay, first, map, switchMap, tap } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';
import { Store } from '@ngrx/store';
import { offendersCurrentPage, offendersPageSize } from './offenders.selectors';
import { RxStompService } from 'src/app/shared/rx-stomp/rx-stomp.service';

@Injectable()
export class OffendersEffects {
  getOffenders$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OffendersActions.getOffenders),
      tap(() => this.store.dispatch(OffendersActions.loadingStarted())),
      switchMap((action) =>
        forkJoin([
          this.store.select(offendersCurrentPage).pipe(first()),
          this.store.select(offendersPageSize).pipe(first()),
        ])
      ),
      switchMap(([currentPage, size]) =>
        this.rxStompService.watch('/ws-resp/greetings').pipe(
          tap(console.log),
          map(() => {
            return {
              content: [
                {
                  lastName: 'Pierre',
                  firstName: 'Dupont',
                  position: { latitude: -42.8999, longitude: 4.6446 },
                  id: 1,
                  picture: '',
                },
                {
                  lastName: 'Pierre',
                  firstName: 'Dupont',
                  position: { latitude: -42.8999, longitude: 4.6446 },
                  id: 1,
                  picture: '',
                },
                {
                  lastName: 'Pierre',
                  firstName: 'Dupont',
                  position: { latitude: -42.8999, longitude: 4.6446 },
                  id: 1,
                  picture: '',
                },
                {
                  lastName: 'Pierre',
                  firstName: 'Dupont',
                  position: { latitude: -42.8999, longitude: 4.6446 },
                  id: 1,
                  picture: '',
                },
                {
                  lastName: 'Pierre',
                  firstName: 'Dupont',
                  position: { latitude: -42.8999, longitude: 4.6446 },
                  id: 1,
                  picture: '',
                },
              ],
              number: currentPage,
              numberOfElements: 0,
              size,
              totalElements: 0,
              totalPages: 0,
            };
          })
        )
      ),
      map((offendersList) =>
        OffendersActions.getOffendersFinished(offendersList)
      ),
      tap(() => this.store.dispatch(OffendersActions.loadingFinished()))
    )
  );

  pageChanged$ = createEffect(() =>
    this.actions$.pipe(
      ofType(OffendersActions.changeOffendersPage),
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
    private rxStompService: RxStompService
  ) {}
}
