import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule } from '@angular/material/dialog';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { DragAndDropModule } from 'ngx-jasr-lib';
import { LayoutModule } from 'src/app/shared/components/layout/layout.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { OffenderComponent } from './components/offender/offender.component';
import { OffendersMapComponent } from './components/offenders-map/offenders-map.component';
import { OffendersRoutingModule } from './offenders-routing.module';
import { OffendersComponent } from './offenders.component';
import { OffendersEffects } from './store/offenders.effects';
import { offendersReducer } from './store/offenders.reducer';
import { AddEditOffenderComponent } from './modals/add-edit-offender/add-edit-offender.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    OffendersComponent,
    OffendersMapComponent,
    OffenderComponent,
    AddEditOffenderComponent,
  ],
  imports: [
    SharedModule,
    CommonModule,
    OffendersRoutingModule,
    LayoutModule,
    MatButtonModule,
    LeafletModule,
    MatIconModule,
    StoreModule.forFeature('offenders', offendersReducer),
    EffectsModule.forFeature([OffendersEffects]),
    MatProgressSpinnerModule,
    MatDialogModule,
    ReactiveFormsModule,
    DragAndDropModule,
  ],
  exports: [],
})
export class OffendersModule {}
