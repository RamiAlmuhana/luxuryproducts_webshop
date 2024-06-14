import { Injectable } from '@angular/core';
import { GiftcardService } from './giftcard.service';
import { CartGiftCard } from '../models/cart-gift-card.model';
import { BehaviorSubject } from 'rxjs';
import { CartService } from './cart.service';

@Injectable({
  providedIn: 'root',
})
export class CartgiftcardService {
  public $giftCardsInCart: BehaviorSubject<CartGiftCard[]> =
    new BehaviorSubject<CartGiftCard[]>([]);

  constructor(
    private giftcardService: GiftcardService,
    private cartService: CartService
  ) {}

  public addGiftCardtoCart(
    giftCardCode: string,
    discountAmount: number,
    imageUrl: string
  ) {
    this.giftcardService
      .addGiftcardToCart(giftCardCode, discountAmount, imageUrl)
      .subscribe((giftcardsInCart) => {
        this.saveGiftCardsAndNotifyChange(giftcardsInCart);
      });
  }

  public updateGiftCardsIncart() {
    this.giftcardService.getAllCartGiftCards().subscribe((giftcardsInCart) => {
      this.saveGiftCardsAndNotifyChange(giftcardsInCart);
    });
  }

  public deleteCartGiftCard(id: number) {
    this.giftcardService.deleteCartGiftCard(id).subscribe((giftcardsInCart) => {
      this.saveGiftCardsAndNotifyChange(giftcardsInCart);
    });
  }

  public deleteAllGiftcards() {
    this.giftcardService.deleteAll().subscribe((text) => {
      console.log(text);
      this.updateGiftCardsIncart();
    });
  }

  private saveGiftCardsAndNotifyChange(cartGiftcards: CartGiftCard[]): void {
    this.cartService.notifyTotalPrice();
    this.$giftCardsInCart.next(cartGiftcards);
  }
}
