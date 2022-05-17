import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  template: `
    <pagination
      [boundaryLinks]="true"
      [totalItems]="totalElements"
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
  @Input() totalElements: number = 0;
  @Input() page: number = 1;
  @Input() tableSize = 5;
  @Output() pageChanged = new EventEmitter<number>();
}
