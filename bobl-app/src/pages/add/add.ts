import {Component, Input, OnInit} from '@angular/core';
import {NavController} from 'ionic-angular';
import {DemandsService} from '../../providers/demands.service';
import {Demand} from "../../models/demand.model";

@Component({
  selector: 'page-add',
  templateUrl: 'add.html'
})
export class AddPage implements OnInit {

  constructor(public navCtrl: NavController, private demandsService: DemandsService) {
  }

  demand :Demand;
  demandTest : Demand;
  id: number;

  addDemand() {

    //change demandTest to demand when ready.
    console.log("demandTest: ");
    console.log(this.demandTest);
    this.demandsService.create(this.demandTest);
  }

  ngOnInit() {

    //test demand
    this.demandTest = {
      "id" : null,
      "title" : "test",
      "source" : "Garching",
      "sourceLatitude" : 48.135125,
      "sourceLongitude" : 11.581981,
      "destination" : "Neufinsing",
      "destinationLatitude" : 52.520007,
      "destinationLongitude" : 13.404954,
      //"type" : "permanent",
      "outboundTime" : "20:00",
      "waybackTime" : null,
      "weekdays" : [false,false,true,false,false,true,true]
    };
  }

}
