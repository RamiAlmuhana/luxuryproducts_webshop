import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ProductsService } from '../../services/products.service';
import { CartService } from '../../services/cart.service';
import { Product } from '../../models/product.model';
import { Brand } from '../../models/brand.model';
import { Color } from '../../models/color.model';
import { ProductVariant } from '../../models/product-variant.model';
import { SizeAndFit } from '../../models/size-and-fit.model';
import { BrandService } from '../../services/brand.service';
import { AuthService } from '../../auth/auth.service';
import { CartproductService } from '../../services/cartproduct.service';

@Component({
  selector: 'app-product-detail',
  standalone: false,
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss',
})
export class ProductDetailComponent {
  @Input() public product!: Product;
  cartProduct: Product = {
    id: 0,
    name: '',
    country: '',
    quantity: 0,
    productVariants: [],
  };
  cartProductVariant: ProductVariant = {
    id: 0,
    description: '',
    price: 0,
    color: new Color(),
    size_and_fit: new SizeAndFit(),
    productImages: [],
    productVariatie: [],
  };
  private productId: number;
  loading: boolean = true;
  canBuy: boolean;
  showCantbuy: boolean = false;
  productVariantIndex = 0;
  productImage1Index = 0;
  productImage2Index = 1;
  productBrand: Brand;
  public selectedSize: string = '0';
  public selectedQuantity: number = 1;
  loginState: boolean = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private productsService: ProductsService,
    private cartService: CartService,
    private brandService: BrandService,
    private authService: AuthService,
    private cartProductService: CartproductService
  ) {}

  ngOnInit() {
    this.checkLoginState();
    this.loadproductIdandCartProductId();
    this.loadproduct();
    this.loadBrands();
  }
  public loadproductIdandCartProductId() {
    this.activatedRoute.params.subscribe((params) => {
      this.productId = params['id'];
      this.cartProduct.id = params['id'];
    });
  }
  loadproduct() {
    this.productsService
      .getProductByIndex(this.productId)
      .subscribe((product: Product) => {
        this.product = product;
      });
  }
  public loadBrands() {
    this.brandService.getAllBrands().subscribe((brandList) => {
      this.filterBrand(brandList);
      this.loading = false;
    });
  }
  public checkLoginState(): void {
    this.authService.$userIsLoggedIn.subscribe((loginState: boolean) => {
      this.loginState = loginState;
    });
  }

  onNextImage() {
    this.showCantbuy = false;
    var ammountOfImages = this.ammountOfImagesCalculator();
    if (this.productImage2Index < ammountOfImages - 1) {
      this.productImage1Index = this.productImage2Index;
      this.productImage2Index += 1;
    } else {
      this.productImage1Index++;
      this.productImage2Index = 0;
    }
  }

  onPreviousImage() {
    this.showCantbuy = false;
    [0, 1, 2];
    var amountOfImages = this.ammountOfImagesCalculator();

    if (this.productImage1Index <= 0) {
      this.productImage1Index = amountOfImages - 1;
    } else {
      this.productImage1Index -= 1;
    }
    this.productImage2Index =
      (this.productImage1Index - 1 + amountOfImages) % amountOfImages;
  }

  ammountOfImagesCalculator() {
    var imageCounter = 0;
    this.product.productVariants[
      this.productVariantIndex
    ].productImages.forEach((image) => {
      imageCounter += 1;
    });

    return imageCounter;
  }

  changeProductVariant(index: number) {
    this.showCantbuy = false;

    this.productVariantIndex = index;
    this.productImage1Index = 0;
    this.productImage2Index = 1;
  }
  productVariantCounter() {
    var amountOfProductVariants = 0;
    this.product.productVariants.forEach((productVariant) => {
      amountOfProductVariants += 1;
    });

    return amountOfProductVariants;
  }

  filterBrand(brands: Brand[]) {
    brands.forEach((brand) => {
      brand.product.forEach((product1) => {
        if (product1.name == this.product.name) {
          this.productBrand = brand;
        }
      });
    });
  }

  getSizeAndFit() {
    return this.product.productVariants[this.productVariantIndex].size_and_fit
      .fit;
  }
  getColor() {
    return this.product.productVariants[
      this.productVariantIndex
    ].color.name.toLowerCase();
  }
  Onquantitychange(event: Event) {
    this.selectedQuantity = parseInt((<HTMLInputElement>event.target).value);
  }
  OnSizechange(event: Event) {
    this.selectedSize = (<HTMLInputElement>event.target).value;
  }

  infoCartProductVariant() {
    var productVariant: ProductVariant =
      this.product.productVariants[this.productVariantIndex];
    this.cartProductVariant.color = productVariant.color;
    this.cartProductVariant.description = productVariant.description;
    this.cartProductVariant.id = productVariant.id;
    this.cartProductVariant.price = productVariant.price;
    this.cartProductVariant.productImages = productVariant.productImages;
    this.cartProductVariant.productVariatie.push(
      productVariant.productVariatie[parseInt(this.selectedSize)]
    );
    this.cartProductVariant.size_and_fit = productVariant.size_and_fit;
  }

  infoCartProduct() {
    this.cartProduct.quantity = this.selectedQuantity;
    this.cartProduct.productVariants.push(this.cartProductVariant);
    this.cartProduct.name = this.product.name;
    this.cartProduct.country = this.product.country;
  }

  clearCartProduct() {
    this.cartProductVariant.productVariatie = [];
    this.cartProduct.productVariants = [];
  }
  buyProductChecker() {
    this.cartProductService
      .checkCartProductStock(this.cartProduct)
      .subscribe((bool) => {
        this.canBuy = bool;
        this.buyProductReal();
      });
  }

  public onBuyProduct() {
    this.infoCartProductVariant();
    this.infoCartProduct();
    this.buyProductChecker();
  }

  buyProductReal() {
    if (this.canBuy == true) {
      this.showCantbuy = false;
      this.cartService.addProductToCart(this.cartProduct);
      this.clearCartProduct();
    } else {
      this.showCantbuy = true;
    }
  }

  productIsCloth(productVariant: ProductVariant): boolean {
    return productVariant.productVariatie[0].size.category == 'clothes'
      ? true
      : false;
  }
}
