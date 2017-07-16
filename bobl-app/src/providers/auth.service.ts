import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Storage} from "@ionic/storage";

@Injectable()
export class AuthService {

  user = {
    user: "106",
    password: "peegsralgplj"
  };

  boblCookie: "session=eyJ1c2VyIjoiODMiLCJ0b2tlbiI6IjVhYTM0YjNkOGQzMjc4ODdiMDFmZjc4NjllNTgzOGM3Mjg5NTljYmY5NGY1ZmM1ZmYxNjUxYzY4Mjk4NmJkYTMiLCJleHBpcmF0aW9uRGF0ZSI6IjIwMTctMDgtMTVUMTk6NDg6MDUuMzM5In0=";

  private baseUrl = 'http://ec2-52-30-65-64.eu-west-1.compute.amazonaws.com/';

  constructor(private http: Http, private storage: Storage) {
    //this.headers = new Headers();
    //this.headers.append('Content-Type', 'application/json');
    //this.headers.append('Authorization','Basic' + btoa('username:password'));
    //console.log("options: ");
    //console.log(this.options);
    this.loadUser();
  }

  setBoblCookie(boblCookie: string) {
    this.storage.set('bobl-cookie', boblCookie);
  }

  getBoblCookie() {
    this.storage.get('bobl-cookie').then(value => {
      this.boblCookie = value;
    });
    return this.boblCookie;
  }

  deleteUser() {
    this.storage.set('user-user', '');
    this.storage.set('user-password', '');
  }

  newUser(newUserObject) {
    this.user.user = newUserObject.user;
    this.user.password = newUserObject.password;
    console.log("saving...");

    this.storage.set('user-user', this.user.user);
    this.storage.set('user-password', this.user.password);

    console.log("user created: ");
    console.log(this.user);
  }

  getCurrentUser() {
    return this.user;
  }

  loadUser() {
    this.storage.get('user-user').then(value => {
      console.log("user-user: " + value);
      this.user.user = value;
      console.log("user.user: " + this.user.user);
    });
    this.storage.get('user-password').then(value => {
      console.log("user-password: " + value);
      this.user.password = value;
      console.log("user.password: " + this.user.password);
    });
  }

  signup() {
    this.http.post(this.baseUrl + 'signup', "")
      .subscribe(data => {
          console.log(JSON.parse(data['_body']));
          this.newUser(JSON.parse(data['_body']));
        },
        error => {
          console.log(error);
        });
  }

  login() {
    this.http.post(this.baseUrl + 'login', {
      user: this.user.user,
      password: this.user.password
    })
      .subscribe(data => {
          console.log(data['_body']);

          console.log("bobl-Cookie: " + data.headers.get('bobl-cookie'));
          this.setBoblCookie(data.headers.get('bobl-cookie'));
        },
        error => {
          console.log(error);
        });

  }

  isAuthenticated() {
    if (this.user.user !== "") {
      return true
    }
    else {
      return false
    }
  }

}
