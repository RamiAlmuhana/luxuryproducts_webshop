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
  public clearCartBool = false;
  private productsInCart: CartProduct[] = [];
  public $totalPrice: BehaviorSubject<number> = new BehaviorSubject<number>(0);

  public $productInCart: BehaviorSubject<CartProduct[]> = new BehaviorSubject<
    CartProduct[]
  >([]);

  public totalPrice: number;
  public totalDiscount: number = 0;
  public totalPriceWithDiscount: number;

  private baseUrl: string = environment.base_url + '/orders';

  private validGiftCardCodes: { [key: string]: number } = {};

  constructor(
    private http: HttpClient,
    private cartProductService: CartproductService
  ) {}

  public reapplyDiscountIfApplicable() {
    const discountValue = parseFloat(
      localStorage.getItem('discountValue') || '0'
    );
    const discountType = localStorage.getItem('discountType') as
      | 'FIXED_AMOUNT'
      | 'PERCENTAGE'
      | null;
    const promoCode = localStorage.getItem('promoCode') || '';
    const token = localStorage.getItem('token') || undefined;

    if (discountType && discountValue && promoCode && token) {
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
    this.clearCartBool = true;
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
    this.calculateTotalPrice((totalprice) => {
      const total = totalprice;
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
    });
  }

  public removeDiscount() {
    this.calculateTotalPrice((totalPrice) => {
      this.totalPriceWithDiscount = totalPrice;
      localStorage.removeItem('promoApplied');
      localStorage.removeItem('promoCode');
      localStorage.removeItem('discountValue');
      localStorage.removeItem('discountType');
      localStorage.removeItem('displayedDiscount');
      localStorage.removeItem('minSpendAmount');
    });
  }

  public calculateTotalPrice(callback: (totalPrice: number) => void): void {
    this.cartProductService
      .getTotalPriceOfCartByUser()
      .subscribe((totalprice) => {
        callback(totalprice);
      });
  }

  notifyTotalPrice() {
    if (this.clearCartBool) {
      this.$totalPrice.next(0);
      this.clearCartBool = false;
    } else {
      this.cartProductService
        .getTotalPriceOfCartByUser()
        .subscribe((totalprice) => {
          this.$totalPrice.next(totalprice);
        });
    }
  }

  private saveProductsAndNotifyChange(cartproducts: CartProduct[]): void {
    this.productsInCart = cartproducts;
    this.notifyTotalPrice();
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
    this.calculateTotalPrice((totalprice) => {
      localStorage.removeItem(discountAmountKey);
      localStorage.removeItem(discountCodesKey);
      this.totalDiscount = 0;
      this.totalPriceWithDiscount = totalprice;
      localStorage.removeItem('promoApplied');
      localStorage.removeItem('promoCode');
      localStorage.removeItem('discountValue');
      localStorage.removeItem('discountType');
      localStorage.removeItem('displayedDiscount');
      localStorage.removeItem('minSpendAmount');
    });
  }
}
