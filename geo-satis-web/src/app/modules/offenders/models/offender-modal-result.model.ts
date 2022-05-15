import { Offender } from './offender.model';

export interface OffenderModalResult {
  offender: Offender;
  picture?: File;
}
