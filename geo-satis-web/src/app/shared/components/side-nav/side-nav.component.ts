import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.scss'],
})
export class SideNavComponent implements OnInit {
  menus = [{ name: 'Offenders', url: '/offenders/tracker', icon: 'bar_chart' }];

  constructor() {}

  ngOnInit() {}
}
