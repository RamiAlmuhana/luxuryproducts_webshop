import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Category } from '../models/category.model';
import { Product } from '../models/product.model';
import { ProductThumbnailComponent } from '../products/product-thumbnail/product-thumbnail.component';
import { CategoryService } from '../services/category.service';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [ProductThumbnailComponent],
  templateUrl: './category.component.html',
  styleUrl: './category.component.scss',
})
export class CategoryComponent implements OnInit {
  onBuyProduct(_t3: Product) {
    throw new Error('Method not implemented.');
  }

  categoryid: number;
  loadingProducts: boolean = true;
  category: Category;

  constructor(
    private activatedRoute: ActivatedRoute,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params) => {
      this.categoryid = params['id'];
    });

    this.categoryService
      .getCategorybyId(this.categoryid)
      .subscribe((category1) => {
        this.category = category1;

        this.loadingProducts = false;
        console.log(this.category.product);
      });
  }
}
