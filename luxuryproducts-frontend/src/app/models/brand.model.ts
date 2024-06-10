import { Product } from './product.model';

export class Brand {
  public id: number;
  public brandName: string;
  public description: string;
  public product: Product[];
}
