import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { CartService } from '../services/cart.service';
import { Product } from '../models/product.model';
import { Router } from '@angular/router';
import { AuthService } from "../auth/auth.service";
import { HttpClient } from '@angular/common/http';
import { environment } from "../../environments/environment";
import { FormsModule } from "@angular/forms";

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CurrencyPipe, NgFor, NgIf, FormsModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  public products_in_cart: Product[];
  public userIsLoggedIn: boolean = false;
  public amountOfProducts: number = 0;
  public promoCode: string = '';
  public discount: number = 0;
  public promoApplied: boolean = this.checkPromoApplied();
  public appliedPromoCode: string = localStorage.getItem('promoCode') || '';
  public displayedDiscount: string = localStorage.getItem('displayedDiscount') || '0';
  public promoCodeError: boolean = false;
  public promoCodeErrorMessage: string = '';
  public orderError: boolean = false;
  public orderErrorMessage: string = '';
  private updatingDiscount: boolean = false;
  private autoDiscountManuallyRemoved: boolean = false;
  private minSpendAmount: number = 0;

  constructor(private cartService: CartService, private router: Router, private authService: AuthService, private http: HttpClient) {}

  ngOnInit() {
    this.products_in_cart = this.cartService.allProductsInCart();
    this.cartService.$productInCart.subscribe((products: Product[]) => {
      this.products_in_cart = products;
      this.amountOfProducts = products.length;
      this.checkLoginState();
      if (this.promoApplied) {
        this.discount = parseFloat(this.displayedDiscount);
        this.minSpendAmount = parseFloat(localStorage.getItem('minSpendAmount') || '0');
      }
      if (!this.updatingDiscount && !this.autoDiscountManuallyRemoved) {
        this.applyAutomaticDiscount();
      }
      this.checkPromoCodeValidity();
    });
  }

  public clearCart() {
    this.cartService.clearCart();
    this.clearPromoCodeFromLocalStorage();
    this.promoApplied = false;
    this.discount = 0;
    this.appliedPromoCode = '';
    this.promoCodeError = false;
    this.orderError = false;
    this.autoDiscountManuallyRemoved = false;
  }

  public removeProductFromCart(product_index: number) {
    const removedProduct = this.products_in_cart[product_index];
    this.cartService.removeProductFromCart(product_index);

    const hasProductInCategory = this.products_in_cart.some(product => product.categoryId === removedProduct.categoryId);

    if (!hasProductInCategory && this.appliedPromoCode) {
      this.removePromoCode();
    }

    this.applyAutomaticDiscount();
    this.checkPromoCodeValidity();
  }


  public getTotalPrice(): number {
    return this.cartService.calculateTotalPrice();
  }

  public getTotalPriceWithDiscount(): number {
    return this.cartService.totalPriceWithDiscount;
  }

  public onInvalidOrder() {
    return this.amountOfProducts === 0;
  }

  public onOrder() {
    if (!this.userIsLoggedIn) {
      this.orderError = true;
      this.orderErrorMessage = 'You need to be logged in to place an order.';
      this.router.navigateByUrl('/auth/login');
    } else {
      this.orderError = false;
      this.router.navigateByUrl('/orders');
    }
  }

  public checkLoginState(): void {
    this.authService.$userIsLoggedIn.subscribe((loginState: boolean) => {
      this.userIsLoggedIn = loginState;
    });
  }

  public applyPromoCode() {
    if (this.promoApplied) {
      this.promoCodeError = true;
      this.promoCodeErrorMessage = 'You can only use one promo code per order.';
      return;
    }

    if (this.products_in_cart.length === 0) {
      this.promoCodeError = true;
      this.promoCodeErrorMessage = 'No products found';
      return;
    }

    const url = `${environment.base_url}/promocodes/validate?code=${this.promoCode}`;
    this.http.get<{ discount: number, type: string, minSpendAmount: number, startDate: string, expiryDate: string, categoryId: number | null }>(url).subscribe({
      next: (response) => {
        const total = this.getTotalPrice();
        const now = new Date();
        const startDate = new Date(response.startDate);
        const expiryDate = new Date(response.expiryDate);

        if (now < startDate) {
          this.promoCodeError = true;
          this.promoCodeErrorMessage = `This promo code is not valid until ${startDate.toLocaleDateString()}`;
          return;
        }

        if (response.categoryId !== null) {
          const hasProductInCategory = this.products_in_cart.some(product => product.categoryId === response.categoryId);
          if (!hasProductInCategory) {
            this.promoCodeError = true;
            this.promoCodeErrorMessage = 'This promo code is not valid for any products in your cart.';
            return;
          }
        }

        if (total >= response.minSpendAmount) {
          let discount = 0;
          if (response.type === 'FIXED_AMOUNT') {
            discount = response.discount;
          } else if (response.type === 'PERCENTAGE') {
            discount = total * (response.discount / 100);
          }

          if (total - discount > 0) {
            this.discount = discount;
            this.minSpendAmount = response.minSpendAmount;
            this.cartService.applyDiscount(this.discount, response.type as 'FIXED_AMOUNT' | 'PERCENTAGE', this.promoCode);
            this.promoApplied = true;
            this.appliedPromoCode = this.promoCode;
            this.promoCodeError = false;
            this.autoDiscountManuallyRemoved = false;
            localStorage.setItem('minSpendAmount', this.minSpendAmount.toString());
          } else {
            this.promoCodeError = true;
            this.promoCodeErrorMessage = 'Promo code cannot be applied as it makes the total price zero or negative.';
          }
        } else {
          this.promoCodeError = true;
          this.promoCodeErrorMessage = `Minimum spend amount for this promo code is ${response.minSpendAmount}`;
        }
      },
      error: () => {
        this.promoCodeError = true;
        this.promoCodeErrorMessage = 'Invalid or expired promo code!';
      }
    });
  }



  public removePromoCode() {
    this.cartService.removeDiscount();
    this.clearPromoCodeFromLocalStorage();
    this.promoApplied = false;
    this.discount = 0;
    this.appliedPromoCode = '';
    this.promoCodeError = false;
    this.autoDiscountManuallyRemoved = true;
  }

  private checkPromoApplied(): boolean {
    return localStorage.getItem('promoApplied') === 'true';
  }

  private applyAutomaticDiscount() {
    this.updatingDiscount = true;
    const total = this.getTotalPrice();
    if (total > 120 && (!this.promoApplied || (this.appliedPromoCode === 'AUTO_DISCOUNT' && !this.autoDiscountManuallyRemoved))) {
      const discount = 20;
      this.cartService.applyDiscount(discount, 'FIXED_AMOUNT', 'AUTO_DISCOUNT');
      this.promoApplied = true;
      this.appliedPromoCode = 'AUTO_DISCOUNT';
      this.discount = discount;
      this.autoDiscountManuallyRemoved = false;
    } else if (total < 120 && this.promoApplied && this.appliedPromoCode === 'AUTO_DISCOUNT') {
      this.removePromoCodeWithoutReapply();
    }
    this.updatingDiscount = false;
  }

  private checkPromoCodeValidity() {
    const total = this.getTotalPrice();
    if (this.promoApplied && total < this.minSpendAmount) {
      setTimeout(() => {
        this.removePromoCode();
        this.promoCodeError = true;
        this.promoCodeErrorMessage = 'Total price is below the minimum spend amount. The promo code has been removed.';
      }, 0);
    }
  }

  private removePromoCodeWithoutReapply() {
    this.cartService.removeDiscount();
    this.clearPromoCodeFromLocalStorage();
    this.promoApplied = false;
    this.discount = 0;
    this.appliedPromoCode = '';
    this.promoCodeError = false;
    this.autoDiscountManuallyRemoved = true;
  }

  private clearPromoCodeFromLocalStorage() {
    localStorage.removeItem('promoCode');
    localStorage.removeItem('promoApplied');
    localStorage.removeItem('discountValue');
    localStorage.removeItem('discountType');
    localStorage.removeItem('displayedDiscount');
    localStorage.removeItem('minSpendAmount');
  }
}
