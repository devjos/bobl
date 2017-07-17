import { Component, Input, OnInit} from '@angular/core';
import { NavController } from 'ionic-angular';
import { LocationTrackerProvider } from '../../providers/location-tracker/location-tracker';
import { Storage } from '@ionic/storage';
import { File } from '@ionic-native/file';
declare var cordova: any;

@Component({
  selector: 'page-gpsTracking',
  templateUrl: 'gpsTracking.html'
})
export class GPSTrackingPage implements OnInit{
  public toggle: boolean;
  public initialized: boolean = false;
  public loc: string = '';

  constructor(public navCtrl: NavController, public locationTracker: LocationTrackerProvider, private storage: Storage, private file: File) {
    this.storage.get('toggle').then((data) => {
      this.toggle = data;
      console.log(data);
    });
  this.storage.get('initialized').then((data) => {
      this.initialized = data;
      console.log(data);
    });
  }
write(){
	this.locationTracker.storage.get('location').then((data) =>{
	this.loc = data.toString();
	console.log(data);
	});

	this.file.listDir(this.file.applicationStorageDirectory, 'myFolder').then(
  (files) => {
    if(files == null){
    	this.file.writeFile(this.file.applicationStorageDirectory + '/myFolder', 'location.txt', this.loc, true);
    }
    else{
      	this.file.writeExistingFile(this.file.applicationStorageDirectory + '/myFolder', 'location.txt', this.loc);
    }
  }
).catch(
  (err) => {
    console.log(err);
  }
);
}
start(){
	if(this.toggle){	
		this.locationTracker.startTracking();
		this.storage.set('toggle', true);
		this.storage.set('initialized',true);
		this.initialized = true;
	}
	else if(this.initialized) {
		this.locationTracker.stopTracking();
		this.write();
		this.storage.set('toggle', false);
		this.storage.set('initialized',false);
		this.initialized = false;
	}
	else ;
};
isChecked(){
	return this.toggle;
}
  ngOnInit(){
  
  }

}
