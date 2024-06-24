import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { Product } from '../../models/product.model';
import { User } from '../../models/user.model';
import { CartProduct } from '../../models/cart-product.model';
import { CartgiftcardService } from '../../services/cartgiftcard.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  public userIsLoggedIn: boolean = false;
  public isDropdownOpen: boolean = false;
  public amountOfItemsInCart: number = 0;
  public amountOfProducts: number = 0;
  public ammountOfGiftcards: number = 0;
  public userRole: string = ''; // Voeg de rol van de gebruiker toe
  loading: boolean = true;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cartService: CartService,
    private cartGiftcardService: CartgiftcardService
  ) {}

  public ngOnInit(): void {
    this.checkLoginState();
    if (this.userIsLoggedIn) {
      this.cartService.updateProductsIncart();
      this.cartGiftcardService.updateGiftCardsIncart();

      this.putProductAmmount();
    } else {
      this.loading = false;
    }
  }

  putProductAmmount() {
    this.cartService.$productInCart.subscribe((cart) => {
      this.amountOfProducts = cart.length;
      this.putGiftCardAmount();
    });
  }

  putGiftCardAmount() {
    this.cartGiftcardService.$giftCardsInCart.subscribe((cart) => {
      this.ammountOfGiftcards = cart.length;
      this.calculateTotalCartItems();
    });
  }

  calculateTotalCartItems() {
    this.amountOfItemsInCart = this.ammountOfGiftcards + this.amountOfProducts;
    this.loading = false;
  }

  public onLogout(): void {
    this.authService.logOut();
    this.router.navigate(['/']).then(() => {
      window.location.reload();
    });
  }

  public checkLoginState(): void {
    this.authService.$userIsLoggedIn.subscribe((loginState: boolean) => {
      this.userIsLoggedIn = loginState;
      if (this.userIsLoggedIn) {
        // Haal de rol van de ingelogde gebruiker op
        this.authService.getCurrentUser().subscribe((user: User) => {
          this.userRole = user.role;
        });
      }
    });
  }

  public toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }
}
