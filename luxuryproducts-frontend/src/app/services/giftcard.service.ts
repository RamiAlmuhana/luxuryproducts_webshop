import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CartGiftCard } from '../models/cart-gift-card.model';

@Injectable({
  providedIn: 'root',
})
export class GiftcardService {
  private baseUrl: string = environment.base_url + '/giftcards';
  private baseUrl2: string = environment.base_url + '/cartGiftCard';

  constructor(private http: HttpClient) {}

  public addGiftcardToCart(
    giftCardCode: string,
    discountAmount: number,
    imageUrl: string
  ): Observable<CartGiftCard[]> {
    return this.http.post<CartGiftCard[]>(this.baseUrl2, {
      code: giftCardCode,
      discountAmount: discountAmount,
      imageUrl: imageUrl,
    });
  }

  public getAllCartGiftCards(): Observable<CartGiftCard[]> {
    return this.http.get<CartGiftCard[]>(this.baseUrl2);
  }

  deleteCartGiftCard(id: number): Observable<CartGiftCard[]> {
    return this.http.delete<CartGiftCard[]>(`${this.baseUrl2}/${id}`);
  }

  deleteAll() {
    return this.http.delete(`${this.baseUrl2}`, {
      responseType: 'text',
    });
  }
}
