import {Inject, Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/delay';

import {Demand} from '../models/demand.model';

@Injectable()
export class DemandsService {

  demands: Demand[];

  private baseUrl = 'http://ec2-52-30-65-64.eu-west-1.compute.amazonaws.com/';
  //private baseUrl = 'http://localhost:3000/';

  getDemands(): Observable<Demand[]> {
    return this.http
      .get(this.baseUrl + 'demands')
      .delay(1000)
      .map(this.extractData);
  }

  getDemand(id): Observable<Demand> {
    return this.http
      .get(this.baseUrl + 'demands')
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

    let headers = new Headers();
    //headers.append("Accept", 'application/json');
    //headers.append('Content-Type', 'application/json');
    //headers.append('Cookie', 'session=value');
    let options = new RequestOptions({headers: headers});


    console.log(newDemand);
    console.log("an:" + this.baseUrl + 'demands');

    this.http.post(this.baseUrl + 'demands', newDemand, options)
      .subscribe(data => {
        console.log(data['_body']);
      },
      error => {
        console.log(error);
      });
  }

  private extractData(res: Response) {
    let body = res.json();
    return body || {};
  }

  constructor(private http: Http) {
    this.demands = [];
  }
}
