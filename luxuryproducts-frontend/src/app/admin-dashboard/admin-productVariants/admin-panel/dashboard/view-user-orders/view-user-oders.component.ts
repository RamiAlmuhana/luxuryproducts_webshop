import { Component, Input, OnInit } from '@angular/core';
import { User } from '../../../../../models/user.model';
import { OrderService } from '../../../../../services/order.service';
import { OrderUserDTO } from '../../../../../models/order-user-dto.model';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Giftcard } from '../../../../../models/giftcard.model';

@Component({
  selector: 'app-view-user-oders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-user-oders.component.html',
  styleUrl: './view-user-oders.component.scss',
})
export class ViewUserOdersComponent implements OnInit {
  public orderUserDTO: OrderUserDTO[];
  public giftcards: Giftcard[] = [];
  public userId: number;
  public loading = true;

  constructor(
    private orderService: OrderService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getUserIdFromRoute();
  }

  public getUserIdFromRoute() {
    this.activatedRoute.params.subscribe((params) => {
      this.userId = params['id'];
      this.getOrdersByUserId();
    });
  }

  getOrdersByUserId() {
    this.orderService
      .getOrdersByUserId(this.userId)
      .subscribe((orderUserDTOBackend) => {
        this.orderUserDTO = orderUserDTOBackend;
        this.loading = false;
        this.getAllGiftCards();
      });
  }

  getAllGiftCards() {
    this.orderUserDTO.forEach((order) => {
      order.giftcards.forEach((giftcard) => {
        if (!giftcard.used) {
          this.giftcards.push(giftcard);
        }
      });
    });
  }

  onBackToDashboard() {
    this.router.navigate(['/admin-productvariants/dashboard']);
  }
}
