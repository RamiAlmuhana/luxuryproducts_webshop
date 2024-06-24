import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';

import { Route, Router } from '@angular/router';
import { AdminService } from '../../../../services/admin.service';
import { ProductsService } from '../../../../services/products.service';

@Component({
  selector: 'app-new-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './new-product.component.html',
  styleUrl: './new-product.component.scss',
})
export class NewProductComponent {
  productForm: FormGroup;

  categories = ['Watches', 'Jewelry', 'Bags', 'Clothes'];
  brands = [
    'Cartier',
    'Vacheron Constantin',
    'Tiffany & Co',
    'Graff',
    'Chanel',
    'Hermes',
    'Gucci',
  ];

  constructor(
    private fb: FormBuilder,
    private productsService: ProductsService,
    private adminservice: AdminService,
    private route: Router
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      country: ['', Validators.required],
      brandId: [1, Validators.required],
      categoryId: [1, Validators.required],
    });
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      this.adminservice
        .createProduct(this.productForm.value)
        .subscribe((text) => {
          alert(text);
          this.productForm.reset();
          alert(
            'To display the product on the webshop. Please create a productVariant'
          );
          this.route.navigate(['admin/add-variant']);
        });
    }
  }
}
