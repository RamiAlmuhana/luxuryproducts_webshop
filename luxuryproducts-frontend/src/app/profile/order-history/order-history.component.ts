import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service'; // Pas dit pad aan naar waar je service zich bevindt
import { Order } from '../../models/order.model'; // Pas dit pad aan naar waar je model zich bevindt
import { CommonModule } from '@angular/common';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { Return } from '../../models/return.model';
import { Product } from '../../models/product.model';
import { ReturnService } from '../../services/return.service';
import { OrderUserDTO } from '../../models/order-user-dto.model';
import { CartProduct } from '../../models/cart-product.model';
import { OrderRetrievalDTO } from '../../models/order-retrieval-dto.model';
import { Giftcard } from '../../models/giftcard.model';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  imports: [CommonModule],
  standalone: true,
  styleUrls: ['./order-history.component.scss'],
})
export class OrderHistoryComponent implements OnInit {
  //orders: Order[]; // Zorg ervoor dat dit het juiste model gebruikt
  orders: OrderUserDTO[];
  giftcards: Giftcard[] = [];
  public product!: OrderRetrievalDTO;
  public user: User = {
    id: 0,
    name: '',
    infix: '',
    lastName: '',
    email: '',
    role: 'admin',
  };
  public returnRequest: Return = {
    user: this.user,
    id: 0,
    cartProduct: this.product,
    returnStatus: '',
  };
  constructor(
    private orderService: OrderService,
    private userService: UserService,
    private returnService: ReturnService
  ) {}

  ngOnInit() {
    this.loadOrdersIn();
    this.userService.getUserByEmail().subscribe((user: User) => {
      this.user = user;
    });
  }

  loadOrdersIn() {
    this.orderService.getOrdersByCurrentUser().subscribe((orders) => {
      this.orders = orders;
      this.getAllGiftCards();
    });
  }

  getAllGiftCards() {
    this.orders.forEach((order) => {
      order.giftcards.forEach((giftcard) => {
        if (!giftcard.used) {
          this.giftcards.push(giftcard);
        }
      });
    });
  }

  onReturn(product: OrderRetrievalDTO) {
    this.returnRequest.cartProduct = product;
    this.returnRequest.returnStatus = 'pending';
    this.returnRequest.user = this.user;
    product.productReturned = true;
    this.returnService
      .createReturnRequest(this.returnRequest)
      .subscribe((text) => {
        alert('return request created ');
      });
  }
  canbeShown(product: OrderRetrievalDTO): boolean {
    return !product.productReturned;
  }

  getReturnStatus(product: OrderRetrievalDTO) {
    return !product.returnStatus ? 'pending' : product.returnStatus;
  }
}
