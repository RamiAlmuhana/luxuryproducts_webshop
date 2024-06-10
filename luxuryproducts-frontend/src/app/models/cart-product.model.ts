import { Product } from './product.model';
import { Size } from './size.model';

export class CartProduct {
  public id: number;
  public product: Product;
  public quantity: number;
  public price: number;
  public size: string;
  public productVariantPrice: number;
  public imageUrl: string;
  public categoryId: number;
}
