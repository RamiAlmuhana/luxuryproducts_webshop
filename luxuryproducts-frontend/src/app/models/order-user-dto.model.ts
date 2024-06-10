import { OrderRetrievalDTO } from "./order-retrieval-dto.model";
import { User } from "./user.model";

export class OrderUserDTO {
  public name: string;

  public infix: string;

  public last_name: string;

  public zipcode: string;

  public houseNumber: number;

  public notes: string;

  public totalProducts: number;

  public orderDate: Date;

  public user: User;

  public cartProducts: OrderRetrievalDTO[];
}
