import { ProductVariant } from './product-variant.model';
import { PromocodeDTO } from './promocodeDTO';

export class Product {
  public id: number;
  public name: string;
  public country: string;
  public quantity: number;
  public productVariants: ProductVariant[];
  public promocodeDTOS?: PromocodeDTO[];
}
