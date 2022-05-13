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
  @Input()
  totalPages!: number;
  @Input()
  page!: number;
  @Output() pageChanged = new EventEmitter<number>();
  @Input() tableSize = 5;
}
