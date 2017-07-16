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

  private baseUrl = 'http://ec2-52-30-65-64.eu-west-1.compute.amazonaws.com/';
  //private baseUrl = 'http://localhost:3000/';

  constructor(private http: Http, private authService: AuthService) {
    this.demands = [];
  }

  getDemands(): Observable<Demand[]> {

    //test cookie
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('bobl-cookie', this.authService.getBoblCookie());
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
    headers.append('bobl-cookie', 'session=eyJ1c2VyIjoiODMiLCJ0b2tlbiI6IjVhYTM0YjNkOGQzMjc4ODdiMDFmZjc4NjllNTgzOGM3Mjg5NTljYmY5NGY1ZmM1ZmYxNjUxYzY4Mjk4NmJkYTMiLCJleHBpcmF0aW9uRGF0ZSI6IjIwMTctMDgtMTVUMTk6NDg6MDUuMzM5In0=');
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

  private extractData(res: Response) {
    let body = res.json();
    return body || {};
  }
}
