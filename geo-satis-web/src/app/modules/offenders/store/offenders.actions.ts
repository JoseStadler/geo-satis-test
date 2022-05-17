import { createAction, props } from '@ngrx/store';
import { PaginatedList } from 'src/app/shared/models/paginated-list.model';
import { OffenderDTO } from '../models/offender-dto.model';
import { Offender } from '../models/offender.model';

export const getOffenders = createAction(
  '[Offenders Page] Find offenders list'
);

export const getOffendersFinished = createAction(
  '[Offenders Page] Find offenders list finished',
  props<PaginatedList<Offender>>()
);

export const loadingStarted = createAction('[Offenders Page] loading started');

export const loadingFinished = createAction(
  '[Offenders Page] loading finished'
);

export const changeOffendersPage = createAction(
  '[Offenders Page] Current page changed',
  props<{ page: number }>()
);

export const saveNewOffender = createAction(
  '[Offenders Page] Save new Offender'
);

export const updateOffender = createAction(
  '[Offenders Page] update Offender',
  props<Offender>()
);

export const trackOffenders = createAction('[Offenders Page] track Offenders');

export const updateOffenderTrackedOffender = createAction(
  '[Offenders Page] update Tracked Offender',
  props<OffenderDTO>()
);

export const stopTrackedOffenders = createAction(
  '[Offenders Page] stop Tracked Offenders',
  props<{ offenderIds: number[] }>()
);
