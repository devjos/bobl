import { Component, OnInit } from '@angular/core';
import { NavController } from 'ionic-angular';

import { DemandsService } from '../../providers/demands.service';
import { Demand } from '../../models/demand.model';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage implements OnInit {
  demands: Demand[] = [];

  constructor(public navCtrl: NavController, private demandsService: DemandsService) {

  }

  ngOnInit() {
    this.demandsService.getDemands().subscribe(demands => {
      this.demands = demands;
    });
  }

}
