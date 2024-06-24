import { Size } from "./size.model";

export class ProductVariation {
  public id: number;
  public size: Size;
  public quantity_in_stock: number;
  public items_in_stock: boolean;
  public productionStop: boolean;
}
