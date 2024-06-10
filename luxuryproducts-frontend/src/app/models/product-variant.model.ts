import { Color } from "./color.model";
import { ProductImages } from "./product-images.model";
import { ProductVariation } from "./product-variation.model";
import { SizeAndFit } from "./size-and-fit.model";

export class ProductVariant {
  public id: number;
  public description: string;
  public price: number;
  public color: Color;
  public size_and_fit: SizeAndFit;
  public productImages: ProductImages[];
  public productVariatie: ProductVariation[];
}
