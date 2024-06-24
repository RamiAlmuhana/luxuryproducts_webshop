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
import { Product } from '../../../../models/product.model';
import { AdminService } from '../../../../services/admin.service';
import { ProductsService } from '../../../../services/products.service';

@Component({
  selector: 'app-add-variant',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-variant.component.html',
  styleUrl: './add-variant.component.scss',
})
export class AddVariantComponent {
  productForm: FormGroup;

  products: Product[];
  colors = ['Black', 'White', 'Silver', 'Gold', 'Red', 'Green', 'Brown'];
  sizeAndFits = [
    'Classic_Fit',
    'Regular_Fit',
    'Relaxed_Fit',
    'Slim_Fit',
    'Loose_Fit',
    'Tailored_Fit',
  ];

  constructor(
    private fb: FormBuilder,
    private productsService: ProductsService,
    private adminservice: AdminService
  ) {
    this.productForm = this.fb.group({
      product: [null, Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      color: [null, Validators.required],
      sizeAndFit: [null, Validators.required],
      imageUrl1: ['', [Validators.required, this.productImageUrlValidator()]],
      imageUrl2: ['', [Validators.required, this.productImageUrlValidator()]],
      imageUrl3: ['', [Validators.required, this.productImageUrlValidator()]],
      productVariatieDTOS: this.fb.array([], Validators.required),
    });
  }

  ngOnInit(): void {
    this.productsService.getAllProducts().subscribe((productsfromBackend) => {
      this.products = productsfromBackend;
    });
  }

  productImageUrlValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const imageUrl = control.value;

      try {
        new URL(imageUrl);
        return null;
      } catch (_) {
        return { imageUrlIsValid: true };
      }
    };
  }

  get productVariatieDTOS(): FormArray {
    return this.productForm.get('productVariatieDTOS') as FormArray;
  }

  addVariation(): void {
    const variationForm = this.fb.group({
      size: ['', Validators.required],
      stock: [0, [Validators.required, Validators.min(0)]],
    });
    this.productVariatieDTOS.push(variationForm);
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      this.adminservice
        .createProductVariant(this.productForm.value)
        .subscribe((text) => {
          alert(text);
          this.productForm.reset();
        });
    }
  }
}
