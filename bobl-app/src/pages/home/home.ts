import {Component} from '@angular/core';
import {NavController} from 'ionic-angular';

import {DemandsService} from '../../providers/demands.service';
import {Demand} from '../../models/demand.model';

import {AuthService} from '../../providers/auth.service';

import {AddPage} from '../add/add';
import {GPSTrackingPage} from '../gpsTracking/gpsTracking';

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

    if (this.authService.isAuthenticated()) {
      this.authService.login();
      console.log("login");
    }
    else {
      this.authService.signup();
      console.log("signup");
    }
    console.log("Current user: " + this.authService.getCurrentUser().user + " "
      + this.authService.getCurrentUser().password);
  }

  pushPage() {
    this.navCtrl.push(AddPage);
  }

  ionViewDidEnter() {
    console.log("view neu geladen");
    /*
     this.demandsService.getDemands().subscribe(demands => {
     this.demands = demands;
     });
     */
    console.log(this.demands);
  }

}
