import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Product} from "../models/product.model";
import {RouterLink} from "@angular/router";
import { NgFor, NgIf } from '@angular/common';
import { CartService } from '../services/cart.service';
import { AuthService } from '../auth/auth.service';
import { GiftcardService } from '../services/giftcard.service';




@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    RouterLink,
    NgFor,
    NgIf
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  public generatedGiftCardCode: string | null = null;
  public userIsLoggedIn: boolean = false;



  constructor(private cartService: CartService, private authService: AuthService,
    private giftcardService: GiftcardService
  ) { }


  
  ngOnInit() {
    this.authService.$userIsLoggedIn.subscribe((loginState: boolean) => {
      this.userIsLoggedIn = loginState;
    });

  }


  public generateGiftCard(discountAmount: number) {
    if (this.userIsLoggedIn) {
      this.giftcardService.buyGiftCard(this.cartService.generateGiftCardCode(discountAmount), discountAmount)
        .subscribe(
          (response: { code: string | null; }) => {
            this.generatedGiftCardCode = response.code;
            confirm(`Giftcard code: ${this.generatedGiftCardCode}`);
          },
          (error: any) => {
            console.error('Error buying gift card:', error);
          }
        );
    } else {
      alert('You need to be logged in to generate a gift card.');
    }
  }

}

