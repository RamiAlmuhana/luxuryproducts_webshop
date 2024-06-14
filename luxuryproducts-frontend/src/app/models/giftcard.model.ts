import { User } from './user.model';

export class Giftcard {
  id: number;
  code: string;
  discountAmount: number;
  balance: number;
  used: boolean;
  imageUrl: string;
  user: User;
}
