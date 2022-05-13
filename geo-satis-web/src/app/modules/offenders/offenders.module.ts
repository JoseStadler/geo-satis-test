import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { LayoutModule } from 'src/app/shared/components/layout/layout.module';
import { OffendersRoutingModule } from './offenders-routing.module';
import { OffendersComponent } from './offenders.component';

@NgModule({
  declarations: [OffendersComponent],
  imports: [
    CommonModule,
    OffendersRoutingModule,
    LayoutModule,
    MatButtonModule,
  ],
  exports: [],
})
export class OffendersModule {}
