/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AddEditOffenderComponent } from './add-edit-offender.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { DragAndDropModule } from 'ngx-jasr-lib';
import {
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { MatNativeDateModule } from '@angular/material/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('AddEditOffenderComponent', () => {
  let component: AddEditOffenderComponent;
  let fixture: ComponentFixture<AddEditOffenderComponent>;
  let el: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AddEditOffenderComponent],
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        MatDatepickerModule,
        DragAndDropModule,
        MatDialogModule,
        MatNativeDateModule,
        NoopAnimationsModule,
      ],
      providers: [
        {
          provide: MatDialogRef,
          useValue: {},
        },
        {
          provide: MAT_DIALOG_DATA,
          useValue: {},
        },
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddEditOffenderComponent);
    component = fixture.componentInstance;
    el = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).withContext('should create the component').toBeTruthy();
  });

  it('should have invalid form', () => {
    expect(component.form.invalid)
      .withContext('form must be invalid')
      .toBeTrue();
  });

  it('should have valid form', () => {
    component.form.patchValue({
      id: 1,
      firstName: 'Jose',
      lastName: 'Stadler',
      position: {
        latitude: -120,
        longitude: 45,
      },
    });
    expect(component.form.valid).withContext('form must be valid').toBeTrue();
  });

  it('should init without errors', () => {
    const errors = el.queryAll(By.css('.mat-error'));

    expect(errors.length).withContext('Has to init without errors').toBe(0);
  });

  it('should show the errors', () => {
    component.form.markAllAsTouched();
    fixture.detectChanges();

    const errors = el.queryAll(By.css('.mat-error'));

    expect(errors.length).withContext('Form has 4 required controls').toBe(4);
  });
});
