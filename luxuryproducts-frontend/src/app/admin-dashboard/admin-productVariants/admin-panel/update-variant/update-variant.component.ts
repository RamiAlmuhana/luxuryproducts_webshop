import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import { ProductDetailshowComponent } from '../product-detailshow/product-detailshow.component';
import { ProductVariant } from '../../../../models/product-variant.model';
import { Product } from '../../../../models/product.model';
import { AdminService } from '../../../../services/admin.service';
import { ProductsService } from '../../../../services/products.service';

@Component({
  selector: 'app-update-variant',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ProductDetailshowComponent],
  templateUrl: './update-variant.component.html',
  styleUrls: ['./update-variant.component.scss'],
})
export class UpdateVariantComponent implements OnInit {
  productForm: FormGroup;
  products: Product[] = []; // List of products for the select option
  selectedProduct: Product | null = null; // Currently selected product
  productVariants: ProductVariant[] = []; // List of variants for the selected product
  sizes = ['XS', 'S', 'M', 'L', 'XL', 'XXL'];
  colors = ['Black', 'White', 'Silver', 'Gold', 'Red', 'Green', 'Brown'];
  sizeAndFits = [
    'Classic_Fit',
    'Regular_Fit',
    'Relaxed_Fit',
    'Slim_Fit',
    'Loose_Fit',
    'Tailored_Fit',
  ];
  existingProductVariationsCount = 0;

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private productsService: ProductsService
  ) {
    this.productForm = this.fb.group({
      product: [null, Validators.required],
      productVariantIndex: [null, Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      color: ['', Validators.required],
      sizeAndFit: ['', Validators.required],
      imageUrl1: ['', Validators.required],
      imageUrl2: ['', Validators.required],
      productVariatieDTOS: this.fb.array([]),
    });
  }

  ngOnInit(): void {
    // Fetch products from the backend
    this.productsService.getProducts().subscribe((products) => {
      this.products = products;
    });
  }

  onProductChange(product: Product): void {
    if (product) {
      this.selectedProduct = product;
      this.productVariants = product.productVariants;

      // Clear previous values
      this.productForm.get('productVariantIndex')!.reset();
      this.productForm.get('description')!.reset();
      this.productForm.get('price')!.reset();
      this.productForm.get('color')!.reset();
      this.productForm.get('sizeAndFit')!.reset();
      this.productForm.get('imageUrl1')!.reset();
      this.productForm.get('imageUrl2')!.reset();
      this.productVariatieDTOsFormArray.clear();
    }
  }

  onVariantChange(variantIndex: number): void {
    const variant = this.productVariants[variantIndex];
    if (variant) {
      this.productForm.patchValue({
        description: variant.description,
        price: variant.price,
        color: variant.color.name,
        sizeAndFit: variant.size_and_fit.fit,
        imageUrl1: variant.productImages[0].imageUrl,
        imageUrl2: variant.productImages[1].imageUrl,
      });

      // Reset productVariatieDTOS
      const productVariatieDTOsFormArray = this.productForm.get(
        'productVariatieDTOS'
      ) as FormArray;
      productVariatieDTOsFormArray.clear();
      this.existingProductVariationsCount = variant.productVariatie.length;
      variant.productVariatie.forEach((dto) => {
        productVariatieDTOsFormArray.push(
          this.fb.group({
            size: [
              { value: dto.size.size, disabled: true },
              Validators.required,
            ],
            stock: [
              dto.quantity_in_stock,
              [Validators.required, Validators.min(0)],
            ],
          })
        );
      });
    }
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      console.log(this.productForm.getRawValue());
      this.adminService
        .updateProductVariant(this.productForm.getRawValue())
        .subscribe((text) => {
          alert(text);
          this.productForm.reset();
        });
    }
  }

  get productVariatieDTOsFormArray() {
    return this.productForm.get('productVariatieDTOS') as FormArray;
  }

  addProductVariatieDTO() {
    this.productVariatieDTOsFormArray.push(
      this.fb.group({
        size: ['', Validators.required],
        stock: [0, [Validators.required, Validators.min(0)]],
      })
    );
  }

  removeProductVariatieDTO(index: number) {
    if (index >= this.existingProductVariationsCount) {
      this.productVariatieDTOsFormArray.removeAt(index);
    }
  }
}
