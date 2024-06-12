import { Product } from './product.model';

export class Category {
  public id: number;
  public imageUrl: string;
  public description: string;
  public name: string;
  public products: Product[];
}
