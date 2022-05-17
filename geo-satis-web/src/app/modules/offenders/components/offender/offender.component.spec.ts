/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { OffenderComponent } from './offender.component';
import { MatIconModule } from '@angular/material/icon';

describe('OffenderComponent', () => {
  let component: OffenderComponent;
  let fixture: ComponentFixture<OffenderComponent>;
  let el: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OffenderComponent],
      imports: [MatIconModule],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OffenderComponent);
    component = fixture.componentInstance;
    component.offender = {
      id: 1,
      firstName: 'Jose',
      lastName: 'Stadler',
      position: {
        latitude: -120,
        longitude: 45,
      },
    };
    el = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have mat-icon account_circle', () => {
    const matIcon = el.query(By.css('mat-icon'));

    expect(matIcon).withContext('Must have the mat-icon shown').toBeTruthy();
    expect(matIcon.nativeElement.innerHTML)
      .withContext('Must have the mat-icon shown with the " account_circle "')
      .toBe(' account_circle ');
  });

  it('should render the last name of the offender', () => {
    const lastName = el.query(By.css('#offender_last_name span'));

    expect(lastName).toBeTruthy();
    expect(lastName.nativeElement.innerHTML)
      .withContext('Last name has to be Stadler')
      .toBe('Stadler');
  });

  it('should render the first name of the offender', () => {
    const firstName = el.query(By.css('#offender_first_name span'));

    expect(firstName).toBeTruthy();
    expect(firstName.nativeElement.innerHTML)
      .withContext('First name has to be Jose')
      .toBe('Jose');
  });

  it('should render the position latitude of the offender', () => {
    const latitude = el.query(By.css('#offender_latitude span'));

    expect(latitude).toBeTruthy();
    expect(latitude.nativeElement.innerHTML)
      .withContext('First name has to be Jose')
      .toBe('-120');
  });

  it('should render the position latitude of the offender', () => {
    const longitude = el.query(By.css('#offender_longitude span'));

    expect(longitude).toBeTruthy();
    expect(longitude.nativeElement.innerHTML)
      .withContext('First name has to be Jose')
      .toBe('45');
  });

  it('should emit an edit event on editButton clicked', (done) => {
    const btn = el.query(By.css('#edit_offender'));

    component.edit.subscribe((editOffender) => {
      expect(component.offender)
        .withContext(
          'Edit Offender has to be the same as the offender shown in the component'
        )
        .toEqual(editOffender);
      done();
    });

    expect(btn).toBeTruthy();
    btn.nativeElement.click();
  });
});
