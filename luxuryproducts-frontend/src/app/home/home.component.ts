import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgFor, NgIf } from '@angular/common';
import { CartService } from '../services/cart.service';
import { AuthService } from '../auth/auth.service';
import { GiftcardService } from '../services/giftcard.service';
import { Category } from '../models/category.model';
import { CategoryService } from '../services/category.service';
import { CartgiftcardService } from '../services/cartgiftcard.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, NgFor, NgIf],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  public generatedGiftCardCode: string | null = null;
  public userIsLoggedIn: boolean = false;
  loading: boolean = true;
  categorys!: Category[];

  constructor(
    private cartService: CartService,
    private authService: AuthService,
    private cartGiftCardService: CartgiftcardService,
    private categoryService: CategoryService
  ) {}

  ngOnInit() {
    this.loadLoginState();
    this.loadCategorys();
  }

  public loadLoginState() {
    this.authService.$userIsLoggedIn.subscribe((loginState: boolean) => {
      this.userIsLoggedIn = loginState;
    });
  }
  public loadCategorys() {
    this.categoryService.getCategorys().subscribe((allCategories) => {
      this.categorys = allCategories;
      this.loading = false;
    });
  }

  public generateGiftCard(discountAmount: number, imageUrl: string) {
    if (this.userIsLoggedIn) {
      this.cartGiftCardService.addGiftCardtoCart(
        this.cartService.generateGiftCardCode(discountAmount),
        discountAmount,
        imageUrl
      );
    } else {
      alert('You need to be logged in to generate a gift card.');
    }
  }
}
