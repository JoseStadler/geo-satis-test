import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ERROR_MESSAGES } from 'src/app/shared/base.constants';
import { Offender } from '../../models/offender.model';

@Component({
  selector: 'app-add-edit-offender',
  templateUrl: './add-edit-offender.component.html',
  styleUrls: ['./add-edit-offender.component.scss'],
})
export class AddEditOffenderComponent implements OnInit {
  readonly errorMessages = ERROR_MESSAGES;

  form: FormGroup = new FormGroup({
    firstName: new FormControl(null, [Validators.required]),
    lastName: new FormControl(null, [Validators.required]),
    position: new FormGroup({
      latitude: new FormControl(null, [Validators.required]),
      longitude: new FormControl(null, [Validators.required]),
    }),
    picture: new FormControl(),
    birthDate: new FormControl(),
  });

  constructor(
    private dialogRef: MatDialogRef<AddEditOffenderComponent, Offender>,
    @Inject(MAT_DIALOG_DATA) public offender: Offender
  ) {}

  ngOnInit() {
    if (this.isEditing()) {
      this.setEditDataIntoForm();
    }
  }

  close() {
    this.dialogRef.close();
  }

  ok() {
    this.form.markAllAsTouched();
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  private isEditing(): boolean {
    return !!this.offender;
  }

  private setEditDataIntoForm() {
    this.form.patchValue(this.offender);
  }
}
