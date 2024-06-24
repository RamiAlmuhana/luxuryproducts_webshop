import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductVariant } from '../../../../models/product-variant.model';

@Component({
  selector: 'app-product-detailshow',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-detailshow.component.html',
  styleUrl: './product-detailshow.component.scss',
})
export class ProductDetailshowComponent {
  @Input() public productVariant!: ProductVariant;
}
