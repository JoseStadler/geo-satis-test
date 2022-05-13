import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutComponent } from 'src/app/shared/components/layout/layout.component';
import { OffendersComponent } from './offenders.component';

const routes: Routes = [
  {
    path: 'tracker',
    component: LayoutComponent,
    children: [{ path: '', component: OffendersComponent }],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OffendersRoutingModule {}
