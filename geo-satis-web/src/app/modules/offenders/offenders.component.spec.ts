/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { OffendersComponent } from './offenders.component';

describe('OffendersComponent', () => {
  let component: OffendersComponent;
  let fixture: ComponentFixture<OffendersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OffendersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OffendersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
