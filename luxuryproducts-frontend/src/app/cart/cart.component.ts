import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { CartService } from '../services/cart.service';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { FormsModule } from '@angular/forms';
import { CartProduct } from '../models/cart-product.model';
import { CartproductService } from '../services/cartproduct.service';
import { Giftcard } from '../models/giftcard.model'; // Assuming you have a Giftcard model
import { CartGiftCard } from '../models/cart-gift-card.model';
import { CartgiftcardService } from '../services/cartgiftcard.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CurrencyPipe, NgFor, NgIf, FormsModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  public giftcards_in_cart: CartGiftCard[];
  public loadingTotalPrice = false;
  public products_in_cart: CartProduct[];
  public userIsLoggedIn: boolean = false;
  public amountOfProducts: number = 0;
  public promoCode: string = '';
  public discount: number = 0;
  public promoApplied: boolean = this.checkPromoApplied();
  public appliedPromoCode: string = localStorage.getItem('promoCode') || '';
  public displayedDiscount: string =
    localStorage.getItem('displayedDiscount') || '0';
  public promoCodeError: boolean = false;
  public promoCodeErrorMessage: string = '';
  public orderError: boolean = false;
  public orderErrorMessage: string = '';
  private updatingDiscount: boolean = false;
  private autoDiscountManuallyRemoved: boolean = false;
  private minSpendAmount: number = 0;
  public giftCardCode: string = '';
  public appliedDiscountCodes: string[] = [];
  public appliedDiscountAmount: number = 0;
  public appliedGiftCard: boolean = false;
  public appliedGiftCardCode: string = '';
  public giftCardDiscount: number = 0;
  public totalPrice: number;
  public newTotalPrice: number;

  constructor(
    private cartService: CartService,
    private router: Router,
    private authService: AuthService,
    private http: HttpClient,
    private cartProductService: CartproductService,
    private cartGiftcardService: CartgiftcardService
  ) {
    this.loadLoginState();

    if (this.userIsLoggedIn) {
      this.getTotalPrice();

      this.cartService.updateProductsIncart();
      this.getCartProducts();
      this.cartGiftcardService.updateGiftCardsIncart();
      this.getCartGiftCards();
      this.cartService.reapplyDiscountIfApplicable();
    }
  }

  ngOnInit() {
    if (this.promoApplied) {
      this.discount = parseFloat(this.displayedDiscount);

      this.minSpendAmount = parseFloat(
        localStorage.getItem('minSpendAmount') || '0'
      );
    } else if (!this.updatingDiscount && !this.autoDiscountManuallyRemoved) {
      this.applyAutomaticDiscount();
    }

    const { discountAmount, discountCodes } =
      this.cartService.loadDiscountFromLocalStorage();
    this.appliedDiscountAmount = discountAmount;
    this.appliedDiscountCodes = discountCodes;

    this.appliedGiftCardCode =
      localStorage.getItem('appliedGiftCardCode') || '';
    this.giftCardDiscount = parseFloat(
      localStorage.getItem('giftCardDiscount') || '0'
    );
    if (this.appliedGiftCardCode) {
      this.appliedGiftCard = true;
    }
  }

  public getTotalPrice() {
    this.cartService.$totalPrice.subscribe((totalprice) => {
      if (totalprice) {
        this.totalPrice = totalprice;
        this.checkPromoCodeValidity();
        this.getTotalPriceWithDiscount();
      }
    });
  }

  public loadLoginState() {
    this.authService.$userIsLoggedIn.subscribe((loginState: boolean) => {
      this.userIsLoggedIn = loginState;
    });
  }
  public getCartProducts() {
    this.cartService.$productInCart.subscribe((cartProducts) => {
      this.products_in_cart = cartProducts;
      this.amountOfProducts = cartProducts.length;
      this.checkLoginState();
    });
  }

  public getCartGiftCards() {
    this.cartGiftcardService.$giftCardsInCart.subscribe((cartGiftcards) => {
      this.giftcards_in_cart = cartGiftcards;
    });
  }

  public clearCartProductsInBackend() {
    this.cartProductService.deleteAll().subscribe((text) => {
      this.products_in_cart = [];
    });
  }

  public clearCartGiftcardsInbackend() {
    this.cartGiftcardService.deleteAllGiftcards();
  }

  public clearCart() {
    this.clearCartProductsInBackend();
    this.clearCartGiftcardsInbackend();
    this.cartService.clearCart();
    this.clearPromoCodeFromLocalStorage();
    this.promoApplied = false;
    this.discount = 0;
    this.appliedPromoCode = '';
    this.promoCodeError = false;
    this.orderError = false;
    this.autoDiscountManuallyRemoved = false;
    this.totalPrice = 0;
    this.clearDiscount();
    this.removeGiftCard();
    localStorage.removeItem('discountedPrice');
    localStorage.removeItem('totalPriceWithDiscount');
    localStorage.removeItem('promoCodeApplied');
    localStorage.removeItem('totalDiscount');
    localStorage.removeItem('discountValue');
  }

  public removeProductFromCart(product_index: number, categoryId: number) {
    this.products_in_cart.length + this.giftcards_in_cart.length - 1 == 0
      ? this.clearCart()
      : this.cartService.removeProductFromCart(product_index);

    const hasProductInCategory = this.products_in_cart.some(
      (product) => product.categoryId == categoryId
    );

    if (!hasProductInCategory && this.appliedPromoCode) {
      this.removePromoCode();
    }
    this.onRemoveItemValidator();
  }

  public removeGiftcardFromCart(cartGiftcardId: number) {
    this.products_in_cart.length + this.giftcards_in_cart.length - 1 == 0
      ? this.clearCart()
      : this.cartGiftcardService.deleteCartGiftCard(cartGiftcardId);
    if (this.appliedPromoCode) {
      this.removePromoCode();
    }
    this.onRemoveItemValidator();
  }

  onRemoveItemValidator() {
    this.applyAutomaticDiscount();
    this.checkPromoCodeValidity();
  }

  private clearDiscount() {
    this.appliedDiscountAmount = 0;
    this.appliedDiscountCodes = [];
    this.cartService.clearDiscountFromLocalStorage();
  }

  public getTotalPriceWithDiscount(): void {
    let total = this.totalPrice;
    total -= this.discount + this.appliedDiscountAmount;
    this.newTotalPrice = Math.max(total, 0);
  }

  public onInvalidOrder() {
    return this.products_in_cart == undefined &&
      this.giftcards_in_cart == undefined
      ? 0
      : this.products_in_cart.length + this.giftcards_in_cart.length === 0;
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

    if (this.products_in_cart.length + this.giftcards_in_cart.length === 0) {
      this.promoCodeError = true;
      this.promoCodeErrorMessage = 'No products found';
      return;
    }

    const url = `${environment.base_url}/promocodes/validate?code=${this.promoCode}`;
    this.http
      .get<{
        discount: number;
        type: string;
        minSpendAmount: number;
        startDate: string;
        expiryDate: string;
        categoryId: number | null;
      }>(url)
      .subscribe({
        next: (response) => {
          const total = this.totalPrice;
          const now = new Date();
          const startDate = new Date(response.startDate);
          const expiryDate = new Date(response.expiryDate);

          if (now < startDate) {
            this.promoCodeError = true;
            this.promoCodeErrorMessage = `This promo code is not valid until ${startDate.toLocaleDateString()}`;
            return;
          }

          if (response.categoryId !== null) {
            const hasProductInCategory = this.products_in_cart.some(
              (product) => product.categoryId === response.categoryId
            );
            if (!hasProductInCategory) {
              this.promoCodeError = true;
              this.promoCodeErrorMessage =
                'This promo code is not valid for any products in your cart.';
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
              this.cartService.applyDiscount(
                response.discount,
                response.type as 'FIXED_AMOUNT' | 'PERCENTAGE',
                this.promoCode
              );
              this.promoApplied = true;
              this.appliedPromoCode = this.promoCode;
              this.promoCodeError = false;
              this.autoDiscountManuallyRemoved = false;
              localStorage.setItem(
                'minSpendAmount',
                this.minSpendAmount.toString()
              );
              localStorage.setItem(
                'discountedPrice',
                Math.max(discount, 0).toString()
              );
              this.getTotalPriceWithDiscount();
            } else {
              this.promoCodeError = true;
              this.promoCodeErrorMessage =
                'Promo code cannot be applied as it makes the total price zero or negative.';
            }
          } else {
            this.promoCodeError = true;
            this.promoCodeErrorMessage = `Minimum spend amount for this promo code is ${response.minSpendAmount}`;
          }
        },
        error: () => {
          this.promoCodeError = true;
          this.promoCodeErrorMessage = 'Invalid or expired promo code!';
        },
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
    this.getTotalPriceWithDiscount();
  }

  private checkPromoApplied(): boolean {
    return localStorage.getItem('promoApplied') === 'true';
  }

  private applyAutomaticDiscount() {
    this.updatingDiscount = true;
    const total = this.totalPrice;
    if (
      total > 20000 &&
      (!this.promoApplied ||
        (this.appliedPromoCode === 'AUTO_DISCOUNT' &&
          !this.autoDiscountManuallyRemoved))
    ) {
      const discount = 500;
      this.cartService.applyDiscount(discount, 'FIXED_AMOUNT', 'AUTO_DISCOUNT');
      this.promoApplied = true;
      this.appliedPromoCode = 'AUTO_DISCOUNT';
      this.discount = discount;
      this.autoDiscountManuallyRemoved = false;
    } else if (
      total < 20000 &&
      this.promoApplied &&
      this.appliedPromoCode === 'AUTO_DISCOUNT'
    ) {
      this.removePromoCodeWithoutReapply();
    }
    this.updatingDiscount = false;
  }

  private checkPromoCodeValidity() {
    let total = this.totalPrice;
    if (this.promoApplied && total < this.minSpendAmount) {
      setTimeout(() => {
        this.removePromoCode();
        this.promoCodeError = true;
        this.promoCodeErrorMessage =
          'Total price is below the minimum spend amount. The promo code has been removed.';
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

  public applyGiftCard() {
    const url = `${environment.base_url}/giftcards/use`;
    this.http.post<Giftcard>(url, { code: this.giftCardCode }).subscribe({
      next: (response) => {
        if (
          response.balance > 0 &&
          !this.appliedDiscountCodes.includes(this.giftCardCode)
        ) {
          const discountAmount = Math.min(response.balance, this.totalPrice);

          this.appliedDiscountAmount += discountAmount;
          this.appliedDiscountCodes.push(this.giftCardCode);

          this.cartService.saveDiscountToLocalStorage(
            this.appliedDiscountAmount,
            this.appliedDiscountCodes
          );

          localStorage.setItem('appliedGiftCardCode', this.giftCardCode);
          localStorage.setItem('giftCardDiscount', discountAmount.toString());

          this.appliedGiftCard = true;
          this.appliedGiftCardCode = this.giftCardCode;
          this.giftCardDiscount = discountAmount;
          this.getTotalPriceWithDiscount();
          alert(`Gift card applied successfully! Discount: ${discountAmount}`);
        } else if (this.appliedDiscountCodes.includes(this.giftCardCode)) {
          alert('This gift card code has already been used');
        }
      },
      error: (err) => {
        alert('Invalid or already used gift card code');
      },
    });
  }

  public removeGiftCard() {
    localStorage.removeItem('appliedGiftCardCode');
    localStorage.removeItem('giftCardDiscount');
    this.appliedGiftCard = false;
    this.appliedGiftCardCode = '';
    this.giftCardDiscount = 0;
    this.appliedDiscountAmount = 0;
    this.appliedDiscountCodes = [];
    this.getTotalPriceWithDiscount();
    this.cartService.clearDiscountFromLocalStorage();
  }
}
