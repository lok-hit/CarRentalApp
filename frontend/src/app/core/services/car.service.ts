import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Car, CreateCarRequest } from '../models/car.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CarService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/api/cars`;

  getAvailable(): Observable<Car[]> {
    return this.http.get<Car[]>(this.base);
  }

  getById(id: string): Observable<Car> {
    return this.http.get<Car>(`${this.base}/${id}`);
  }

  create(req: CreateCarRequest): Observable<void> {
    return this.http.post<void>(this.base, req);
  }

  changePrice(id: string, pricePerDay: number): Observable<void> {
    return this.http.put<void>(`${this.base}/${id}/price`, { id: { value: id }, pricePerDay });
  }

  markAvailable(id: string): Observable<void> {
    return this.http.post<void>(`${this.base}/${id}/available`, {});
  }

  markUnavailable(id: string): Observable<void> {
    return this.http.post<void>(`${this.base}/${id}/unavailable`, {});
  }
}
