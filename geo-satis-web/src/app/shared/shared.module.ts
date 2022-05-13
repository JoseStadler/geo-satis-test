import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { PaginationComponent } from './components/pagination/pagination.component';

import { DateTimePipe } from './pipes/date-time.pipe';

const components = [PaginationComponent];
const pipes = [DateTimePipe];

@NgModule({
  declarations: [...pipes, ...components],
  imports: [CommonModule, PaginationModule, FormsModule],
  exports: [...pipes, ...components],
})
export class SharedModule {}
