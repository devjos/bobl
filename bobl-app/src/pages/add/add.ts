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

  @Input() demand:Demand;

  logForm() {
    console.log(this.demand);
  }

  addDemand() {
    this.demandsService.create(this.demand);
  }

  ngOnInit() {
    this.demand = new Demand();
    this.demand.id = 20;
  }

}
