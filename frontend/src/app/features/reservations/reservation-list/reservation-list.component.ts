import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { SlicePipe } from '@angular/common';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ReservationService } from '../../../core/services/reservation.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Reservation, ReservationStatus } from '../../../core/models/reservation.model';

@Component({
  selector: 'app-reservation-list',
  imports: [
    RouterLink,
    SlicePipe,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatDialogModule,
    MatTooltipModule,
  ],
  template: `
    <div class="page-header">
      <h1><mat-icon>event</mat-icon> Rezerwacje</h1>
      <a mat-raised-button color="primary" routerLink="/reservations/new">
        <mat-icon>add</mat-icon> Nowa rezerwacja
      </a>
    </div>

    @if (loading()) {
      <div class="center"><mat-spinner /></div>
    } @else if (error()) {
      <div class="error-banner"><mat-icon>error_outline</mat-icon> {{ error() }}</div>
    } @else {
      @if (reservations().length === 0) {
        <div class="empty-state">
          <mat-icon>event_busy</mat-icon>
          <p>Brak rezerwacji.</p>
          <a mat-raised-button color="primary" routerLink="/reservations/new">
            Utwórz pierwszą rezerwację
          </a>
        </div>
      } @else {
        <div class="table-container mat-elevation-z2">
          <table mat-table [dataSource]="reservations()">
            <ng-container matColumnDef="id">
              <th mat-header-cell *matHeaderCellDef>ID</th>
              <td mat-cell *matCellDef="let r">
                <span class="code" [matTooltip]="r.reservationId">
                  {{ r.reservationId | slice:0:8 }}...
                </span>
              </td>
            </ng-container>

            <ng-container matColumnDef="car">
              <th mat-header-cell *matHeaderCellDef>Pojazd</th>
              <td mat-cell *matCellDef="let r">
                <a [routerLink]="['/cars', r.carId]" class="car-link">
                  <mat-icon>directions_car</mat-icon> {{ r.carId | slice:0:8 }}
                </a>
              </td>
            </ng-container>

            <ng-container matColumnDef="dates">
              <th mat-header-cell *matHeaderCellDef>Termin</th>
              <td mat-cell *matCellDef="let r">{{ r.startDate }} – {{ r.endDate }}</td>
            </ng-container>

            <ng-container matColumnDef="price">
              <th mat-header-cell *matHeaderCellDef>Kwota</th>
              <td mat-cell *matCellDef="let r">{{ r.priceAmount }} {{ r.priceCurrency }}</td>
            </ng-container>

            <ng-container matColumnDef="status">
              <th mat-header-cell *matHeaderCellDef>Status</th>
              <td mat-cell *matCellDef="let r">
                <mat-chip [class]="statusClass(r.status)">
                  {{ r.status }}
                </mat-chip>
              </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef></th>
              <td mat-cell *matCellDef="let r">
                @if (r.status === 'CREATED') {
                  <button mat-icon-button color="warn" matTooltip="Anuluj"
                    (click)="cancel(r.reservationId)">
                    <mat-icon>cancel</mat-icon>
                  </button>
                }
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="columns"></tr>
            <tr mat-row *matRowDef="let row; columns: columns;"></tr>
          </table>
        </div>
      }
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
    .table-container { overflow-x: auto; border-radius: 8px; }
    table { width: 100%; }
    .code { font-family: monospace; font-size: 0.85rem; }
    .car-link { display: flex; align-items: center; gap: 4px; text-decoration: none; color: var(--mat-sys-primary); }
    .empty-state { display: flex; flex-direction: column; align-items: center; gap: 12px; padding: 64px; }
    .empty-state mat-icon { font-size: 64px; width: 64px; height: 64px; color: var(--mat-sys-on-surface-variant); }
    .chip-created { background: #e3f2fd !important; color: #1565c0 !important; }
    .chip-confirmed { background: #e8f5e9 !important; color: #2e7d32 !important; }
    .chip-cancelled { background: #ffebee !important; color: #c62828 !important; }
    .chip-pending { background: #fff8e1 !important; color: #f57f17 !important; }
  `,
})
export class ReservationListComponent implements OnInit {
  private readonly reservationService = inject(ReservationService);
  private readonly auth = inject(AuthService);

  protected readonly loading = signal(true);
  protected readonly error = signal<string | null>(null);
  protected readonly reservations = signal<Reservation[]>([]);
  protected readonly columns = ['id', 'car', 'dates', 'price', 'status', 'actions'];

  ngOnInit(): void {
    this.reservationService.listByCustomer(this.auth.username).subscribe({
      next: list => {
        this.reservations.set(list);
        this.loading.set(false);
      },
      error: err => {
        this.error.set('Nie udało się załadować rezerwacji.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  cancel(id: string): void {
    this.reservationService.cancel(id, 'Anulowane przez użytkownika').subscribe({
      next: () => {
        this.reservations.update(list =>
          list.map(r => r.reservationId === id ? { ...r, status: 'CANCELLED' as ReservationStatus } : r),
        );
      },
      error: err => console.error('Anulowanie nie powiodło się', err),
    });
  }

  protected statusClass(status: ReservationStatus): string {
    return `chip-${status.toLowerCase()}`;
  }
}
