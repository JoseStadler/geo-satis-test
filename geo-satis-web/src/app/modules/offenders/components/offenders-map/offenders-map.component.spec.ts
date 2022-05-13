/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { OffendersMapComponent } from './offenders-map.component';

describe('OffendersMapComponent', () => {
  let component: OffendersMapComponent;
  let fixture: ComponentFixture<OffendersMapComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OffendersMapComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OffendersMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
