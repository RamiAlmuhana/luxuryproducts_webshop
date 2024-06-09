import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service'; // Pas dit pad aan naar waar je service zich bevindt
import { Order } from '../../models/order.model'; // Pas dit pad aan naar waar je model zich bevindt
import { CommonModule } from '@angular/common';
import {User} from "../../models/user.model";
import {UserService} from "../../services/user.service";
import {Return} from "../../models/return.model";
import {Product} from "../../models/product.model";
import {ReturnService} from "../../services/return.service";

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  imports: [CommonModule],
  standalone: true,
  styleUrls: ['./order-history.component.scss']
})
export class OrderHistoryComponent implements OnInit {
  //orders: Order[]; // Zorg ervoor dat dit het juiste model gebruikt
  orders: any[];
  public  product!: Product;
  public user: User = {id:0, name:"", infix: "", lastName:"", email:"", role: "admin" };
  public returnRequest: Return = {user: this.user,  id: 0, product: this.product, returnStatus: ""};
  constructor(private orderService: OrderService, private userService: UserService, private returnService: ReturnService) { }

  ngOnInit() {
    this.loadOrdersIn();
    this.userService.getUserByEmail().subscribe((user: User) => {
      this.user = user;

    })
  }

  loadOrdersIn() {
    this.orderService.getOrdersByCurrentUser().subscribe(orders => {
      this.orders = orders;
    });
  }

  onReturn(product: Product) {
    this.returnRequest.product = product;
    this.returnRequest.returnStatus = "pending";
    this.returnRequest.user = this.user;
    product.productReturned = true;
    this.returnService.createReturnRequest(this.returnRequest).subscribe((text) => {
      alert("return request created ");
    });


  }
  canbeShown(product: Product): boolean {

    return !product.productReturned;

  }
}
