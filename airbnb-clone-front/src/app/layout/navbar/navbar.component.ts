import { Component, inject, OnInit } from '@angular/core';

import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';
import { ToolbarModule } from 'primeng/toolbar';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AvatarComponent } from './avatar/avatar.component';
import { CategoryComponent } from './category/category.component';
import {DialogService} from "primeng/dynamicdialog";
import { ToastService } from '../../services/toast.service';

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

  currentMenuItems: MenuItem[] | undefined= [];

  ngOnInit(): void {
    this.fetchMenu();
    this.toastService.sendMessage({severity: "info", summary: "welcome back to airbnb home page"});
  }

  fetchMenu() {
    return [
      {
        label: "Sing up",
        styleClass: "font-bold"
      },
      {
        label: "Log in"
      }
    ]
  }

}
