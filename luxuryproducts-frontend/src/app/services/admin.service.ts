import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { Product } from "../models/product.model";
import { Observable } from "rxjs";
import { User } from "../models/user.model";

@Injectable({
  providedIn: "root",
})
export class AdminService {
  private baseUrl: string = environment.base_url + "/admin";

  constructor(private http: HttpClient) {}

  createProductVariant(object: any) {
    return this.http.post(`${this.baseUrl}/product-variant`, object, {
      responseType: "text",
    });
  }

  createProduct(object: any) {
    return this.http.post(`${this.baseUrl}/create-product`, object, {
      responseType: "text",
    });
  }

  deleteProductVariant(object: any) {
    console.log(object);
    return this.http.request("delete", `${this.baseUrl}/product-variant`, {
      body: object,
    });
  }

  getProductsThatCanBeDeleted(): Observable<Product[]> {
    return this.http.get<Product[]>(this.baseUrl);
  }
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/users`);
  }

  updateProductVariant(object: any) {
    return this.http.put(`${this.baseUrl}/product-variant`, object, {
      responseType: "text",
    });
  }

  productionStop(object: any) {
    return this.http.put(`${this.baseUrl}/productionStop`, object, {
      responseType: "text",
    });
  }

  changeStock(object: any) {
    return this.http.put(`${this.baseUrl}/stock`, object, {
      responseType: "text",
    });
  }
}
