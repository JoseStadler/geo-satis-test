/* tslint:disable:no-unused-variable */
import {
  async,
  ComponentFixture,
  fakeAsync,
  flushMicrotasks,
  TestBed,
  tick,
} from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { Store, StoreModule } from '@ngrx/store';
import { provideMockStore, MockStore } from '@ngrx/store/testing';
import { OffendersComponent } from './offenders.component';
import {
  loadingOffenders,
  offendersList,
  offendersPagedList,
} from './store/offenders.selectors';
import { OffendersModule } from './offenders.module';
import { OffenderComponent } from './components/offender/offender.component';
import { OffendersMapComponent } from './components/offenders-map/offenders-map.component';
import { AddEditOffenderComponent } from './modals/add-edit-offender/add-edit-offender.component';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { MatIconModule } from '@angular/material/icon';
import { PaginationComponent } from 'src/app/shared/components/pagination/pagination.component';
import { FormsModule } from '@angular/forms';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';

describe('OffendersComponent', () => {
  let component: OffendersComponent;
  let fixture: ComponentFixture<OffendersComponent>;
  let store: MockStore;
  let el: DebugElement;
  const initialState = {
    offenders: {
      content: [],
      number: 0,
      numberOfElements: 0,
      size: 5,
      totalElements: 0,
      totalPages: 0,
    },
    currentPage: 1,
    loading: false,
  };
  const offenders = [
    {
      id: 1,
      firstName: 'Jose',
      lastName: 'Stadler',
      position: {
        latitude: -120,
        longitude: 45,
      },
    },
  ];
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        OffendersComponent,
        OffendersMapComponent,
        OffenderComponent,
        AddEditOffenderComponent,
        PaginationComponent,
      ],
      imports: [
        StoreModule.forRoot({}),
        PaginationModule,
        MatIconModule,
        FormsModule,
        LeafletModule,
        HttpClientTestingModule,
      ],
      providers: [
        Store,
        provideMockStore({
          initialState: initialState,
          selectors: [
            { selector: loadingOffenders, value: false },
            {
              selector: offendersPagedList,
              value: {
                content: offenders,
                number: 0,
                numberOfElements: 1,
                size: 5,
                totalElements: 1,
                totalPages: 1,
              },
            },
            { selector: offendersList, value: offenders },
          ],
        }),
      ],
    }).compileComponents();
    store = TestBed.inject(MockStore);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OffendersComponent);
    component = fixture.componentInstance;
    el = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have loader when loading = true', fakeAsync(() => {
    store.overrideSelector(loadingOffenders, true);
    fixture.detectChanges();

    const loader = el.queryAll(By.css('.loader'));

    expect(loader).toBeTruthy();
  }));

  it('should not have loader when loading = false', fakeAsync(() => {
    const loader = el.query(By.css('.loader'));

    expect(loader).toBeFalsy();
  }));

  it('should not display map when loading = true', fakeAsync(() => {
    store.overrideSelector(loadingOffenders, true);
    fixture.detectChanges();

    const map = el.query(By.css('.map'));

    expect(map).toBeFalsy();
  }));

  it('should display map when loading = false', fakeAsync(() => {
    const map = el.query(By.css('#map'));

    expect(map).toBeTruthy();
  }));

  it('should have 1 offender in the list', fakeAsync(() => {
    const offendersComponent = el.queryAll(By.css('app-offender'));

    expect(offendersComponent.length).toBeTruthy();
    expect(offendersComponent.length).toBe(1);
  }));

  it('should display a pagination', fakeAsync(() => {
    const offendersComponent = el.query(By.css('app-pagination'));

    expect(offendersComponent).toBeTruthy();
  }));
});
