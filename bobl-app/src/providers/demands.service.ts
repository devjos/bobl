import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/delay';

import {Demand} from '../models/demand.model';
import {AuthService} from "./auth.service";

@Injectable()
export class DemandsService {
  demands: Demand[];

  dummyDemand = {
      "id": null,
      "title": "Erstellen Sie Ihren ersten Bedarf",
      "source": "",
      "sourceLatitude": null,
      "sourceLongitude": null,
      "destination": "",
      "destinationLatitude": null,
      "destinationLongitude": null,
      //"type" : "permanent",
      "outboundTime": "",
      "waybackTime": "",
      "weekdays": [0, 0, 0, 0, 0, 0, 0]
    };
  dummyDemands: Array<Demand> = [this.dummyDemand];

  options;

  private baseUrl = 'http://ec2-52-30-65-64.eu-west-1.compute.amazonaws.com/';
  //private baseUrl = 'http://localhost:3000/';

  constructor(private http: Http, private authService: AuthService) {
    this.demands = [];
  }

  setRequestOptions() {
    let headers = new Headers();
    headers.append("Accept", 'application/json');
    headers.append('x-bobl-cookie', this.authService.getBoblCookie().cookie);
    this.options = new RequestOptions({headers: headers});
  }

  getDemands(): Observable<Demand[]> {
    this.setRequestOptions();
    return this.http.get(this.baseUrl + 'demand', this.options).map(this.extractData);
  }

  getDemand(id): Observable<Demand> {
    return this.http
      .get(this.baseUrl + 'demand')
      .map(this.extractData)
      .map((demands: Demand[]) => {
        for (let demand of demands) {
          if (demand.id === id) {
            return demand;
          }
        }
      });
  }

  create(newDemand: Demand) {
    this.setRequestOptions();
    if (newDemand.id !== null) {
      console.log("requesting deletion of old demand...");
      this.delete(newDemand.id);
    }
    console.log("new Demand: ");
    console.log(newDemand);

    this.http.post(this.baseUrl + 'demand', newDemand, this.options)
      .subscribe(data => {
          console.log(data['_body']);
        },
        error => {
          console.log(error);
        });
  }

  delete(id) {
    this.setRequestOptions();
    this.http.delete(this.baseUrl + 'demand/' + id, this.options)
      .subscribe(data => {
          console.log(data['_body']);
        },
        error => {
          console.log(error);
        });
  }

  getDummyDemand(): Demand[] {
    return this.dummyDemands;
  }

  private extractData(res: Response) {
    let body = res.json();
    console.log(body);

    this.demands = body.demands;
    return this.demands;
  }
}
