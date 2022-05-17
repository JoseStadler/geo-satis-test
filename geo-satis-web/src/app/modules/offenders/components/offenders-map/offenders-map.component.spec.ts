/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { OffendersMapComponent } from './offenders-map.component';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';

describe('OffendersMapComponent', () => {
  let component: OffendersMapComponent;
  let fixture: ComponentFixture<OffendersMapComponent>;
  let el: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OffendersMapComponent],
      imports: [LeafletModule],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OffendersMapComponent);
    component = fixture.componentInstance;
    component.offenders = [
      {
        firstName: 'Giuliana',
        id: 3,
        lastName: 'Marquez',
        position: { latitude: 50.01005, longitude: -118.00147 },
      },
    ];
    el = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    const map = el.query(By.css('#map'));
    expect(component).toBeTruthy();
  });

  it('should create one marker', () => {
    const marker = el.queryAll(By.css('#marker'));
    expect(marker).toBeTruthy();
    expect(marker.length).toBe(1);
  });
});
