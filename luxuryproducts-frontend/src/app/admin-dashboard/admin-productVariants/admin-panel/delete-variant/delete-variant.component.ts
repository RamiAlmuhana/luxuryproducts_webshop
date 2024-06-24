import { Component } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';

import { CommonModule } from '@angular/common';
import { Product } from '../../../../models/product.model';
import { AdminService } from '../../../../services/admin.service';
import { ProductsService } from '../../../../services/products.service';
import { ProductVariant } from '../../../../models/product-variant.model';

@Component({
  selector: 'app-delete-variant',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './delete-variant.component.html',
  styleUrl: './delete-variant.component.scss',
})
export class DeleteVariantComponent {
  productForm: FormGroup;
  productSelected: boolean = false;
  productVariantSelected: boolean = false;
  productVariantindex: number;
  cantDeleteProductVariant: boolean = false;

  products: Product[];
  productnew: Product;
  productVariant: ProductVariant;
  waitForVariant: boolean = false;

  constructor(private fb: FormBuilder, private adminservice: AdminService) {
    this.productForm = this.fb.group({
      product: [null, Validators.required],
      productVariantIndex: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.adminservice
      .getProductsThatCanBeDeleted()
      .subscribe(
        (productsfromBackend) => (this.products = productsfromBackend)
      );
  }

  onProductVariants(selectedproduct: Product) {
    if (!selectedproduct || !Array.isArray(selectedproduct.productVariants)) {
      return;
    }

    if (selectedproduct.productVariants.length <= 1) {
      this.cantDeleteProductVariant = true;
    } else {
      this.cantDeleteProductVariant = false;
      if (selectedproduct != this.productnew) {
        this.productnew = selectedproduct;
        this.waitForVariant = true;
        this.productSelected = true;
      } else {
        this.productSelected = true;
      }
    }
  }

  onShowProductVariantDetails(index: number) {
    if (index == undefined) {
      this.productVariantSelected = false;
    } else {
      this.productVariant = this.productnew.productVariants[index];
      this.productVariantSelected = true;
      this.waitForVariant = false;
    }
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      console.log(this.productForm.value);
      this.adminservice
        .deleteProductVariant(this.productForm.value)
        .subscribe(() => {
          alert('Succesfully Deleted Product Variant');
          this.productForm.reset();
        });
    }
  }
}
