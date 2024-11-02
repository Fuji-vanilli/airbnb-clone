import { Component, inject, OnInit } from '@angular/core';
import { CategoryService } from '../../../services/category.service';
import { Category } from '../../../models/category.model';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [
    FontAwesomeModule
  ],
  templateUrl: './category.component.html',
  styleUrl: './category.component.scss'
})
export class CategoryComponent implements OnInit {

  categoryService= inject(CategoryService);

  categories: Category[] | undefined;

  currentActivateCategory= this.categoryService.getCategoryByDefault();

  ngOnInit(): void {
    this.fetchCategories()
  }

  fetchCategories() {
    this.categories= this.categoryService.getCategories();
  }

}
