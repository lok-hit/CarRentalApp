import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateUserProfileRequest, UserProfile } from '../models/user-profile.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class UserProfileService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/users`;

  list(): Observable<UserProfile[]> {
    return this.http.get<UserProfile[]>(this.base);
  }

  getById(id: string): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.base}/${id}`);
  }

  getByKeycloakId(keycloakId: string): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.base}/by-keycloak/${keycloakId}`);
  }

  create(req: CreateUserProfileRequest): Observable<UserProfile> {
    return this.http.post<UserProfile>(this.base, req);
  }

  updatePreferences(id: string, preferences: Record<string, string>): Observable<UserProfile> {
    return this.http.put<UserProfile>(`${this.base}/${id}/preferences`, { preferences });
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
