import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { LayoutModule } from 'src/app/shared/components/layout/layout.module';
import { OffenderComponent } from './components/offender/offender.component';
import { OffendersMapComponent } from './components/offenders-map/offenders-map.component';
import { OffendersRoutingModule } from './offenders-routing.module';
import { OffendersComponent } from './offenders.component';

@NgModule({
  declarations: [OffendersComponent, OffendersMapComponent, OffenderComponent],
  imports: [
    CommonModule,
    OffendersRoutingModule,
    LayoutModule,
    MatButtonModule,
    LeafletModule,
    MatIconModule,
  ],
  exports: [],
})
export class OffendersModule {}
