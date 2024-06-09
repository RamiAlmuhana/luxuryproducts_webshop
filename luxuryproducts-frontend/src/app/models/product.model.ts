export class Product {
  public id: number;
  public name: string;
  public description: string;
  public price: number;
  public imgURL: string;
  public amount: number;
  public specifications: string;
  public publisher: string;
  public releaseDate: string;
  public promoCode: string;
  public promoDiscount: number;
  public promoType: string;
  public categoryId: number;
  public productReturned?: boolean;
}
