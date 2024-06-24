import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ProductVariant } from '../../../../models/product-variant.model';
import { Product } from '../../../../models/product.model';
import { AdminService } from '../../../../services/admin.service';
import { ProductsService } from '../../../../services/products.service';

@Component({
  selector: 'app-add-stock',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add-stock.component.html',
  styleUrl: './add-stock.component.scss',
})
export class AddStockComponent {
  productForm: FormGroup;
  productSelected: boolean = false;
  productVariantSelected: boolean = false;
  productVariantindex: number;
  cantDeleteProductVariant: boolean = false;

  products: Product[];
  productnew: Product;
  productVariant: ProductVariant;
  waitForVariant: boolean = false;
  productionStopValues = [true, false];

  constructor(
    private fb: FormBuilder,
    private productsService: ProductsService,
    private adminservice: AdminService
  ) {
    this.productForm = this.fb.group({
      product: [null, Validators.required],
      productVariantIndex: [null, Validators.required],
      productVariatieIndex: [null, Validators.required],
      stock: [50, [Validators.required, Validators.min(0)]],
    });
  }

  productionStopValueMetod(productionStopValue: boolean) {
    return productionStopValue ? 'Stop Production' : 'Continue Production';
  }

  ngOnInit(): void {
    this.productsService.getProducts().subscribe((productsFromBackend) => {
      this.products = productsFromBackend;
    });
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
      this.adminservice
        .changeStock(this.productForm.value)
        .subscribe((text) => {
          alert(text);
          this.productForm.reset();
        });
    }
  }
}
