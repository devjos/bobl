import { Component } from '@angular/core';

import { AddPage } from '../add/add';
import { SettingsPage } from '../settings/settings';
import { HomePage } from '../home/home';

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = HomePage;
  tab2Root = AddPage;
  tab3Root = SettingsPage;

  constructor() {

  }
}
