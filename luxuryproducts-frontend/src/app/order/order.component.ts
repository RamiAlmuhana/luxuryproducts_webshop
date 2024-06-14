import { Component, OnInit } from '@angular/core';
import { CartService } from '../services/cart.service';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Product } from '../models/product.model';
import { CartProduct } from '../models/cart-product.model';
import { OrderDTO } from '../models/order-dto.model';
import { CartGiftCard } from '../models/cart-gift-card.model';
import { CartgiftcardService } from '../services/cartgiftcard.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  standalone: true,
  imports: [ReactiveFormsModule],
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  public bestelForm: FormGroup;
  public products_in_cart: CartProduct[];
  public giftcards_in_cart: CartGiftCard[];
  public order: OrderDTO;
  // public totalPrice: number;
  public promoCode: string;
  public discountedPrice: number;

  constructor(
    private cartService: CartService,
    private cartGiftcardService: CartgiftcardService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.getProductsInCart();
    this.getCartGiftCards();

    this.bestelForm = this.fb.group({
      name: ['', [Validators.required]],
      infix: [''],
      lastName: ['', [Validators.required]],
      zipCode: ['', [Validators.required]],
      houseNumber: ['', [Validators.required, Validators.maxLength(5)]],
      notes: [''],
    });
  }

  public getCartGiftCards() {
    this.cartGiftcardService.$giftCardsInCart.subscribe((cartGiftcards) => {
      this.giftcards_in_cart = cartGiftcards;
    });
  }

  public getProductsInCart() {
    this.cartService.$productInCart.subscribe((cartItems) => {
      this.products_in_cart = cartItems;
    });
  }

  public clearCart() {
    this.cartService.clearCart();
  }

  public getCartProductids() {
    var ids: number[] = [];
    this.products_in_cart.forEach((product) => {
      ids.push(product.id);
    });

    return ids;
  }

  public onSubmit() {
    const formData = this.bestelForm.value;

    this.order = {
      name: formData.name,
      infix: formData.infix,
      last_name: formData.lastName,
      zipcode: formData.zipCode,
      houseNumber: formData.houseNumber,
      notes: formData.notes,
      cartProductId: this.getCartProductids(),
      discountedPrice: JSON.parse(
        localStorage.getItem('discountedPrice') || '0'
      ),
      promoCode: localStorage.getItem('promoCode') || '',
      giftCardCode: localStorage.getItem('appliedGiftCardCode') || '',
      cartGiftcards: this.giftcards_in_cart,
    };

    this.addOrder();
  }

  public addOrder() {
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
    localStorage.removeItem('discountedPrice');
    localStorage.removeItem('totalPriceWithDiscount');
    localStorage.removeItem('promoCodeApplied');
    localStorage.removeItem('totalDiscount');
    localStorage.removeItem('discountValue');
  }

  private removeGiftCardFromLocalStorage() {
    localStorage.removeItem('appliedGiftCardCode');
    localStorage.removeItem('giftCardDiscount');
    localStorage.removeItem('discountedPrice');
    localStorage.removeItem('totalPriceWithDiscount');
    localStorage.removeItem('promoCodeApplied');
    localStorage.removeItem('totalDiscount');
    localStorage.removeItem('discountValue');
  }
}
