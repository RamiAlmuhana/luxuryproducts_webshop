import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { CartgiftcardService } from '../../services/cartgiftcard.service';

@Component({
  selector: 'app-payment-successful',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './payment-successful.component.html',
  styleUrl: './payment-successful.component.scss',
})
export class PaymentSuccessfulComponent {
  constructor(
    private cartService: CartService,
    private cartGiftcardService: CartgiftcardService
  ) {
    this.cartService.updateProductsIncart();
    this.cartGiftcardService.updateGiftCardsIncart();
  }
}
