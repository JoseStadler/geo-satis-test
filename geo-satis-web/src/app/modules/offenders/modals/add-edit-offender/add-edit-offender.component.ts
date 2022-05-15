import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ERROR_MESSAGES } from 'src/app/shared/base.constants';
import { OffenderModalResult } from '../../models/offender-modal-result.model';
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
    birthDate: new FormControl(),
  });
  picture = new FormControl();

  constructor(
    private dialogRef: MatDialogRef<
      AddEditOffenderComponent,
      OffenderModalResult
    >,
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

  fileDropped(files: FileList) {
    this.loadFile(files?.item(0));
  }

  fileSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    this.loadFile(target.files?.item(0));
  }

  private loadFile(picture?: File | null) {
    this.picture.setValue(picture);
  }

  ok() {
    this.form.markAllAsTouched();
    if (this.form.valid) {
      this.dialogRef.close(
        this.picture.value
          ? { offender: this.form.value, picture: this.picture.value }
          : { offender: this.form.value }
      );
    }
  }

  private isEditing(): boolean {
    return !!this.offender;
  }

  private setEditDataIntoForm() {
    this.form.patchValue(this.offender);
  }
}
