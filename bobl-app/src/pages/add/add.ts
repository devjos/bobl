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
  id: number;

  addDemand() {

    this.demandsService.create(this.demand);
  }

  ngOnInit() {
    this.demand = {
      id: 53,
      title: "",
      src: "",
      dst: "",
      type: "",
      outboundTime: "",
    };
  }

}
