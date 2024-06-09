import { Component, OnInit } from '@angular/core';
import { CartService } from "../services/cart.service";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { Product } from '../models/product.model';
import { Order } from '../models/order.model';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  public bestelForm: FormGroup;
  public products_in_cart: Product[];
  public order: Order;
  public totalPrice: number;
  public promoCode: string;
  public discountedPrice: number;

  constructor(private cartService: CartService, private router: Router, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.products_in_cart = this.cartService.allProductsInCart();
    this.bestelForm = this.fb.group({
      name: ['', [Validators.required]],
      infix: [''],
      lastName: ['', [Validators.required]],
      zipCode: ['', [Validators.required]],
      houseNumber: ['', [Validators.required, Validators.maxLength(5)]],
      notes: ['']
    });
  }

  public clearCart() {
    this.cartService.clearCart();
  }

  public onSubmit() {
    const formData = this.bestelForm.value;

    this.order = {
      id: formData.id,
      name: formData.name,
      infix: formData.infix,
      last_name: formData.lastName,
      zipcode: formData.zipCode,
      houseNumber: formData.houseNumber,
      notes: formData.notes,
      orderDate: new Date().toISOString(),
      products: this.products_in_cart,
      totalPrice: this.totalPrice,
      // discountedPrice: this.discountedPrice,
      discountedPrice: JSON.parse(localStorage.getItem('discountedPrice') || ''),
      promoCode: localStorage.getItem('promoCode') || '',
      giftCardCode: localStorage.getItem('appliedGiftCardCode') || ''
    };

    this.cartService.addOrder(this.order).subscribe(
      (result) => {
        console.log('Order added successfully:', result);
        this.clearCart();
        this.removePromoCodeFromLocalStorage();
        this.removeGiftCardFromLocalStorage();
        this.router.navigateByUrl('/paymentsuccessful');
      },
      (error) => {
        console.error('Failed to add order:', error);
      }
    );
  }

  private removePromoCodeFromLocalStorage() {
    localStorage.removeItem('promoCode');
    localStorage.removeItem('promoApplied');
    localStorage.removeItem('applied-discount-amount');
    localStorage.removeItem('discountType');
    localStorage.removeItem('displayedDiscount');
    localStorage.removeItem("discountedPrice")
    localStorage.removeItem("totalPriceWithDiscount")
    localStorage.removeItem("promoCodeApplied")
    localStorage.removeItem("totalDiscount")
    localStorage.removeItem("discountValue")
  }

  private removeGiftCardFromLocalStorage() {
    localStorage.removeItem('appliedGiftCardCode');
    localStorage.removeItem('giftCardDiscount');
    localStorage.removeItem("discountedPrice")
    localStorage.removeItem("totalPriceWithDiscount")
    localStorage.removeItem("promoCodeApplied")
    localStorage.removeItem("totalDiscount")
    localStorage.removeItem("discountValue")
  }
}
