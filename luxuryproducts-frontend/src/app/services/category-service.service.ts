import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";
import { Product } from "../models/product.model";
import { Category } from "../models/category.model";

@Injectable({
  providedIn: "root",
})
export class CategoryServiceService {
  private baseUrl: string = environment.base_url + "/category";

  constructor(private http: HttpClient) {}

  getCategorys(): Observable<Category[]> {
    return this.http.get<Category[]>(this.baseUrl);
  }

  getCategorybyId(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.baseUrl}/${id}`);
  }

  getCategorybyName(name: string): Observable<Category> {
    return this.http.get<Category>(`${this.baseUrl}/name/${name}`);
  }
}
