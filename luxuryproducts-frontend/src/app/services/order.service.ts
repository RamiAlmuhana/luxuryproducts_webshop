import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { OrderUserDTO } from '../models/order-user-dto.model';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private baseUrl: string = environment.base_url + '/orders/myOrders';

  constructor(private http: HttpClient) {}

  getOrdersByCurrentUser(): Observable<OrderUserDTO[]> {
    return this.http.get<OrderUserDTO[]>(this.baseUrl);
  }
}
