import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateReservationRequest, Reservation } from '../models/reservation.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReservationService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/api/reservations`;

  create(req: CreateReservationRequest): Observable<{ reservationId: string; status: string }> {
    return this.http.post<{ reservationId: string; status: string }>(this.base, req);
  }

  getById(id: string): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.base}/${id}`);
  }

  listByCustomer(customerId: string, page = 0, size = 20): Observable<Reservation[]> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Reservation[]>(`${this.base}/customer/${customerId}`, { params });
  }

  confirm(id: string): Observable<void> {
    return this.http.put<void>(`${this.base}/${id}/confirm`, {});
  }

  cancel(id: string, reason: string): Observable<void> {
    return this.http.put<void>(`${this.base}/${id}/cancel`, { reason });
  }
}
