import {Injectable} from "@angular/core";
import {Http, Response, Headers, RequestOptions} from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class AuthService {

  headers;
  options;

  //username is an ID assigned by the server.
  user = {
    user: "",
    password: ""
  };

  private baseUrl = 'http://ec2-52-30-65-64.eu-west-1.compute.amazonaws.com/';

  constructor(private http: Http) {
    this.headers = new Headers();
    //this.headers.append('Authorization','Basic' + btoa('username:password'));
    //console.log("options: ");
    //console.log(this.options);
  }

  newUser(newUserObject) {
    this.user.user = newUserObject.user;
    this.user.password = newUserObject.password;
    console.log("user created: ");
    console.log(this.user);
  }

  getCurrentUser() {
    return this.user;
  }

  signup() {
    this.options = new RequestOptions({headers: this.headers});
    this.http.post(this.baseUrl + 'signup', "", this.options)
      .subscribe(data => {
          console.log(JSON.parse(data['_body']));
          this.newUser(JSON.parse(data['_body']));
        },
        error => {
          console.log(error);
        });
  }

  login() {
    //  let credentials = this.user.user + this.user.password;
    //  this.headers.append('Authorization', 'Basic' + btoa(credentials));
    //  this.options = new RequestOptions({headers: this.headers});
    //  console.log(this.options);

    this.http.post(this.baseUrl + 'login', {
      user: this.user.user,
      password: this.user.password
    })
      .subscribe(data => {
        //TODO: Check why it does not log the response into console.
          console.log(data['_body']);
        },
        error => {
          console.log(error);
        });

  }

}
