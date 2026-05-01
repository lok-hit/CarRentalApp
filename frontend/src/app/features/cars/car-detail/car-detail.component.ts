import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { CarService } from '../../../core/services/car.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Car } from '../../../core/models/car.model';

@Component({
  selector: 'app-car-detail',
  imports: [
    RouterLink,
    DecimalPipe,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatDividerModule,
  ],
  template: `
    <div class="back-btn">
      <a mat-button routerLink="/cars"><mat-icon>arrow_back</mat-icon> Powrót</a>
    </div>

    @if (loading()) {
      <div class="center"><mat-spinner /></div>
    } @else if (error()) {
      <div class="error-banner"><mat-icon>error_outline</mat-icon> {{ error() }}</div>
    } @else if (car(); as c) {
      <mat-card class="detail-card">
        <mat-card-header>
          <mat-icon mat-card-avatar class="car-icon">directions_car</mat-icon>
          <mat-card-title>{{ c.id }}</mat-card-title>
          <mat-card-subtitle>{{ c.category }}</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <div class="detail-grid">
            <div class="detail-item">
              <mat-icon>category</mat-icon>
              <div><span class="label">Kategoria</span><span class="value">{{ c.category }}</span></div>
            </div>
            <div class="detail-item">
              <mat-icon>sell</mat-icon>
              <div><span class="label">Cena</span><span class="value">{{ c.price | number:'1.2-2' }} PLN/dzień</span></div>
            </div>
            <div class="detail-item">
              <mat-icon>tag</mat-icon>
              <div><span class="label">ID</span><span class="value code">{{ c.id }}</span></div>
            </div>
          </div>
          <mat-divider class="divider" />
          <mat-chip-set>
            <mat-chip [class]="c.status === 'AVAILABLE' ? 'available-chip' : 'unavailable-chip'">
              <mat-icon matChipAvatar>{{ c.status === 'AVAILABLE' ? 'check_circle' : 'cancel' }}</mat-icon>
              {{ c.status === 'AVAILABLE' ? 'Dostępny' : 'Niedostępny' }}
            </mat-chip>
          </mat-chip-set>
        </mat-card-content>
        <mat-card-actions>
          @if (c.status === 'AVAILABLE') {
            <a mat-raised-button color="primary" routerLink="/reservations/new"
               [queryParams]="{ carId: c.id, price: c.price }">
              <mat-icon>event_available</mat-icon> Zarezerwuj
            </a>
          }
          @if (auth.hasRole('OPS')) {
            @if (c.status === 'AVAILABLE') {
              <button mat-stroked-button color="warn" (click)="markUnavailable(c.id)">
                <mat-icon>block</mat-icon> Oznacz jako niedostępny
              </button>
            } @else {
              <button mat-stroked-button color="primary" (click)="markAvailable(c.id)">
                <mat-icon>check_circle</mat-icon> Oznacz jako dostępny
              </button>
            }
          }
        </mat-card-actions>
      </mat-card>
    }
  `,
  styles: `
    :host { display: block; padding: 24px; }
    .back-btn { margin-bottom: 16px; }
    .center { display: flex; justify-content: center; margin-top: 80px; }
    .error-banner { display: flex; align-items: center; gap: 8px; background: var(--mat-sys-error-container); color: var(--mat-sys-on-error-container); padding: 16px; border-radius: 8px; }
    .detail-card { max-width: 500px; }
    .car-icon { font-size: 40px; width: 40px; height: 40px; }
    .detail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin: 20px 0; }
    .detail-item { display: flex; align-items: flex-start; gap: 10px; }
    .detail-item div { display: flex; flex-direction: column; }
    .label { font-size: 0.75rem; color: var(--mat-sys-on-surface-variant); }
    .value { font-size: 1rem; font-weight: 500; }
    .code { font-family: monospace; font-size: 0.85rem; }
    .divider { margin: 16px 0; }
    .available-chip { background: #e8f5e9 !important; color: #2e7d32 !important; }
    .unavailable-chip { background: #ffebee !important; color: #c62828 !important; }
  `,
})
export class CarDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly carService = inject(CarService);
  protected readonly auth = inject(AuthService);

  protected readonly loading = signal(true);
  protected readonly error = signal<string | null>(null);
  protected readonly car = signal<Car | null>(null);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.carService.getById(id).subscribe({
      next: car => { this.car.set(car); this.loading.set(false); },
      error: () => { this.error.set('Nie znaleziono pojazdu.'); this.loading.set(false); },
    });
  }

  markAvailable(id: string): void {
    this.carService.markAvailable(id).subscribe(() =>
      this.car.update(c => c ? { ...c, status: 'AVAILABLE' as const } : c));
  }

  markUnavailable(id: string): void {
    this.carService.markUnavailable(id).subscribe(() =>
      this.car.update(c => c ? { ...c, status: 'UNAVAILABLE' as const } : c));
  }
}
