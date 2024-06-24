import { Giftcard } from './giftcard.model';
import { User } from './user.model';

export class CartGiftCard {
  public id: number;
  public giftcard: Giftcard;
  public imageUrl: string;
  public status: string;
  public user: User;
}
