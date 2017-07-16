import {Component, Input, OnInit} from '@angular/core';
import {NavController} from 'ionic-angular';
import {DemandsService} from '../../providers/demands.service';
import {Demand} from '../../models/demand.model';
import {NativeGeocoder, NativeGeocoderForwardResult} from '@ionic-native/native-geocoder';

@Component({
  selector: 'page-add',
  templateUrl: 'add.html'
})
export class AddPage implements OnInit {

  constructor(public navCtrl: NavController, private demandsService: DemandsService, private nativeGeocoder: NativeGeocoder) {
  }

  demand: Demand;
  demandTest: Demand;
  id: number;
  //geocodeOutput: any;

  addDemand() {

    //change demandTest to demand when ready.
    console.log("demandTest: ");
    console.log(this.demandTest);

    this.getLocation(this.demand.source);
    this.getLocation(this.demand.destination);

    this.demandsService.create(this.demandTest);
  }

  getLocation(location: string) {
    this.nativeGeocoder.forwardGeocode(location)
      .then((coordinates: NativeGeocoderForwardResult) => console.log('The coordinates are latitude='
          + coordinates.latitude + ' and longitude=' + coordinates.longitude))
      .catch((error: any) => console.log(error));
  }

  ngOnInit() {

    this.demand = {
      "id": null,
      "title": "",
      "source": "",
      "sourceLatitude": null,
      "sourceLongitude": null,
      "destination": "",
      "destinationLatitude": null,
      "destinationLongitude": null,
      //"type" : "permanent",
      "outboundTime": "",
      "waybackTime": null,
      "weekdays": [false, false, false, false, false, false, false]
    };

    //test demand
    this.demandTest = {
      "id": null,
      "title": "test",
      "source": "Garching",
      "sourceLatitude": 48.135125,
      "sourceLongitude": 11.581981,
      "destination": "Neufinsing",
      "destinationLatitude": 52.520007,
      "destinationLongitude": 13.404954,
      //"type" : "permanent",
      "outboundTime": "20:00",
      "waybackTime": null,
      "weekdays": [false, false, true, false, false, true, true]
    };
  }

}
