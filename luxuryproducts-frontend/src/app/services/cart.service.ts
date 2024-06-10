import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Order } from '../models/order.model';
import { Product } from '../models/product.model';
import { CartProduct } from '../models/cart-product.model';
import { CartproductService } from './cartproduct.service';
import { OrderDTO } from '../models/order-dto.model';

const localStorageKey: string = 'products-in-cart';
const promoAppliedKey: string = 'promoApplied';
const discountAmountKey: string = 'applied-discount-amount';
const discountCodesKey: string = 'applied-discount-codes';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private productsInCart: CartProduct[] = [];
  public $productInCart: BehaviorSubject<CartProduct[]> = new BehaviorSubject<
    CartProduct[]
  >([]);

  public totalDiscount: number = 0;
  public totalPriceWithDiscount: number = this.loadInitialDiscountedPrice();

  private baseUrl: string = environment.base_url + '/orders';

  private validGiftCardCodes: { [key: string]: number } = {};

  constructor(
    private http: HttpClient,
    private cartProductService: CartproductService
  ) {
    this.updateProductsIncart();
    // this.loadProductsFromLocalStorage(); // get products in cart
    this.reapplyDiscountIfApplicable();
  }

  private reapplyDiscountIfApplicable() {
    const discountValue = parseFloat(
      localStorage.getItem('discountValue') || '0'
    );
    const discountType = localStorage.getItem('discountType') as
      | 'FIXED_AMOUNT'
      | 'PERCENTAGE'
      | null;
    const promoCode = localStorage.getItem('promoCode') || '';

    if (discountType && discountValue && promoCode) {
      this.applyDiscount(discountValue, discountType, promoCode);
    }
  }
  public addProductToCart(productToAdd: Product) {
    this.cartProductService
      .addProductToCart(productToAdd)
      .subscribe((cartProductList) => {
        this.saveProductsAndNotifyChange(cartProductList);
      });
  }

  public removeProductFromCart(productIndex: number) {
    this.cartProductService
      .deleteProductInCart(productIndex)
      .subscribe((cartList) => {
        localStorage.removeItem(promoAppliedKey);
        this.saveProductsAndNotifyChange(cartList);
      });
  }

  public updateProductsIncart() {
    this.cartProductService.getProductsInCart().subscribe((cart) => {
      this.saveProductsAndNotifyChange(cart);
    });
  }

  public clearCart() {
    this.productsInCart = [];
    localStorage.removeItem(promoAppliedKey);
    this.saveProductsAndNotifyChange(this.productsInCart);
    this.clearDiscountFromLocalStorage();
  }

  public addOrder(order: OrderDTO): Observable<Order> {
    console.log('Ontvangen order: ' + order);

    return this.http.post<Order>(this.baseUrl, order).pipe(
      catchError((error) => {
        console.error('Error adding order:', error);
        return throwError(error);
      })
    );
  }

  public applyDiscount(
    discountValue: number,
    discountType: 'FIXED_AMOUNT' | 'PERCENTAGE',
    promoCode: string
  ) {
    const total = this.calculateTotalPrice();
    if (discountType === 'FIXED_AMOUNT') {
      this.totalDiscount = discountValue;
    } else if (discountType === 'PERCENTAGE') {
      this.totalDiscount = total * (discountValue / 100);
    }
    this.totalPriceWithDiscount = total - this.totalDiscount;
    localStorage.setItem('promoApplied', 'true');
    localStorage.setItem('promoCode', promoCode);
    localStorage.setItem('discountValue', discountValue.toString());
    localStorage.setItem('discountType', discountType);
    localStorage.setItem('displayedDiscount', this.totalDiscount.toString());
    this.$productInCart.next(this.productsInCart.slice());
    // check dit
  }

  public removeDiscount() {
    this.totalDiscount = 0;
    this.totalPriceWithDiscount = this.calculateTotalPrice();
    localStorage.removeItem('promoApplied');
    localStorage.removeItem('promoCode');
    localStorage.removeItem('discountValue');
    localStorage.removeItem('discountType');
    localStorage.removeItem('displayedDiscount');
    localStorage.removeItem('minSpendAmount');
    this.$productInCart.next(this.productsInCart.slice());
  }

  public calculateTotalPrice(): number {
    var totalPrice = 0;
    this.productsInCart.forEach((cartProduct) => {
      totalPrice += cartProduct.price;
    });
    return totalPrice;
  }

  private loadInitialDiscountedPrice(): number {
    const total = this.calculateTotalPrice();
    const discountValue = parseFloat(
      localStorage.getItem('discountValue') || '0'
    );
    const discountType = localStorage.getItem('discountType') as
      | 'FIXED_AMOUNT'
      | 'PERCENTAGE'
      | null;

    if (discountType === 'FIXED_AMOUNT') {
      return Math.max(0, total - discountValue);
    } else if (discountType === 'PERCENTAGE') {
      return Math.max(0, total - (total * discountValue) / 100);
    }
    return total;
  }

  private saveProductsAndNotifyChange(cartproducts: CartProduct[]): void {
    this.productsInCart = cartproducts;
    this.$productInCart.next(cartproducts);
  }

  public generateGiftCardCode(discountAmount: number): string {
    const code = (Math.random() + 1).toString(36).substring(7);
    this.validGiftCardCodes[code] = discountAmount;
    return code;
  }

  public getGiftCardDiscount(code: string): number {
    return this.validGiftCardCodes[code] || 0;
  }

  public saveDiscountToLocalStorage(
    discountAmount: number,
    discountCodes: string[]
  ): void {
    localStorage.setItem(discountAmountKey, discountAmount.toString());
    localStorage.setItem(discountCodesKey, JSON.stringify(discountCodes));
  }

  public loadDiscountFromLocalStorage(): {
    discountAmount: number;
    discountCodes: string[];
  } {
    const discountAmount = parseFloat(
      localStorage.getItem(discountAmountKey) || '0'
    );
    const discountCodes = JSON.parse(
      localStorage.getItem(discountCodesKey) || '[]'
    );
    return { discountAmount, discountCodes };
  }

  public clearDiscountFromLocalStorage(): void {
    localStorage.removeItem(discountAmountKey);
    localStorage.removeItem(discountCodesKey);
  }
}
