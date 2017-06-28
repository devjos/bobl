import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/delay';

import {Demand} from '../models/demand.model';

@Injectable()
export class DemandsService {

  constructor(private http: Http) {}

  private demandsUrl = 'assets/demands.json';

  getDemands(): Observable<Demand[]> {
    return this.http
      .get('assets/demands.json')
      .delay(1000)
      .map((res: Response) => res.json());
  }

  getDemand(id): Observable<Demand> {
    return this.http
      .get('assets/demands.json')
      .map((res: Response) => res.json())
      .map((demands: Demand[]) => {
        for (let demand of demands) {
          if (demand.id === id) {
            return demand;
          }
        }
      });
  }

  create(newDemand: Demand): Observable<Demand> {
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    console.log(newDemand);
    return this.http.post(this.demandsUrl, {newDemand}, options)
      .map(this.extractData);
  }

  private extractData(res: Response) {
    let body = res.json();
    return body.data || { };
  }
}
