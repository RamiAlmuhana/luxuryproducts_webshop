import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { Brand } from "../models/brand.model";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class BrandService {
  private baseUrl: string = environment.base_url + "/brand";

  constructor(private http: HttpClient) {}

  getBrandByName(name: string): Observable<Brand> {
    return this.http.get<Brand>(`${this.baseUrl}/${name}`);
  }

  getAllBrands(): Observable<Brand[]> {
    return this.http.get<Brand[]>(`${this.baseUrl}`);
  }
}
