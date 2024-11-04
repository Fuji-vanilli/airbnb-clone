import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal, WritableSignal } from '@angular/core';
import { State } from './model/state.model';
import { User } from './model/user.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

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
    this.fetchHttpUser(forceResync);
  }

  fetchHttpUser(forceResync: boolean): Observable<User> {
    return this.httpClient.get<User>(`${environment.API_URL}/auth/get-authenticated-user`);
  }
}
