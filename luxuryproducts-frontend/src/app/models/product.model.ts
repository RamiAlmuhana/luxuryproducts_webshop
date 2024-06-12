import { ProductVariant } from './product-variant.model';

export class Product {
  public id: number;
  public name: string;
  public country: string;
  public quantity: number;
  public productVariants: ProductVariant[];
  public promoCode?: string;
  public promoDiscount?: number;
  public promoType?: string;
}
