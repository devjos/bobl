import { Component, OnInit } from '@angular/core';
import { NavController } from 'ionic-angular';

import { DemandsService } from '../../providers/demands.service';
import { Demand } from '../../models/demand.model';

import { AuthService } from '../../providers/auth.service';

import { AddPage } from '../add/add';
import { GPSTrackingPage } from '../gpsTracking/gpsTracking';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage implements OnInit {
  demands: Demand[] = [];
  gpsTracking = GPSTrackingPage;

  constructor(public navCtrl: NavController, private demandsService: DemandsService, private authService: AuthService) {

  }

  ngOnInit() {
    if (this.authService.getCurrentUser().username === "") {
      this.authService.signup();
      console.log("signup");
    }
    else {
      this.authService.login()
      console.log("login");
    }

    this.demandsService.getDemands().subscribe(demands => {
      this.demands = demands;
    });
  }

  pushPage() {
    this.navCtrl.push(AddPage);
  }

  ionViewDidEnter() {
    console.log("view neu geladen");
    this.demandsService.getDemands().subscribe(demands => {
      this.demands = demands;
    });
  }

}
