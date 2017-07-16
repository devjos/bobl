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

  private extractData(res: Response) {
    let body = res.json();
    return body || {};
  }

  getDemands(): Observable<Demand[]> {

    //test cookie
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    //headers.append('Authorization','Basic ' + btoa('83'+':'+'ler5a34r1nuh'));
    //headers.append('Cookie', '');
    let options = new RequestOptions({headers: headers});
    //options.withCredentials = true;

    console.log(this.baseUrl+'demand');

    return this.http
      .get(this.baseUrl + 'demand', options)
      .delay(1000)
      .map(this.extractData);
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

    let headers = new Headers();
    //headers.append("Accept", 'application/json');
    //headers.append('Cookie', 'session=value');
    let options = new RequestOptions({headers: headers});


    console.log(newDemand);
    console.log("an:" + this.baseUrl + 'demand');

    this.http.post(this.baseUrl + 'demand', newDemand, options)
      .subscribe(data => {
        console.log(data['_body']);
      },
      error => {
        console.log(error);
      });
  }

  constructor(private http: Http) {
    this.demands = [];
  }
}
