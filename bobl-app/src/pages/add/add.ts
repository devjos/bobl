import {Component, OnInit} from '@angular/core';
import {NavController, NavParams} from 'ionic-angular';
import {DemandsService} from '../../providers/demands.service';
import {Demand} from '../../models/demand.model';
import {NativeGeocoder, NativeGeocoderForwardResult} from '@ionic-native/native-geocoder';
import {AlertController} from 'ionic-angular';

@Component({
  selector: 'page-add',
  templateUrl: 'add.html'
})
export class AddPage implements OnInit {

  constructor(public navCtrl: NavController,
              public navParams: NavParams,
              private demandsService: DemandsService,
              private nativeGeocoder: NativeGeocoder,
              private alertController: AlertController) {
  }

  demand: Demand;
  demandTest: Demand;
  id: number;
  //geocodeOutput: any;

  testCheckboxOpen = false;

  ngOnInit() {
    console.log("navParams");
    console.log(this.navParams.get("demandPass"));
    if (this.navParams.get("demandPass") === undefined) {
      console.log("no demand passed");
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
        "weekdays": [0, 0, 0, 0, 0, 0, 0]
      };
    }
    else {
      this.demand = this.navParams.get("demandPass");
      console.log("got demand: " + this.demand);
    }

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
      "outboundTime": "01:00",
      "waybackTime": "08:00",
      "weekdays": [0, 0, 1, 0, 0, 1, 1]
    };
  }

  addDemand() {

    //change demandTest to demand when ready.
    console.log("demandTest: ");
    console.log(this.demandTest);

    this.getLocation(this.demand.source);
    this.getLocation(this.demand.destination);

    console.log("demand: ");
    console.log(this.demand);
    this.demandsService.create(this.demandTest);
  }

  getLocation(location: string) {
    this.nativeGeocoder.forwardGeocode(location)
      .then((coordinates: NativeGeocoderForwardResult) => console.log('The coordinates are latitude='
        + coordinates.latitude + ' and longitude=' + coordinates.longitude))
      .catch((error: any) => console.log(error));
  }

  doCheckbox() {
    let alert = this.alertController.create();
    alert.setTitle('Wochentage');

    alert.addInput({
      type: 'checkbox',
      label: 'Montag',
      value: '0',
      checked: Boolean(this.demand.weekdays[0])
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Dienstag',
      value: '1',
      checked: Boolean(this.demand.weekdays[1])
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Mittwoch',
      value: '2',
      checked: Boolean(this.demand.weekdays[2])
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Donnerstag',
      value: '3',
      checked: Boolean(this.demand.weekdays[3])
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Freitag',
      value: '4',
      checked: Boolean(this.demand.weekdays[4])
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Samstag',
      value: '5',
      checked: Boolean(this.demand.weekdays[5])
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Sonntag',
      value: '6',
      checked: Boolean(this.demand.weekdays[6])
    });

    alert.addButton('Abbrechen');
    alert.addButton({
      text: 'Ok',
      handler: (data: any) => {
        this.testCheckboxOpen = false;
        //reset
        for (let i in this.demand.weekdays) {
          this.demand.weekdays[i] = 0;
        }
        for (let i in data) {
          this.demand.weekdays[Number(data[i])] = 1;
        }
      }
    });

    alert.present();
  }

}
