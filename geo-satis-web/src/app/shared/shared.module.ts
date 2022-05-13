import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { DateTimePipe } from './pipes/date-time.pipe';

const pipes = [DateTimePipe];

@NgModule({
  declarations: [...pipes],
  imports: [CommonModule],
  exports: [...pipes],
})
export class SharedModule {}
