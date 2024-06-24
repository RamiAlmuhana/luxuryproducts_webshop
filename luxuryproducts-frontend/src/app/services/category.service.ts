// category.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../models/category.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private baseUrl: string = environment.base_url + '/category';

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
