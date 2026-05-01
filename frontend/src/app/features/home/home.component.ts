import { Component, inject, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-home',
  imports: [MatButtonModule, MatIconModule, MatCardModule],
  template: `
    <div class="hero">

      <div class="hero-content">
        <div class="logo">
          <mat-icon class="logo-icon">directions_car</mat-icon>
        </div>
        <h1 class="title">CarRental</h1>
        <p class="subtitle">Platforma wynajmu samochodów</p>

        <div class="actions">
          <button mat-raised-button color="primary" class="btn-main" (click)="login()">
            <mat-icon>login</mat-icon>
            Zaloguj się
          </button>
          <button mat-stroked-button class="btn-register" (click)="register()">
            <mat-icon>person_add</mat-icon>
            Zarejestruj się
          </button>
        </div>
      </div>

      <div class="features">
        <mat-card class="feature-card">
          <mat-card-content>
            <mat-icon class="feature-icon">search</mat-icon>
            <h3>Przeglądaj flotę</h3>
            <p>Wybieraj spośród dostępnych pojazdów różnych kategorii</p>
          </mat-card-content>
        </mat-card>
        <mat-card class="feature-card">
          <mat-card-content>
            <mat-icon class="feature-icon">event_available</mat-icon>
            <h3>Rezerwuj online</h3>
            <p>Zarezerwuj auto na wybrany termin w kilku krokach</p>
          </mat-card-content>
        </mat-card>
        <mat-card class="feature-card">
          <mat-card-content>
            <mat-icon class="feature-icon">account_circle</mat-icon>
            <h3>Zarządzaj kontem</h3>
            <p>Śledzij swoje rezerwacje i historię wynajmów</p>
          </mat-card-content>
        </mat-card>
      </div>

    </div>
  `,
  styles: `
    :host { display: block; }

    .hero {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 48px 24px;
      gap: 64px;
      background: linear-gradient(160deg, var(--mat-sys-primary-container) 0%, var(--mat-sys-surface) 50%);
    }

    .hero-content {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 16px;
      text-align: center;
    }

    .logo {
      width: 96px; height: 96px; border-radius: 50%;
      background: var(--mat-sys-primary);
      display: flex; align-items: center; justify-content: center;
      box-shadow: 0 8px 32px rgba(0,0,0,0.15);
    }

    .logo-icon {
      font-size: 56px; width: 56px; height: 56px;
      color: var(--mat-sys-on-primary);
    }

    .title {
      font-size: 3rem;
      font-weight: 700;
      margin: 0;
      color: var(--mat-sys-on-surface);
    }

    .subtitle {
      font-size: 1.2rem;
      color: var(--mat-sys-on-surface-variant);
      margin: 0;
    }

    .actions {
      display: flex;
      gap: 16px;
      margin-top: 16px;
      flex-wrap: wrap;
      justify-content: center;
    }

    .btn-main {
      height: 52px;
      padding: 0 32px;
      font-size: 1rem;
    }

    .btn-register {
      height: 52px;
      padding: 0 32px;
      font-size: 1rem;
    }

    .features {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 20px;
      max-width: 800px;
      width: 100%;
    }

    .feature-card {
      text-align: center;
      background: var(--mat-sys-surface-container);
    }

    .feature-card mat-card-content {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
      padding: 24px 16px;
    }

    .feature-icon {
      font-size: 40px; width: 40px; height: 40px;
      color: var(--mat-sys-primary);
    }

    .feature-card h3 { margin: 0; font-size: 1rem; font-weight: 600; }
    .feature-card p  { margin: 0; font-size: 0.875rem; color: var(--mat-sys-on-surface-variant); }
  `,
})
export class HomeComponent implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  ngOnInit(): void {
    if (this.auth.isAuthenticated) {
      this.router.navigate(['/dashboard']);
      return;
    }
    // Po wylogowaniu z aktywną sesją wracamy tutaj z ?register=1
    if (this.route.snapshot.queryParamMap.get('register') === '1') {
      this.auth.register();
    }
  }

  login(): void {
    this.auth.login();
  }

  register(): void {
    // Wyloguj obecną sesję Keycloak przed rejestracją nowego użytkownika
    // żeby SSO nie przywróciło starego konta po rejestracji
    if (this.auth.isAuthenticated) {
      this.auth.registerAfterLogout();
    } else {
      this.auth.register();
    }
  }
}
