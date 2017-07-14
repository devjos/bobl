import {Injectable} from "@angular/core";
import {Http, Response, Headers, RequestOptions} from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class AuthService {

  headers;
  options;

  //username is an ID assigned by the server.
  user = {
    username: "",
    password: ""
  };

  private baseUrl = 'http://ec2-52-30-65-64.eu-west-1.compute.amazonaws.com/';

  constructor(private http:Http) {
    this.headers = new Headers();
    //this.headers.append('Authorization','Basic' + btoa('username:password'));
    console.log("options: ");
    console.log(this.options);
  }

  newUser(username, password) {
    this.user.username = username;
    this.user.password = password;
  }

  getCurrentUser() {
    return this.user;
  }

  signup(){
    this.options = new RequestOptions({headers: this.headers});
    this.http.post(this.baseUrl + 'signup', "", this.options)
      .subscribe(data => {
          console.log(data['_body']);
          this.newUser(data['user'], data['password']);
        },
        error => {
          console.log(error);
        });
  }


  login(){
    let credentials = this.user.username + this.user.password;
    this.headers.append('Authorization','Basic' + btoa(credentials));
    this.options = new RequestOptions({headers: this.headers});
    this.http.post(this.baseUrl + 'login', "", )
  }
}
