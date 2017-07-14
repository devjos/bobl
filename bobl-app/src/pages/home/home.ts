import { Component, OnInit } from '@angular/core';
import { NavController } from 'ionic-angular';

import { DemandsService } from '../../providers/demands.service';
import { Demand } from '../../models/demand.model';

import { AuthService } from '../../providers/auth.service';

import { AddPage } from '../add/add';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage implements OnInit {
  demands: Demand[] = [];

  constructor(public navCtrl: NavController, private demandsService: DemandsService, private authService: AuthService) {

  }

  ngOnInit() {
    console.log("signup");
    this.authService.signup();

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
