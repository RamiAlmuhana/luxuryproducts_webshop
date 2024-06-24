import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { CartProduct } from '../../../../models/cart-product.model';
import { User } from '../../../../models/user.model';
import { AdminService } from '../../../../services/admin.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  users: User[];
  loading: boolean = true;
  viewOrder: boolean = false;
  user: User;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.adminService.getAllUsers().subscribe((userList) => {
      this.users = userList;
      this.loading = false;
    });
  }

  public getOrdersByUser(userId: number) {}

  onViewOrders(index: number) {
    this.user = this.users[index];
    this.viewOrder = true;
  }
}
