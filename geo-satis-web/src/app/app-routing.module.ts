import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'offenders/tracker' },
  {
    path: 'offenders',
    loadChildren: () =>
      import('./modules/offenders/offenders.module').then(
        (module) => module.OffendersModule
      ),
  },
  { path: '**', redirectTo: 'offenders/tracker' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
