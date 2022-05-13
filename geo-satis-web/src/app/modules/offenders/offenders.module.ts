import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { LayoutModule } from 'src/app/shared/components/layout/layout.module';
import { OffenderComponent } from './components/offender/offender.component';
import { OffendersMapComponent } from './components/offenders-map/offenders-map.component';
import { OffendersRoutingModule } from './offenders-routing.module';
import { OffendersComponent } from './offenders.component';
import { OffendersEffects } from './store/offenders.effects';
import { offendersReducer } from './store/offenders.reducer';

@NgModule({
  declarations: [OffendersComponent, OffendersMapComponent, OffenderComponent],
  imports: [
    CommonModule,
    OffendersRoutingModule,
    LayoutModule,
    MatButtonModule,
    LeafletModule,
    MatIconModule,
    StoreModule.forFeature('offenders', offendersReducer),
    EffectsModule.forFeature([OffendersEffects]),
    MatProgressSpinnerModule,
  ],
  exports: [],
})
export class OffendersModule {}
