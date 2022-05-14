import { CommonModule } from '@angular/common';
import { NgModule, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { PaginationComponent } from './components/pagination/pagination.component';
import { RxStompService } from './rx-stomp/rx-stomp.service';
import { rxStompServiceFactory } from './rx-stomp/rx-stomp-service-factory';

import { DateTimePipe } from './pipes/date-time.pipe';

const components = [PaginationComponent];
const pipes = [DateTimePipe];
const materialModules = [MatInputModule, MatFormFieldModule];

@NgModule({
  declarations: [...pipes, ...components],
  imports: [CommonModule, PaginationModule, FormsModule, ...materialModules],
  exports: [...pipes, ...components, ...materialModules],
  providers: [
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
    },
  ],
})
export class SharedModule implements OnDestroy {
  constructor(private rxStompService: RxStompService) {}
  ngOnDestroy(): void {
    this.rxStompService.deactivate();
  }
}
