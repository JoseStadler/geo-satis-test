import { Component, OnInit } from '@angular/core';
import { latLng, tileLayer } from 'leaflet';

@Component({
  selector: 'app-offenders-map',
  templateUrl: './offenders-map.component.html',
  styleUrls: ['./offenders-map.component.scss'],
})
export class OffendersMapComponent implements OnInit {
  options = {
    layers: [
      tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: '...',
      }),
    ],
    zoom: 5,
    center: latLng(46.879966, -121.726909),
  };

  constructor() {}

  ngOnInit() {}
}
