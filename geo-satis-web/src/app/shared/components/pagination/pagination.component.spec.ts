/* tslint:disable:no-unused-variable */
import {
  async,
  ComponentFixture,
  fakeAsync,
  flush,
  TestBed,
} from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { PaginationComponent } from './pagination.component';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { FormsModule } from '@angular/forms';

describe('PaginationComponent', () => {
  let component: PaginationComponent;
  let fixture: ComponentFixture<PaginationComponent>;
  let el: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PaginationComponent],
      imports: [PaginationModule, FormsModule],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaginationComponent);
    component = fixture.componentInstance;
    el = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have one page', () => {
    const pages = el.queryAll(By.css('.pagination-page')),
      pageNumber = el.queryAll(By.css('.pagination-page .page-link'));

    expect(pages.length)
      .withContext('Page items could not be found')
      .toBeGreaterThan(0);
    expect(pages.length).withContext('There is more than one page').toBe(1);
    expect(pageNumber[0].nativeElement.innerText)
      .withContext('Incorrect menu icon')
      .toBe('1');
  });

  it('should have two pages', () => {
    component.totalElements = 9;
    fixture.detectChanges();

    const pages = el.queryAll(By.css('.pagination-page')),
      pageNumber = el.queryAll(By.css('.pagination-page .page-link'));

    expect(pages.length).withContext('There is more than one page').toBe(2);
    expect(pageNumber[0].nativeElement.innerText)
      .withContext('Incorrect page number')
      .toBe('1');
    expect(pageNumber[1].nativeElement.innerText)
      .withContext('Incorrect page number')
      .toBe('2');
  });

  it('should have one pagination next button', () => {
    const paginationNext = el.queryAll(By.css('.pagination-next')),
      paginationNextLink = el.queryAll(By.css('.pagination-next .page-link'));

    expect(paginationNext.length)
      .withContext('There has to be 1 pagination next button')
      .toBe(1);
    expect(paginationNextLink[0].nativeElement.innerText)
      .withContext('Incorrect icon')
      .toBe('›');
  });

  it('should have one pagination last button', () => {
    const paginationLast = el.queryAll(By.css('.pagination-last')),
      paginationLastLink = el.queryAll(By.css('.pagination-last .page-link'));

    expect(paginationLast.length)
      .withContext('There has to be 1 pagination last button')
      .toBe(1);
    expect(paginationLastLink[0].nativeElement.innerText)
      .withContext('Incorrect icon')
      .toBe('»');
  });

  it('should have one pagination previous button', () => {
    const paginationPrev = el.queryAll(By.css('.pagination-prev')),
      paginationPrevLink = el.queryAll(By.css('.pagination-prev .page-link'));

    expect(paginationPrev.length)
      .withContext('There has to be 1 pagination previous button')
      .toBe(1);
    expect(paginationPrevLink[0].nativeElement.innerText)
      .withContext('Incorrect icon')
      .toBe('‹');
  });

  it('should have one pagination first button', () => {
    const paginationFirst = el.queryAll(By.css('.pagination-first')),
      paginationFirstLink = el.queryAll(By.css('.pagination-first .page-link'));

    expect(paginationFirst.length)
      .withContext('There has to be 1 pagination first button')
      .toBe(1);
    expect(paginationFirstLink[0].nativeElement.innerText)
      .withContext('Incorrect icon')
      .toBe('«');
  });

  it('should change pages on click pages buttons click', fakeAsync(() => {
    let currentPage = component.page;
    component.totalElements = 21;
    component.pageChanged.subscribe((page) => (currentPage = page));
    fixture.detectChanges();

    const pages = el.queryAll(By.css('.pagination-page .page-link')),
      paginationLast = el.queryAll(By.css('.pagination-last .page-link')),
      paginationFirst = el.queryAll(By.css('.pagination-first .page-link')),
      paginationPrev = el.queryAll(By.css('.pagination-prev .page-link')),
      paginationNext = el.queryAll(By.css('.pagination-next .page-link'));

    paginationLast[0].nativeElement.click();
    fixture.detectChanges();
    flush();
    expect(component.page).withContext('The page has not changed').toBe(5);
    expect(currentPage).withContext('The page has not changed').toBe(5);

    paginationFirst[0].nativeElement.click();
    fixture.detectChanges();
    flush();
    expect(component.page).withContext('The page has not changed').toBe(1);
    expect(currentPage).withContext('The page has not changed').toBe(1);

    paginationNext[0].nativeElement.click();
    fixture.detectChanges();
    flush();
    expect(component.page).withContext('The page has not changed').toBe(2);
    expect(currentPage).withContext('The page has not changed').toBe(2);

    paginationPrev[0].nativeElement.click();
    fixture.detectChanges();
    flush();
    expect(component.page).withContext('The page has not changed').toBe(1);
    expect(currentPage).withContext('The page has not changed').toBe(1);
  }));
});
