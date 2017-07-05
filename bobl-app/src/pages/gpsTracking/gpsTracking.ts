import { Component, Input, OnInit} from '@angular/core';
import { NavController } from 'ionic-angular';
import { LocationTrackerProvider } from '../../providers/location-tracker/location-tracker';

@Component({
  selector: 'page-gpsTracking',
  templateUrl: 'gpsTracking.html'
})
export class GPSTrackingPage implements OnInit{
  public toggle: boolean;
  public initialized: boolean = false;

  constructor(public navCtrl: NavController, public locationTracker: LocationTrackerProvider) {
  }

start(){
	if(this.toggle){	
		this.locationTracker.startTracking();
	}
	else if(!(this.toggle && this.initialized)) {
		this.locationTracker.stopTracking();
		this.toggle = false;
	}
	else ;
};
isChecked(){
	return this.toggle;
}
  ngOnInit(){
  
  }

}
