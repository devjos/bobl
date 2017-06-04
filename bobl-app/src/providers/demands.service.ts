import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/delay';

import {Demand} from '../models/demand.model';

@Injectable()
export class DemandsService {

  constructor(private http: Http) {}

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
}
