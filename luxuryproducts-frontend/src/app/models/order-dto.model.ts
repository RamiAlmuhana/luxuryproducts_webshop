export class OrderDTO {
  public name: string;
  public infix: string;
  public last_name: string;
  public zipcode: string;
  public houseNumber: number;
  public notes: string;
  public cartProductId: number[];
  public promoCode: string | null;
  public giftCardCode: string | null;
  public discountedPrice: number;
}
