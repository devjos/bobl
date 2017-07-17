import {Component} from '@angular/core';
import {NavController} from 'ionic-angular';

import {DemandsService} from '../../providers/demands.service';
import {Demand} from '../../models/demand.model';

import {AuthService} from '../../providers/auth.service';

import {AddPage} from '../add/add';
import {GPSTrackingPage} from '../gpsTracking/gpsTracking';
import {delay} from "rxjs/operator/delay";

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  demands: Demand[];
  gpsTracking = GPSTrackingPage;

  constructor(public navCtrl: NavController, private demandsService: DemandsService, private authService: AuthService) {

  }

  ionViewDidLoad() {
    //for testing
    /*
     let testUser = {
     user : "83",
     password : "ler5a34r1nuh"
     };
     this.authService.newUser(testUser);
     */
    //this.authService.deleteUser();



  }
  pushPage() {
    this.navCtrl.push(AddPage);
  }

  ionViewDidEnter() {
    let inner = this;
    console.log("view neu geladen");
    this.authService.authenticate(function (){
      console.log('call demand service');
      inner.demandsService.getDemands().subscribe(demands => {
        inner.demands = demands;
        console.log("demands: "+ demands);

        if (inner.demands.length === 0) {
          inner.demands = inner.demandsService.getDummyDemand();
        }
      });
    });
    this.demands = inner.demands;
    console.log(this.demands);
  }
}
