import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/home/home.component').then(m => m.HomeComponent),
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [authGuard],
  },
  {
    path: 'cars',
    loadComponent: () =>
      import('./features/cars/car-list/car-list.component').then(m => m.CarListComponent),
    canActivate: [authGuard],
  },
  {
    path: 'cars/new',
    loadComponent: () =>
      import('./features/cars/car-form/car-form.component').then(m => m.CarFormComponent),
    canActivate: [authGuard],
  },
  {
    path: 'cars/:id',
    loadComponent: () =>
      import('./features/cars/car-detail/car-detail.component').then(m => m.CarDetailComponent),
    canActivate: [authGuard],
  },
  {
    path: 'reservations',
    loadComponent: () =>
      import('./features/reservations/reservation-list/reservation-list.component').then(
        m => m.ReservationListComponent,
      ),
    canActivate: [authGuard],
  },
  {
    path: 'reservations/new',
    loadComponent: () =>
      import('./features/reservations/reservation-form/reservation-form.component').then(
        m => m.ReservationFormComponent,
      ),
    canActivate: [authGuard],
  },
  {
    path: 'profile',
    loadComponent: () =>
      import('./features/profile/profile.component').then(m => m.ProfileComponent),
    canActivate: [authGuard],
  },
  {
    path: 'user',
    loadComponent: () =>
      import('./features/profile/profile.component').then(m => m.ProfileComponent),
    canActivate: [authGuard],
  },
  { path: '**', redirectTo: 'dashboard' },
];
