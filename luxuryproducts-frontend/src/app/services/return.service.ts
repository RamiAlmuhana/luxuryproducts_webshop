import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Return } from '../models/return.model';

@Injectable({
  providedIn: 'root',
})
export class ReturnService {
  private baseUrl: string = environment.base_url + '/returns';
  constructor(private http: HttpClient) {}

  public createReturnRequest(returnRequest: Return) {
    console.log(returnRequest);
    return this.http.post(`${this.baseUrl}`, returnRequest, {
      responseType: 'text',
    });
  }

  getReturns(): Observable<Return[]> {
    return this.http.get<Return[]>(`${this.baseUrl}/myReturns`);
  }

  updateReturns(returnData: { returnStatus: string; adminReason: string }, id: number) {
    return this.http.put(`${this.baseUrl}/${id}`, returnData, {
      responseType: 'text',
    });
  }

}
