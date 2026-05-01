import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { provideNativeDateAdapter } from '@angular/material/core';
import { ReservationService } from '../../../core/services/reservation.service';
import { CarService } from '../../../core/services/car.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Car } from '../../../core/models/car.model';

@Component({
  selector: 'app-reservation-form',
  imports: [
    RouterLink,
    DecimalPipe,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDatepickerModule,
    MatSelectModule,
    MatProgressSpinnerModule,
  ],
  providers: [provideNativeDateAdapter()],
  template: `
    <div class="back-btn">
      <a mat-button routerLink="/reservations"><mat-icon>arrow_back</mat-icon> Powrót</a>
    </div>

    <mat-card class="form-card">
      <mat-card-header>
        <mat-icon mat-card-avatar>event_available</mat-icon>
        <mat-card-title>Nowa rezerwacja</mat-card-title>
        <mat-card-subtitle>Wybierz pojazd i termin wynajmu</mat-card-subtitle>
      </mat-card-header>

      <mat-card-content>
        @if (success()) {
          <div class="success-banner">
            <mat-icon>check_circle</mat-icon>
            Rezerwacja utworzona! ID: <strong>{{ createdId() }}</strong>
          </div>
        }
        @if (submitError()) {
          <div class="error-banner">
            <mat-icon>error_outline</mat-icon> {{ submitError() }}
          </div>
        }

        <form [formGroup]="form" (ngSubmit)="submit()" class="form-grid">
          <mat-form-field appearance="outline">
            <mat-label>Pojazd</mat-label>
            <mat-select formControlName="carId">
              @for (car of cars(); track car.id) {
                <mat-option [value]="car.id">
                  {{ car.id }} — {{ car.category }} ({{ car.price | number:'1.0-0' }} PLN/dzień)
                </mat-option>
              }
            </mat-select>
            @if (form.get('carId')?.invalid && form.get('carId')?.touched) {
              <mat-error>Wybierz pojazd</mat-error>
            }
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>ID klienta</mat-label>
            <input matInput formControlName="customerId" placeholder="customer-123" />
            @if (form.get('customerId')?.invalid && form.get('customerId')?.touched) {
              <mat-error>ID klienta jest wymagane</mat-error>
            }
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Data rozpoczęcia</mat-label>
            <input matInput [matDatepicker]="startPicker" formControlName="startDate" />
            <mat-datepicker-toggle matIconSuffix [for]="startPicker" />
            <mat-datepicker #startPicker />
            @if (form.get('startDate')?.invalid && form.get('startDate')?.touched) {
              <mat-error>Wybierz datę rozpoczęcia</mat-error>
            }
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Data zakończenia</mat-label>
            <input matInput [matDatepicker]="endPicker" formControlName="endDate" />
            <mat-datepicker-toggle matIconSuffix [for]="endPicker" />
            <mat-datepicker #endPicker />
            @if (form.get('endDate')?.invalid && form.get('endDate')?.touched) {
              <mat-error>Wybierz datę zakończenia</mat-error>
            }
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Kwota (PLN)</mat-label>
            <input matInput type="number" formControlName="priceAmount" min="0.01" step="0.01" />
            <span matTextSuffix>PLN</span>
            @if (form.get('priceAmount')?.invalid && form.get('priceAmount')?.touched) {
              <mat-error>Podaj kwotę</mat-error>
            }
          </mat-form-field>
        </form>
      </mat-card-content>

      <mat-card-actions align="end">
        <a mat-button routerLink="/reservations">Anuluj</a>
        <button mat-raised-button color="primary" (click)="submit()"
          [disabled]="form.invalid || submitting()">
          @if (submitting()) {
            <mat-spinner diameter="20" />
          } @else {
            <mat-icon>event_available</mat-icon> Zarezerwuj
          }
        </button>
      </mat-card-actions>
    </mat-card>
  `,
  styles: `
    :host { display: block; padding: 24px; }
    .back-btn { margin-bottom: 16px; }
    .form-card { max-width: 640px; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-top: 16px; }
    mat-form-field { width: 100%; }
    .success-banner {
      display: flex; align-items: center; gap: 8px;
      background: #e8f5e9; color: #2e7d32; padding: 12px 16px; border-radius: 6px; margin-bottom: 16px;
    }
    .error-banner {
      display: flex; align-items: center; gap: 8px;
      background: var(--mat-sys-error-container); color: var(--mat-sys-on-error-container);
      padding: 12px 16px; border-radius: 6px; margin-bottom: 16px;
    }
    @media (max-width: 480px) { .form-grid { grid-template-columns: 1fr; } }
  `,
})
export class ReservationFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly reservationService = inject(ReservationService);
  private readonly carService = inject(CarService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  protected readonly auth = inject(AuthService);
  protected readonly cars = signal<Car[]>([]);
  protected readonly submitting = signal(false);
  protected readonly success = signal(false);
  protected readonly submitError = signal<string | null>(null);
  protected readonly createdId = signal<string | null>(null);

  protected readonly form = this.fb.group({
    carId: ['', Validators.required],
    customerId: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9_-]+$/)]],
    startDate: [null as Date | null, Validators.required],
    endDate: [null as Date | null, Validators.required],
    priceAmount: [null as number | null, [Validators.required, Validators.min(0.01)]],
    priceCurrency: ['PLN'],
  });

  ngOnInit(): void {
    this.carService.getAvailable().subscribe(cars => {
      this.cars.set(cars);
      // Pre-fill from query params after cars are loaded
      const q = this.route.snapshot.queryParamMap;
      const carId = q.get('carId');
      const price = q.get('price');
      if (carId) this.form.patchValue({ carId });
      if (price) this.form.patchValue({ priceAmount: parseFloat(price) });
    });

    // Auto-fill price when car is selected
    this.form.get('carId')!.valueChanges.subscribe(selectedId => {
      const car = this.cars().find(c => c.id === selectedId);
      if (car) {
        this.form.patchValue({ priceAmount: car.price }, { emitEvent: false });
      }
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.submitError.set(null);

    const v = this.form.value;
    const toIso = (d: Date | null | undefined) => d ? d.toISOString().split('T')[0] : '';

    this.reservationService.create({
      carId: v.carId!,
      customerId: v.customerId!,
      startDate: toIso(v.startDate),
      endDate: toIso(v.endDate),
      priceAmount: v.priceAmount!,
      priceCurrency: v.priceCurrency!,
    }).subscribe({
      next: res => {
        this.submitting.set(false);
        this.success.set(true);
        this.createdId.set(res.reservationId ?? null);
        setTimeout(() => this.router.navigate(['/reservations']), 2000);
      },
      error: err => {
        this.submitting.set(false);
        this.submitError.set('Nie udało się utworzyć rezerwacji. Sprawdź dane i spróbuj ponownie.');
        console.error(err);
      },
    });
  }
}
