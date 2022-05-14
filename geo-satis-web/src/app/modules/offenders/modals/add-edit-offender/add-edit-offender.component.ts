import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-add-edit-offender',
  templateUrl: './add-edit-offender.component.html',
  styleUrls: ['./add-edit-offender.component.scss'],
})
export class AddEditOffenderComponent implements OnInit {
  form: FormGroup = new FormGroup({
    firstName: new FormControl(null, [Validators.required]),
    lastName: new FormControl(),
    position: new FormGroup({
      latitude: new FormControl(),
      longitude: new FormControl(),
    }),
    picture: new FormControl(),
    birthDate: new FormControl(),
  });

  constructor(private dialogRef: MatDialogRef<AddEditOffenderComponent>) {}

  ngOnInit() {}

  close() {
    this.dialogRef.close();
  }

  ok() {}
}
