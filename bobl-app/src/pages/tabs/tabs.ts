import { Component } from '@angular/core';

import { AddPage } from '../add/add';
import { GPSTrackingPage } from '../gpsTracking/gpsTracking';
import { HomePage } from '../home/home';

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = HomePage;
  tab2Root = AddPage;
  tab3Root = GPSTrackingPage;

  constructor() {

  }
}
