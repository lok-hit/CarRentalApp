import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { DashboardService } from '../../core/services/dashboard.service';
import { Dashboard } from '../../core/models/dashboard.model';

@Component({
  selector: 'app-dashboard',
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
    @if (loading()) {
      <div class="center"><mat-spinner /></div>
    } @else if (error()) {
      <div class="error-banner">
        <mat-icon>error_outline</mat-icon> {{ error() }}
      </div>
    } @else if (data(); as d) {
      <div class="dashboard-grid">

        <mat-card class="welcome-card">
          <mat-card-header>
            <mat-icon mat-card-avatar>waving_hand</mat-icon>
            <mat-card-title>Witaj, {{ d.userProfile.firstName || d.userProfile.username }}!</mat-card-title>
            <mat-card-subtitle>{{ d.userProfile.email }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <div class="stat-row">
              <div class="stat">
                <span class="stat-value">{{ d.userProfile.activeReservations }}</span>
                <span class="stat-label">Aktywne rezerwacje</span>
              </div>
              @if (d.userProfile.vip) {
                <mat-chip class="vip-chip"><mat-icon matChipAvatar>star</mat-icon> VIP</mat-chip>
              }
            </div>
          </mat-card-content>
          <mat-card-actions>
            <a mat-button color="primary" routerLink="/reservations">Moje rezerwacje</a>
            <a mat-button routerLink="/profile">Profil</a>
          </mat-card-actions>
        </mat-card>

        <mat-card class="cars-summary-card">
          <mat-card-header>
            <mat-icon mat-card-avatar>directions_car</mat-icon>
            <mat-card-title>Dostępne samochody</mat-card-title>
            <mat-card-subtitle>{{ availableCount(d) }} pojazd{{ suffix(availableCount(d)) }} do wynajęcia</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <div class="car-preview-list">
              @for (car of d.availableCars.slice(0, 4); track car.id) {
                <div class="car-preview">
                  <mat-icon>directions_car</mat-icon>
                  <span>{{ car.id }} &nbsp;·&nbsp; {{ car.category }} &nbsp;·&nbsp; <strong>{{ car.price | number:'1.0-0' }} PLN</strong></span>
                </div>
              }
              @if (d.availableCars.length > 4) {
                <span class="more-label">+{{ d.availableCars.length - 4 }} więcej</span>
              }
              @if (d.availableCars.length === 0) {
                <span class="more-label">Brak dostępnych pojazdów</span>
              }
            </div>
          </mat-card-content>
          <mat-card-actions>
            <a mat-button color="primary" routerLink="/cars">Przeglądaj wszystkie</a>
            <a mat-button color="accent" routerLink="/reservations/new">Zarezerwuj</a>
          </mat-card-actions>
        </mat-card>

      </div>
    }
  `,
  styles: `
    :host { display: block; padding: 24px; }
    .center { display: flex; justify-content: center; margin-top: 80px; }
    .error-banner {
      display: flex; align-items: center; gap: 8px;
      background: var(--mat-sys-error-container); color: var(--mat-sys-on-error-container);
      padding: 16px; border-radius: 8px;
    }
    .dashboard-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 24px; max-width: 900px; }
    .stat-row { display: flex; align-items: center; gap: 24px; margin-top: 16px; }
    .stat { display: flex; flex-direction: column; }
    .stat-value { font-size: 2rem; font-weight: 700; line-height: 1; }
    .stat-label { font-size: 0.875rem; color: var(--mat-sys-on-surface-variant); }
    .vip-chip { background: gold !important; color: #333 !important; }
    .car-preview-list { display: flex; flex-direction: column; gap: 8px; margin-top: 12px; }
    .car-preview { display: flex; align-items: center; gap: 8px; font-size: 0.9rem; }
    .more-label { font-size: 0.875rem; color: var(--mat-sys-on-surface-variant); padding-left: 32px; }
  `,
})
export class DashboardComponent implements OnInit {
  private readonly dashboardService = inject(DashboardService);

  protected readonly loading = signal(true);
  protected readonly error = signal<string | null>(null);
  protected readonly data = signal<Dashboard | null>(null);

  ngOnInit(): void {
    this.dashboardService.getDashboard().subscribe({
      next: d => { this.data.set(d); this.loading.set(false); },
      error: err => {
        this.error.set('Nie udało się załadować danych. Sprawdź połączenie z API Gateway.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  protected availableCount(d: Dashboard): number {
    return d.availableCars.filter(c => c.status === 'AVAILABLE').length;
  }

  protected suffix(n: number): string {
    if (n === 1) return '';
    if (n >= 2 && n <= 4) return 'y';
    return 'ów';
  }
}
