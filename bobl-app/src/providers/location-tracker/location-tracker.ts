import { Injectable, NgZone } from '@angular/core';
import { BackgroundGeolocation } from '@ionic-native/background-geolocation';
import { Geolocation, Geoposition } from '@ionic-native/geolocation';
import { Storage } from '@ionic/storage';
import 'rxjs/add/operator/filter';

/*
  Generated class for the LocationTrackerProvider provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class LocationTrackerProvider {

  public watch: any;
  public lat: number = 0;
  public lng: number = 0;
  public items: string [];
  public dateTime : Date = null;


  constructor(public zone: NgZone, public backgroundGeolocation: BackgroundGeolocation, public geolocation: Geolocation, public storage: Storage) {
   this.storage.get('location').then((data) => {
      this.items = data;
      console.log(data);
    });
  }

  save(val){
    console.log('data added '+val);
    this.storage.get('location').then((data) => {
      if(data != null)
      {
        data.push(val);
        this.storage.set('location', data);
      }
      else
      {
        let array = [];
        array.push(val);
        this.storage.set('location', array);
      }
    });
  };

  startTracking() {

    // Background Tracking

    let config = {
      desiredAccuracy: 0,
      stationaryRadius: 20,
      distanceFilter: 10, 
      debug: true,
      interval: 2000 
    };

    this.backgroundGeolocation.configure(config).subscribe((location) => {

      console.log('BackgroundGeolocation:  ' + location.latitude + ',' + location.longitude);
      this.dateTime = new Date();
      this.save (this.dateTime + ',' + location.latitude + ','+ location.longitude);

      // Run update inside of Angular's zone
      this.zone.run(() => {
        this.lat = location.latitude;
        this.lng = location.longitude;
      });

    }, (err) => {

      console.log(err);

    });

    // Turn ON the background-geolocation system.
    this.backgroundGeolocation.start();


    // Foreground Tracking

  let options = {
    frequency: 3000, 
    enableHighAccuracy: true
  };

  this.watch = this.geolocation.watchPosition(options).filter((p: any) => p.code === undefined).subscribe((position: Geoposition) => {

    console.log(position);
    this.dateTime = new Date();
    this.save (this.dateTime+','+position.coords.latitude + ','+ position.coords.longitude);

    // Run update inside of Angular's zone
    this.zone.run(() => {
      this.lat = position.coords.latitude;
      this.lng = position.coords.longitude;
    });

  });

  }

  stopTracking(){
  	console.log('stopTracking');
    this.backgroundGeolocation.finish();
    this.watch.unsubscribe();
    this.lat = 0;
    this.lng = 0;
  }
}
