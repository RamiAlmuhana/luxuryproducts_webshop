import { Injectable } from "@angular/core";
import { CartService } from "./cart.service";
import { CartProduct } from "../models/cart-product.model";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Product } from "../models/product.model";

@Injectable({
  providedIn: "root",
})
export class CartproductService {
  private baseUrl: string = environment.base_url + "/cart-product";

  constructor(private http: HttpClient) {}

  addProductToCart(product: Product): Observable<CartProduct[]> {
    return this.http.post<CartProduct[]>(this.baseUrl, product);
  }

  getProductsInCart(): Observable<CartProduct[]> {
    return this.http.get<CartProduct[]>(this.baseUrl);
  }

  deleteProductInCart(id: number): Observable<CartProduct[]> {
    return this.http.delete<CartProduct[]>(`${this.baseUrl}/${id}`);
  }

  deleteAll() {
    return this.http.delete(`${this.baseUrl}/deleteall`, {
      responseType: "text",
    });
  }

  checkCartProductStock(cartProduct: Product): Observable<boolean> {
    return this.http.post<boolean>(
      `${this.baseUrl}/check-cartProduct`,
      cartProduct
    );
  }
}
