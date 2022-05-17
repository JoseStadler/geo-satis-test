import { createFeatureSelector, createSelector } from '@ngrx/store';
import { OffendersState } from './offenders.reducer';

export const selectOffendersState =
  createFeatureSelector<OffendersState>('offenders');

export const offendersPagedList = createSelector(
  selectOffendersState,
  (state) => state.offenders
);

export const offendersList = createSelector(
  offendersPagedList,
  (offenders) => offenders.content
);

export const offendersCurrentPage = createSelector(
  selectOffendersState,
  (state) => state.currentPage
);

export const offendersPageSize = createSelector(
  offendersPagedList,
  (offenders) => offenders.size
);

export const loadingOffenders = createSelector(
  selectOffendersState,
  (state) => state.loading
);

export const offendersListIds = createSelector(offendersList, (offenders) =>
  offenders.map((offender) => offender.id)
);
