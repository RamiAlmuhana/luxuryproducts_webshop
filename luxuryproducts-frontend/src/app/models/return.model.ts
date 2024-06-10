import { CartProduct } from './cart-product.model';
import { User } from './user.model';
export class Return {
  public id: number;
  public cartproduct: CartProduct;
  public returnStatus: string;
  public user: User;
}
