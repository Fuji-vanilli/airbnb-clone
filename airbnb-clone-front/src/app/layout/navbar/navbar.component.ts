import { Component, effect, inject, OnInit } from '@angular/core';

import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';
import { ToolbarModule } from 'primeng/toolbar';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AvatarComponent } from './avatar/avatar.component';
import { CategoryComponent } from './category/category.component';
import {DialogService} from "primeng/dynamicdialog";
import { ToastService } from '../../services/toast.service';
import { AuthService } from '../../core/auth/auth.service';
import { User } from '../../core/model/user.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    ToolbarModule,
    MenuModule,
    FontAwesomeModule,
    AvatarComponent,
    CategoryComponent
  ],
  providers: [
    DialogService
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit{

  location= 'Anywhere';
  guests= 'Add guests';
  dates= 'Add week'

  toastService= inject(ToastService);
  authService= inject(AuthService);

  currentMenuItems: MenuItem[] | undefined= [];

  private connectedUser: User= {email: this.authService.notConnected};

  login= ()=> this.authService.login();
  logout= ()=> this.authService.logout();

  constructor() {
    effect(()=> {
      if (this.authService.fetchUser().status=== "OK") {
        this.connectedUser= this.authService.fetchUser().value!;
        this.currentMenuItems= this.fetchMenu();
      }
    })
  }

  ngOnInit(): void {
    this.authService.fetch(false);
   // this.toastService.sendMessage({severity: "info", summary: "welcome back to airbnb home page"});
  }

  fetchMenu() {
    if (this.authService.isAuthenticated()) {
      return [
        {
          label: "My properties",
          RouterLink: "landlord/properties",
          visible: this.hasToBeLandlord()
        },
        {
          label: "My booking",
          RouterLink: "booking",
        },
        {
          label: "My reservation",
          RouterLink: "landlord/reservation",
          visible: this.hasToBeLandlord()
        },
        {
          label: "Log out",
          command: this.logout
        }
      ]
    } else {
      return [
        {
          label: "Sing up",
          styleClass: "font-bold",
          command: this.login
        },
        {
          label: "Log in",
          command: this.login
        }
      ]
    }
  }

  hasToBeLandlord() {
    return this.authService.hasAnyAuthority("ROLE_LANDLORD");
  }

}
