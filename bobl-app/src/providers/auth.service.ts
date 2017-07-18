import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Storage} from "@ionic/storage";
import {Observable} from "rxjs/Observable";

@Injectable()
export class AuthService {

  user = {
    user: "",
    password: ""
  };
  /*
   user = {
   user: "113",
   password: "j5dpj4r206ln",
   };
   */
  //wait for storage to load data.
  initialized = false;

  boblCookie;

  private baseUrl = 'http://ec2-52-30-65-64.eu-west-1.compute.amazonaws.com/';

  constructor(private http: Http, private storage: Storage) {
  }

  authenticate(fn) {

    this.storage.get('user-user').then(u => {
      this.storage.get('user-password').then(pw => {
        this.user.user = u;
        this.user.password = pw;

        console.log("Pre login Current user: " + this.getCurrentUser().user + " "
          + this.getCurrentUser().password);

        if (!this.isAuthenticated()) {
          this.signup();
          console.log("signup");
        }
        console.log("Post login Current user: " + this.getCurrentUser().user + " "
          + this.getCurrentUser().password);

        this.login(fn);
        console.log("login");

      })
    })
  }

  setBoblCookie(boblCookie) {
    this.storage.set('bobl-cookie', boblCookie.cookie);
  }

  getBoblCookie() {
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
      this.initialized = true;
    });

  }

  signup() {
    this.http.post(this.baseUrl + 'signup', "")
      .subscribe(data => {
          console.log("new user: ");
          console.log(JSON.parse(data['_body']));
          this.newUser(JSON.parse(data['_body']));
        },
        error => {
          console.log(error);
        });
  }

  login(fn) {
    this.http.post(this.baseUrl + 'login', {
      user: this.user.user,
      password: this.user.password
    })
      .subscribe(data => {
          console.log(data['_body']);
          console.log('setze cookie');
          this.boblCookie = (JSON.parse(data['_body']));
          console.log(this.boblCookie);
          fn();
        },
        error => {
          console.log(error);
        });

  }

  isAuthenticated() {
    if (this.user.user != null) {
      return true
    }
    else {
      return false
    }
  }

}
