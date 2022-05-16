import { Component, Input, OnInit } from '@angular/core';
import {
  circle,
  icon,
  Icon,
  latLng,
  LatLngExpression,
  Marker,
  marker,
  polygon,
  tileLayer,
  Map,
  point,
  polyline,
  LatLngBoundsExpression,
  latLngBounds,
} from 'leaflet';
import { Offender } from '../../models/offender.model';

@Component({
  selector: 'app-offenders-map',
  templateUrl: './offenders-map.component.html',
  styleUrls: ['./offenders-map.component.scss'],
})
export class OffendersMapComponent implements OnInit {
  @Input() offenders: Array<Offender> | null = null;

  streetMaps = tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    detectRetina: true,
    attribution:
      '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
  });
  wMaps = tileLayer('http://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png', {
    detectRetina: true,
    attribution:
      '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
  });
  googleMap = tileLayer('http://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
    maxZoom: 20,
    subdomains: ['mt0', 'mt1', 'mt2', 'mt3'],
  });

  options = {
    layers: [this.streetMaps],
    zoom: 7,
    center: latLng([46.879966, -121.726909]),
  };
  layersControl = {
    baseLayers: {
      'Street Maps': this.streetMaps,
      'Wikimedia Maps': this.wMaps,
      'Google Maps': this.googleMap,
    },
    overlays: {},
  };

  constructor() {}

  ngOnInit() {}

  openPopup(marker: any) {
    console.log(marker);
  }

  getOffenderLayer(offender: Offender): Marker {
    const position: LatLngExpression = latLng(
      offender.position.latitude,
      offender.position.longitude
    );
    return marker(position, {
      icon: icon({
        ...Icon.Default.prototype.options,
        iconUrl: 'assets/marker-icon.png',
        iconRetinaUrl: 'assets/marker-icon-2x.png',
        shadowUrl: 'assets/marker-shadow.png',
      }),
    });
  }

  onMapReady(map: Map) {
    if (this.offenders) {
      const latitudes = this.offenders.map(
        (offender) => offender.position.latitude
      );
      const longitudes = this.offenders.map(
        (offender) => offender.position.longitude
      );
      const maxLatitude = Math.max(...latitudes);
      const minLatitude = Math.min(...latitudes);
      const maxLongitude = Math.max(...longitudes);
      const minLongitude = Math.min(...longitudes);
      map.fitBounds(
        latLngBounds(
          latLng(minLatitude, minLongitude),
          latLng(maxLatitude, maxLongitude)
        ),
        {
          padding: point(50, 50),
          maxZoom: 12,
          animate: true,
        }
      );
    }
  }
}
