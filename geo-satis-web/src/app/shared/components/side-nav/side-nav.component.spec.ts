/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { SideNavComponent } from './side-nav.component';
import { LayoutModule } from '../layout/layout.module';
import { RouterTestingModule } from '@angular/router/testing';

describe('SideNavComponent', () => {
  let component: SideNavComponent;
  let fixture: ComponentFixture<SideNavComponent>;
  let el: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [LayoutModule, RouterTestingModule],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SideNavComponent);
    component = fixture.componentInstance;
    el = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render menu items', () => {
    const menuItems = el.queryAll(By.css('.side-nav__item mat-icon'));

    expect(menuItems.length)
      .withContext('menu items could not be found')
      .toBeGreaterThan(0);
    expect(menuItems[0].nativeElement.innerHTML)
      .withContext('Incorrect menu icon')
      .toBe(' bar_chart ');
  });

  it('should have menus', () => {
    expect(component.menus.length)
      .withContext('There are no menus')
      .toBeGreaterThan(0);
  });
});
