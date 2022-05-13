import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Offender } from '../../models/offender.model';

@Component({
  selector: 'app-offender',
  templateUrl: './offender.component.html',
  styleUrls: ['./offender.component.scss'],
})
export class OffenderComponent {
  @Input() offender?: Offender;
  @Output() edit: EventEmitter<Offender> = new EventEmitter();

  editOffender() {
    if (this.offender) {
      this.edit.next(this.offender);
    }
  }
}
