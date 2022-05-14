import {
  ActionReducer,
  ActionReducerMap,
  createFeatureSelector,
  createReducer,
  createSelector,
  MetaReducer,
  on,
} from '@ngrx/store';
import { PaginatedList } from 'src/app/shared/models/paginated-list.model';
import { Offender } from '../models/offender.model';
import { OffendersActions } from './action-types';

export interface OffendersState {
  offenders: PaginatedList<Offender>;
  currentPage: number;
  loading: boolean;
}

export const initialAuthState: OffendersState = {
  offenders: {
    content: [],
    number: 0,
    numberOfElements: 0,
    size: 5,
    totalElements: 0,
    totalPages: 0,
  },
  currentPage: 0,
  loading: false,
};

export const offendersReducer = createReducer(
  initialAuthState,

  on(OffendersActions.getOffenders, (state) => {
    return {
      ...state,
      offenders: { ...state.offenders, content: [] },
    };
  }),
  on(OffendersActions.getOffendersFinished, (state, action) => {
    return {
      ...state,
      offenders: action,
    };
  }),
  on(OffendersActions.loadingStarted, (state) => {
    return {
      ...state,
      loading: true,
    };
  }),
  on(OffendersActions.loadingFinished, (state) => {
    return {
      ...state,
      loading: false,
    };
  }),
  on(OffendersActions.changeOffendersPage, (state, action) => {
    return {
      ...state,
      currentPage: action.page,
    };
  })
);
