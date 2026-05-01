import { Component, inject, input, output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLink,
    RouterLinkActive,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatDividerModule,
  ],
  template: `
    <mat-toolbar color="primary" class="navbar">
      <button mat-icon-button (click)="menuToggled.emit()">
        <mat-icon>menu</mat-icon>
      </button>
      <span class="brand" routerLink="/">CarRental</span>

      <span class="spacer"></span>

      <nav class="nav-links">
        <a mat-button routerLink="/dashboard" routerLinkActive="active-link">
          <mat-icon>dashboard</mat-icon> Dashboard
        </a>
        <a mat-button routerLink="/cars" routerLinkActive="active-link">
          <mat-icon>directions_car</mat-icon> Samochody
        </a>
        <a mat-button routerLink="/reservations" routerLinkActive="active-link">
          <mat-icon>event</mat-icon> Rezerwacje
        </a>
      </nav>

      <button mat-icon-button [matMenuTriggerFor]="userMenu">
        <mat-icon>account_circle</mat-icon>
      </button>
      <mat-menu #userMenu="matMenu">
        <div class="user-info">
          <mat-icon>person</mat-icon>
          <span>{{ auth.username }}</span>
        </div>
        <mat-divider />
        <a mat-menu-item routerLink="/profile">
          <mat-icon>manage_accounts</mat-icon> Profil
        </a>
        <button mat-menu-item (click)="auth.logout()">
          <mat-icon>logout</mat-icon> Wyloguj
        </button>
      </mat-menu>
    </mat-toolbar>
  `,
  styles: `
    .navbar { position: fixed; top: 0; left: 0; right: 0; z-index: 1000; }
    .brand { font-size: 1.25rem; font-weight: 600; cursor: pointer; margin-left: 8px; }
    .spacer { flex: 1; }
    .nav-links { display: flex; gap: 4px; }
    .active-link { background: rgba(255,255,255,0.15); border-radius: 4px; }
    .user-info { display: flex; align-items: center; gap: 8px; padding: 8px 16px; font-weight: 500; }
    @media (max-width: 600px) { .nav-links { display: none; } }
  `,
})
export class NavbarComponent {
  protected readonly auth = inject(AuthService);
  readonly menuToggled = output<void>();
}
