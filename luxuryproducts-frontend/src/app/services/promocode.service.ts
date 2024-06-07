import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PromoCode } from "../models/promocode.model";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PromoCodeService {

  private baseUrl = environment.base_url + '/promocodes';

  constructor(private http: HttpClient) { }

  public getAllPromoCodes(): Observable<PromoCode[]> {
    return this.http.get<PromoCode[]>(this.baseUrl);
  }

  public getPromoCode(id: number): Observable<PromoCode> {
    return this.http.get<PromoCode>(`${this.baseUrl}/${id}`);
  }

  public createPromoCode(promoCode: PromoCode): Observable<any> {
    return this.http.post(this.baseUrl, promoCode);
  }

  public updatePromoCode(id: number, promoCode: PromoCode): Observable<PromoCode> {
    return this.http.put<PromoCode>(`${this.baseUrl}/${id}`, promoCode);
  }

  public deletePromoCode(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  // New method to fetch promo code statistics
  public getPromoCodeStats(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/promocode-stats`);
  }
}
