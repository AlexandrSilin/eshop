import {Injectable} from '@angular/core';
import {Credentials} from "../model/credentials";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUser?: Credentials;

  constructor(private http: HttpClient) {
    let data = localStorage.getItem('current_user');
    if (data) {
      this.currentUser = JSON.parse(data);
    }
  }

  authenticate(credentials: Credentials) {
    const headers = new HttpHeaders(credentials ? {
      authorization: 'Basic' + btoa(credentials.username + ':' + credentials.password)
    } : {});

    return this.http.get('/api/v1/login', {headers: headers})
      .pipe(map(response => {
          if ('username' in response) {
            this.currentUser = response as Credentials;
            localStorage.setItem('current_user', JSON.stringify(response));
            return response;
          }
          throw new Error('Authentication error');
        })
      )
  }

  logout() {
    if (this.isAuthenticated()) {
      this.currentUser = undefined;
      localStorage.removeItem('current_user');
      this.http.post('/api/v1/logout', {}).subscribe();
    }
  }

  public isAuthenticated() {
    return !!this.currentUser;
  }
}
