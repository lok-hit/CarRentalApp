import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from '../../core/auth/auth.service';
import { UserProfileService } from '../../core/services/user-profile.service';
import { UserProfile } from '../../core/models/user-profile.model';

@Component({
  selector: 'app-profile',
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatDividerModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  template: `
    @if (loading()) {
      <div class="center"><mat-spinner /></div>
    } @else if (profile(); as p) {

      <!-- Istniejący profil -->
      <div class="profile-layout">
        <mat-card class="profile-card">
          <mat-card-header>
            <div mat-card-avatar class="avatar">{{ initials(p) }}</div>
            <mat-card-title>{{ p.firstName }} {{ p.lastName }}</mat-card-title>
            <mat-card-subtitle>{{ p.email }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <div class="chip-row">
              <mat-chip><mat-icon matChipAvatar>person</mat-icon>{{ p.username }}</mat-chip>
              @if (p.vip) {
                <mat-chip class="vip-chip"><mat-icon matChipAvatar>star</mat-icon>VIP</mat-chip>
              }
            </div>
            <mat-divider class="divider" />
            <div class="stats-grid">
              <div class="stat">
                <span class="stat-value">{{ p.activeReservations }}</span>
                <span class="stat-label">Aktywne rezerwacje</span>
              </div>
              <div class="stat">
                <span class="stat-value">{{ p.rentalHistory?.length ?? 0 }}</span>
                <span class="stat-label">Łącznie wynajmów</span>
              </div>
            </div>
          </mat-card-content>
          <mat-card-actions>
            <button mat-stroked-button color="warn" (click)="logout()">
              <mat-icon>logout</mat-icon> Wyloguj się
            </button>
          </mat-card-actions>
        </mat-card>
      </div>

    } @else {

      <!-- Brak profilu — formularz tworzenia -->
      <div class="profile-layout">
        <mat-card>
          <mat-card-header>
            <mat-icon mat-card-avatar>person_add</mat-icon>
            <mat-card-title>Utwórz profil użytkownika</mat-card-title>
            <mat-card-subtitle>
              Zalogowany jako: <strong>{{ auth.username }}</strong>
            </mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            @if (createError()) {
              <div class="error-banner">
                <mat-icon>error_outline</mat-icon> {{ createError() }}
              </div>
            }
            <form [formGroup]="createForm" class="form-grid">
              <mat-form-field appearance="outline">
                <mat-label>Imię</mat-label>
                <input matInput formControlName="firstName" />
                @if (createForm.get('firstName')?.invalid && createForm.get('firstName')?.touched) {
                  <mat-error>Imię jest wymagane</mat-error>
                }
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Nazwisko</mat-label>
                <input matInput formControlName="lastName" />
                @if (createForm.get('lastName')?.invalid && createForm.get('lastName')?.touched) {
                  <mat-error>Nazwisko jest wymagane</mat-error>
                }
              </mat-form-field>
              <mat-form-field appearance="outline" class="full-width">
                <mat-label>E-mail</mat-label>
                <input matInput type="email" formControlName="email" />
                @if (createForm.get('email')?.invalid && createForm.get('email')?.touched) {
                  <mat-error>Podaj poprawny e-mail</mat-error>
                }
              </mat-form-field>
            </form>
          </mat-card-content>
          <mat-card-actions align="end">
            <button mat-raised-button color="primary" (click)="createProfile()"
              [disabled]="createForm.invalid || creating()">
              @if (creating()) {
                <mat-spinner diameter="20" />
              } @else {
                <mat-icon>save</mat-icon> Utwórz profil
              }
            </button>
          </mat-card-actions>
        </mat-card>
      </div>

    }
  `,
  styles: `
    :host { display: block; padding: 24px; }
    .center { display: flex; justify-content: center; margin-top: 80px; }
    .profile-layout { max-width: 560px; }
    .avatar {
      width: 48px; height: 48px; border-radius: 50%;
      background: var(--mat-sys-primary); color: var(--mat-sys-on-primary);
      display: flex; align-items: center; justify-content: center;
      font-size: 1.2rem; font-weight: 600;
    }
    .chip-row { display: flex; gap: 8px; flex-wrap: wrap; margin-top: 16px; }
    .vip-chip { background: gold !important; color: #333 !important; }
    .divider { margin: 20px 0; }
    .stats-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .stat { display: flex; flex-direction: column; align-items: center; padding: 16px; background: var(--mat-sys-surface-variant); border-radius: 8px; }
    .stat-value { font-size: 2rem; font-weight: 700; }
    .stat-label { font-size: 0.8rem; color: var(--mat-sys-on-surface-variant); text-align: center; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-top: 16px; }
    .full-width { grid-column: 1 / -1; }
    mat-form-field { width: 100%; }
    .error-banner {
      display: flex; align-items: center; gap: 8px;
      background: var(--mat-sys-error-container); color: var(--mat-sys-on-error-container);
      padding: 12px 16px; border-radius: 6px; margin-bottom: 16px;
    }
  `,
})
export class ProfileComponent implements OnInit {
  protected readonly auth = inject(AuthService);
  private readonly userProfileService = inject(UserProfileService);
  private readonly fb = inject(FormBuilder);

  protected readonly loading = signal(true);
  protected readonly profile = signal<UserProfile | null>(null);
  protected readonly creating = signal(false);
  protected readonly createError = signal<string | null>(null);

  protected readonly createForm = this.fb.group({
    firstName: ['', Validators.required],
    lastName:  ['', Validators.required],
    email:     ['', [Validators.required, Validators.email]],
  });

  ngOnInit(): void {
    this.createForm.patchValue({ email: this.auth.email });

    this.userProfileService.getByKeycloakId(this.auth.userId).subscribe({
      next: p  => { this.profile.set(p); this.loading.set(false); },
      error: () => { this.profile.set(null); this.loading.set(false); },
    });
  }

  createProfile(): void {
    if (this.createForm.invalid) { this.createForm.markAllAsTouched(); return; }

    this.creating.set(true);
    this.createError.set(null);
    const v = this.createForm.value;

    this.userProfileService.create({
      keycloakId: this.auth.userId,
      email:      v.email!,
      firstName:  v.firstName!,
      lastName:   v.lastName!,
    }).subscribe({
      next: p  => { this.profile.set(p); this.creating.set(false); },
      error: () => {
        this.createError.set('Nie udało się utworzyć profilu. Spróbuj ponownie.');
        this.creating.set(false);
      },
    });
  }

  protected initials(p: UserProfile): string {
    return (`${p.firstName?.charAt(0) ?? ''}${p.lastName?.charAt(0) ?? ''}`).toUpperCase()
      || p.username.charAt(0).toUpperCase();
  }

  logout(): void { this.auth.logout(); }
}
