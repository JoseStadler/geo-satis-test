import { createReducer, on } from '@ngrx/store';
import { PaginatedList } from 'src/app/shared/models/paginated-list.model';
import { Offender } from '../models/offender.model';
import { OffendersActions } from './action-types';

export interface OffendersState {
  offenders: PaginatedList<Offender>;
  currentPage: number;
  loading: boolean;
}

export const initialOffenderState: OffendersState = {
  offenders: {
    content: [],
    number: 0,
    numberOfElements: 0,
    size: 5,
    totalElements: 0,
    totalPages: 0,
  },
  currentPage: 1,
  loading: false,
};

export const offendersReducer = createReducer(
  initialOffenderState,

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
  }),
  on(OffendersActions.updateOffenderTrackedOffender, (state, action) => {
    const offenders: Array<Offender> = state.offenders.content.slice();
    const index = offenders.findIndex((offender) => offender.id === action.id);
    const offender = { ...offenders[index] };
    offender.position = {
      latitude: action.latitude,
      longitude: action.longitude,
    };
    offenders[index] = offender;
    return {
      ...state,
      offenders: {
        ...state.offenders,
        content: offenders,
      },
    };
  })
);
