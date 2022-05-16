import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';

@Component({
  selector: 'app-pagination',
  template: `
    <pagination
      [boundaryLinks]="true"
      [totalItems]="totalPages"
      [(ngModel)]="page"
      (pageChanged)="pageChanged.emit($event.page)"
      previousText="&lsaquo;"
      nextText="&rsaquo;"
      firstText="&laquo;"
      lastText="&raquo;"
      [maxSize]="3"
      [rotate]="true"
      [itemsPerPage]="tableSize"
    >
    </pagination>
  `,
})
export class PaginationComponent {
  @Input() set totalPages(totalPages: number) {
    this._totalPages = totalPages;
  }
  @Input()
  page!: number;
  @Output() pageChanged = new EventEmitter<number>();
  @Input() tableSize = 5;

  private _totalPages: number = 0;

  get totalPages() {
    return this._totalPages;
  }
}
