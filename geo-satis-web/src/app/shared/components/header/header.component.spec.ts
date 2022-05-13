/* tslint:disable:no-unused-variable */
import {
  async,
  ComponentFixture,
  fakeAsync,
  flush,
  flushMicrotasks,
  TestBed,
  tick,
} from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { HeaderComponent } from './header.component';
import { DateTimePipe } from '../../pipes/date-time.pipe';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { first, take } from 'rxjs';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let el: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderComponent, DateTimePipe],
      imports: [MatIconModule, MatMenuModule],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    el = fixture.debugElement;
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(`should change the date after every second'`, fakeAsync(() => {
    let rightNow = undefined;
    component.today$.pipe(first()).subscribe((today) => (rightNow = today));
    tick(1000);

    expect(rightNow).withContext('Date was not initialized').toBeTruthy();

    let newTime = undefined;
    component.today$.pipe(first()).subscribe((today) => (newTime = today));

    expect(newTime).withContext('New date was not initialized').toBeTruthy();
    expect(newTime).not.toEqual(rightNow);
  }));

  it('should render brand image', () => {
    const brandImage = el.query(By.css('.main-header__brand')).nativeElement;

    expect(brandImage)
      .withContext('Brand image could not be found')
      .toBeTruthy();
    expect(brandImage.src)
      .withContext('Brand image src is not the expected one')
      .toBe(component.brandImage);
  });
});
