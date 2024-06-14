import { CartProduct } from './cart-product.model';
import { OrderRetrievalDTO } from './order-retrieval-dto.model';
import { User } from './user.model';
export class Return {
  public id: number;
  public cartProduct: OrderRetrievalDTO;
  public returnStatus: string;
  public returnReason: string;
  public adminReason: string;
  public user: User;
}
