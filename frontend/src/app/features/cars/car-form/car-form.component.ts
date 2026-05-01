import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CarService } from '../../../core/services/car.service';
import { CarCategory } from '../../../core/models/car.model';

const CAR_CATEGORIES: CarCategory[] = ['SEDAN', 'SUV', 'HATCHBACK', 'VAN', 'ELECTRIC'];

@Component({
  selector: 'app-car-form',
  imports: [
    RouterLink,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatProgressSpinnerModule,
  ],
  template: `
    <div class="back-btn">
      <a mat-button routerLink="/cars"><mat-icon>arrow_back</mat-icon> Powrót do listy</a>
    </div>

    <mat-card class="form-card">
      <mat-card-header>
        <mat-icon mat-card-avatar>add_circle</mat-icon>
        <mat-card-title>Dodaj nowy samochód</mat-card-title>
        <mat-card-subtitle>Wypełnij dane pojazdu</mat-card-subtitle>
      </mat-card-header>

      <mat-card-content>
        @if (success()) {
          <div class="success-banner">
            <mat-icon>check_circle</mat-icon> Samochód dodany pomyślnie!
          </div>
        }
        @if (submitError()) {
          <div class="error-banner">
            <mat-icon>error_outline</mat-icon> {{ submitError() }}
          </div>
        }

        <form [formGroup]="form" class="form-grid">

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>ID pojazdu</mat-label>
            <input matInput formControlName="id" placeholder="np. car-toyota-001" />
            <mat-hint>Unikalny identyfikator — litery, cyfry, myślniki</mat-hint>
            @if (form.get('id')?.invalid && form.get('id')?.touched) {
              <mat-error>ID wymagane (tylko litery, cyfry, _ lub -)</mat-error>
            }
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Kategoria</mat-label>
            <mat-select formControlName="category">
              @for (cat of categories; track cat) {
                <mat-option [value]="cat">{{ cat }}</mat-option>
              }
            </mat-select>
            @if (form.get('category')?.invalid && form.get('category')?.touched) {
              <mat-error>Wybierz kategorię</mat-error>
            }
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Cena za dzień</mat-label>
            <input matInput type="number" formControlName="price" min="0.01" step="0.01" />
            <span matTextSuffix>PLN/dzień</span>
            @if (form.get('price')?.invalid && form.get('price')?.touched) {
              <mat-error>Podaj cenę (> 0)</mat-error>
            }
          </mat-form-field>

        </form>
      </mat-card-content>

      <mat-card-actions align="end">
        <a mat-button routerLink="/cars">Anuluj</a>
        <button mat-raised-button color="primary" (click)="submit()"
          [disabled]="form.invalid || submitting()">
          @if (submitting()) {
            <mat-spinner diameter="20" />
          } @else {
            <mat-icon>save</mat-icon> Zapisz
          }
        </button>
      </mat-card-actions>
    </mat-card>
  `,
  styles: `
    :host { display: block; padding: 24px; }
    .back-btn { margin-bottom: 16px; }
    .form-card { max-width: 500px; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-top: 16px; }
    .full-width { grid-column: 1 / -1; }
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
  `,
})
export class CarFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly carService = inject(CarService);
  private readonly router = inject(Router);

  protected readonly categories = CAR_CATEGORIES;
  protected readonly submitting = signal(false);
  protected readonly success = signal(false);
  protected readonly submitError = signal<string | null>(null);

  protected readonly form = this.fb.group({
    id: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9_-]+$/)]],
    category: ['' as CarCategory, Validators.required],
    price: [null as number | null, [Validators.required, Validators.min(0.01)]],
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.submitError.set(null);

    const v = this.form.value;
    this.carService.create({
      id: { value: v.id! },
      category: v.category as CarCategory,
      price: v.price!,
    }).subscribe({
      next: () => {
        this.submitting.set(false);
        this.success.set(true);
        setTimeout(() => this.router.navigate(['/cars']), 1500);
      },
      error: err => {
        this.submitting.set(false);
        const status = err.status;
        if (status === 403) {
          this.submitError.set('Brak uprawnień. Wymagana rola ADMIN.');
        } else if (status === 400) {
          this.submitError.set('Błąd walidacji: ' + (err.error?.message ?? 'sprawdź dane'));
        } else if (status === 0) {
          this.submitError.set('Brak połączenia z serwerem. Sprawdź czy backend działa.');
        } else {
          this.submitError.set(`Błąd serwera (${status}). Spróbuj ponownie.`);
        }
        console.error('Create car error:', err);
      },
    });
  }
}
