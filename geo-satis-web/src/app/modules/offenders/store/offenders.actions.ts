import { createAction, props } from '@ngrx/store';
import { BaseSearch } from 'src/app/shared/models/base-search.model';
import { PaginatedList } from 'src/app/shared/models/paginated-list.model';
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
