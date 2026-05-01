import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { CarService } from '../../../core/services/car.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Car } from '../../../core/models/car.model';

@Component({
  selector: 'app-car-list',
  imports: [
    RouterLink,
    DecimalPipe,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
  ],
  template: `
    <div class="page-header">
      <h1><mat-icon>directions_car</mat-icon> Samochody</h1>
      @if (auth.hasRole('ADMIN')) {
        <a mat-raised-button color="primary" routerLink="/cars/new">
          <mat-icon>add</mat-icon> Dodaj samochód
        </a>
      }
    </div>

    @if (loading()) {
      <div class="center"><mat-spinner /></div>
    } @else if (error()) {
      <div class="error-banner"><mat-icon>error_outline</mat-icon> {{ error() }}</div>
    } @else {
      <div class="cars-grid">
        @for (car of cars(); track car.id) {
          <mat-card class="car-card" [class.unavailable]="car.status !== 'AVAILABLE'">
            <mat-card-header>
              <mat-icon mat-card-avatar>directions_car</mat-icon>
              <mat-card-title>{{ car.id }}</mat-card-title>
              <mat-card-subtitle>{{ car.category }}</mat-card-subtitle>
            </mat-card-header>
            <mat-card-content>
              <div class="car-details">
                <span><mat-icon>sell</mat-icon> {{ car.price | number:'1.2-2' }} PLN/dzień</span>
              </div>
              <mat-chip-set>
                <mat-chip [class]="car.status === 'AVAILABLE' ? 'available-chip' : 'unavailable-chip'">
                  <mat-icon matChipAvatar>{{ car.status === 'AVAILABLE' ? 'check_circle' : 'cancel' }}</mat-icon>
                  {{ car.status === 'AVAILABLE' ? 'Dostępny' : 'Niedostępny' }}
                </mat-chip>
              </mat-chip-set>
            </mat-card-content>
            <mat-card-actions>
              <a mat-button [routerLink]="['/cars', car.id]">Szczegóły</a>
              @if (car.status === 'AVAILABLE') {
                <a mat-raised-button color="primary" routerLink="/reservations/new"
                   [queryParams]="{ carId: car.id, price: car.price }">
                  Zarezerwuj
                </a>
              }
            </mat-card-actions>
          </mat-card>
        } @empty {
          <div class="empty-state">
            <mat-icon>directions_car_off</mat-icon>
            <p>Brak samochodów. Dodaj pierwszy pojazd!</p>
          </div>
        }
      </div>
    }
  `,
  styles: `
    :host { display: block; padding: 24px; }
    .page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
    .page-header h1 { display: flex; align-items: center; gap: 8px; margin: 0; }
    .center { display: flex; justify-content: center; margin-top: 80px; }
    .error-banner {
      display: flex; align-items: center; gap: 8px;
      background: var(--mat-sys-error-container); color: var(--mat-sys-on-error-container);
      padding: 16px; border-radius: 8px;
    }
    .cars-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap: 20px; }
    .car-card { transition: transform 0.2s, box-shadow 0.2s; }
    .car-card:hover { transform: translateY(-2px); box-shadow: 0 4px 16px rgba(0,0,0,0.12); }
    .car-card.unavailable { opacity: 0.65; }
    .car-details { display: flex; flex-direction: column; gap: 6px; margin: 12px 0; }
    .car-details span { display: flex; align-items: center; gap: 6px; font-size: 0.9rem; }
    .available-chip { background: #e8f5e9 !important; color: #2e7d32 !important; }
    .unavailable-chip { background: #ffebee !important; color: #c62828 !important; }
    .empty-state { display: flex; flex-direction: column; align-items: center; gap: 12px; padding: 64px; color: var(--mat-sys-on-surface-variant); }
    .empty-state mat-icon { font-size: 64px; width: 64px; height: 64px; }
  `,
})
export class CarListComponent implements OnInit {
  private readonly carService = inject(CarService);
  protected readonly auth = inject(AuthService);

  protected readonly loading = signal(true);
  protected readonly error = signal<string | null>(null);
  protected readonly cars = signal<Car[]>([]);

  ngOnInit(): void {
    this.carService.getAvailable().subscribe({
      next: cars => { this.cars.set(cars); this.loading.set(false); },
      error: err => { this.error.set('Nie udało się załadować samochodów.'); this.loading.set(false); console.error(err); },
    });
  }
}
