import { HttpClient, HttpParams, HttpStatusCode } from '@angular/common/http';
import { computed, inject, Injectable, signal, WritableSignal } from '@angular/core';
import { State } from './model/state.model';
import { User } from './model/user.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { Location } from '@angular/common';
import { response } from 'express';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  httpClient= inject(HttpClient);

  location= inject(Location);

  notConnected= "NOT_CONNECTED";

  private fetchUsers$: WritableSignal<State<User>> = 
    signal(State.Builder<User>().forSuccess({email: this.notConnected}));
  
  fetchUser= computed(()=> this.fetchUsers$());

  constructor() { }

  fetch(forceResync: boolean) {
    this.fetchHttpUser(forceResync).subscribe({
      next: user=> this.fetchUsers$.set(State.Builder<User>().forSuccess(user)),
      error: err=> {
        if (err.status=== HttpStatusCode.Unauthorized && this.isAuthenticated()) {
          this.fetchUsers$.set(State.Builder<User>().forError(err));
        }
      }
    });
  }

  isAuthenticated() : boolean{
    if (this.fetchUsers$().value) {
      return this.fetchUsers$().value?.email!== this.notConnected;
    } else {
      return false;
    }
  }

  fetchHttpUser(forceResync: boolean): Observable<User> {
    const params= new HttpParams().set('forceResync', forceResync)
    return this.httpClient.get<User>(`${environment.API_URL}/auth/get-authenticated-user`, {params});
  }

  login() {
    location.href= `${location.origin}${this.location.prepareExternalUrl("oauth2/authorization/okta")}`;
  }

  logout() {
    this.httpClient.post(`${environment.API_URL}/auth/logout`, {}).subscribe({
      next: (response: any)=> {
        this.fetchUsers$.set(State.Builder<User>().forSuccess({email: this.notConnected}))
        location.href= response.logoutUrl;
      }
    })
  }
}
