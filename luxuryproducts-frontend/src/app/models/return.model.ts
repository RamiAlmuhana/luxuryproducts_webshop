import { Product } from "./product.model";
import { User } from "./user.model";
export class Return {
    public id: number;
    public user: User;
    public product: Product;
    public returnStatus: string;


}