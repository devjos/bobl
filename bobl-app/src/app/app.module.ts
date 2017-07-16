import { NgModule, ErrorHandler } from '@angular/core';
import { HttpModule } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';
import { IonicStorageModule } from '@ionic/storage';
import { File } from '@ionic-native/file';

import { AddPage } from '../pages/add/add';
import { GPSTrackingPage } from '../pages/gpsTracking/gpsTracking';
import { HomePage } from '../pages/home/home';
import { TabsPage } from '../pages/tabs/tabs';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { DemandsService } from '../providers/demands.service';
import {AuthService} from "../providers/auth.service";
import { LocationTrackerProvider } from '../providers/location-tracker/location-tracker';
import { BackgroundGeolocation} from '@ionic-native/background-geolocation';
import { Geolocation } from '@ionic-native/geolocation'
import { NativeGeocoder, NativeGeocoderForwardResult } from '@ionic-native/native-geocoder';


@NgModule({
  declarations: [
    MyApp,
    AddPage,
    GPSTrackingPage,
    HomePage,
    TabsPage
  ],
  imports: [
    BrowserModule,
    HttpModule,
    IonicModule.forRoot(MyApp),
    IonicStorageModule.forRoot()
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    AddPage,
    GPSTrackingPage,
    HomePage,
    TabsPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    DemandsService,
    AuthService,

    {provide: ErrorHandler, useClass: IonicErrorHandler},
    LocationTrackerProvider,
    BackgroundGeolocation,
<<<<<<< HEAD
<<<<<<< HEAD
    Geolocation
=======
    Geolocation,
    File
>>>>>>> gps
=======
    Geolocation,
    NativeGeocoder
>>>>>>> 31bdac6095bee52ea0332a9c7d039a179c3b1717
  ]
})
export class AppModule {}
