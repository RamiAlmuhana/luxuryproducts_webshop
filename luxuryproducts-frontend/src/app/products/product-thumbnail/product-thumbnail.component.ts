import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Product } from '../../models/product.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-product-thumbnail',
  templateUrl: './product-thumbnail.component.html',
  styleUrl: './product-thumbnail.component.scss',
  standalone: true,
  imports: [CommonModule, RouterModule],
})
export class ProductThumbnailComponent {
  @Input() public product!: Product;
  @Output() public onBuyProduct: EventEmitter<Product> =
    new EventEmitter<Product>();

  public buyProduct(product: Product) {
    this.onBuyProduct.emit(product);
  }
}
