import { Component } from '@angular/core';

import { CartService } from '../services/cart.service';
import { ProductsService } from '../services/products.service';
import { Product } from '../models/product.model';
import { CategoryService } from '../services/category.service';
import { BrandService } from '../services/brand.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss',
})
export class ProductsComponent {
  public products: Product[] = new Array<Product>();
  public savedAllproducts: Product[] = new Array<Product>();
  headerName: string = 'All Products';
  public loadingProducts: boolean = true;

  constructor(
    private productsService: ProductsService,
    private cartService: CartService,
    private categoryService: CategoryService,
    private brandService: BrandService
  ) {}

  ngOnInit(): void {
    this.loadproducts();
  }

  public loadproducts() {
    this.productsService.getProducts().subscribe((products: Product[]) => {
      this.loadingProducts = false;
      this.products = products;
      this.savedAllproducts = products;
    });
  }

  public onBuyProduct(product: Product) {
    this.cartService.addProductToCart(product);
  }

  buttons = [
    {
      label: 'Category',
      showDropdown: false,
      items: ['All', 'Watches', 'Jewelry', 'Bags', 'Clothes'],
    },
    {
      label: 'Brand',
      showDropdown: false,
      items: [
        'Cartier',
        'Vacheron Constantin',
        'Tiffany & Co',
        'Graff',
        'Chanel',
        'Hermes',
        'Gucci',
      ],
    },
    {
      label: 'Color',
      showDropdown: false,
      items: ['Black', 'White', 'Silver', 'Gold', 'Red', 'Green', 'Brown'],
    },
    {
      label: 'Size And Fit',
      showDropdown: false,
      items: ['Relaxed_Fit', 'Loose_Fit', 'Tailored_Fit'],
    },
  ];

  onButtonClick(label: string, item: string) {
    if (item == 'All') {
      this.headerName = 'All Products';
    } else {
      this.headerName = item;
    }
    switch (label) {
      case 'Category':
        this.getCategory(item);

        break;
      case 'Color':
        this.getColor(item);
        break;

      case 'Brand':
        this.getBrand(item);
        break;

      case 'Size And Fit':
        this.getSizeAndFit(item);
        break;

      default:
        console.log('error');
        break;
    }
  }

  getCategory(item: string) {
    if (item == 'All') {
      this.products = this.savedAllproducts;
      return;
    }
    this.categoryService.getCategorybyName(item).subscribe((category) => {
      this.products = category.products;
    });
  }
  getBrand(item: string) {
    this.brandService.getBrandByName(item).subscribe((brand) => {
      this.products = brand.product;
    });
  }
  getColor(item: string) {
    this.productsService
      .getProductsByColor(item)
      .subscribe((productOfColor) => {
        this.products = productOfColor;
      });
  }
  getSizeAndFit(item: string) {
    this.productsService
      .getProductsBySizeAndFit(item)
      .subscribe((productOfSizeAndFit) => {
        this.clothesCheck(productOfSizeAndFit);
      });
  }

  public clothesCheck(products: Product[]): void {
    var clothesSizeAndFit: Product[] = [];
    products.forEach((product) => {
      if (
        product.productVariants[0].productVariatie[0].size.category == 'clothes'
      ) {
        clothesSizeAndFit.push(product);
      }
    });
    this.products = clothesSizeAndFit;
  }

  toggleDropdown(button: any) {
    button.showDropdown = !button.showDropdown;
  }
  toggleDropdownClose(button: any) {
    if (button.showDropdown == false) {
    } else {
      button.showDropdown = false;
    }
  }
}
