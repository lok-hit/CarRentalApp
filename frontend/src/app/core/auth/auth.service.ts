import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly keycloak = new Keycloak(environment.keycloak);

  init(): Promise<boolean> {
    return this.keycloak.init({
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      checkLoginIframe: false,
    });
  }

  login(): void {
    this.keycloak.login();
  }

  register(): void {
    this.keycloak.register();
  }

  registerAfterLogout(): void {
    // Wyloguj z Keycloak, a po wylogowaniu przekieruj z powrotem na stronę główną
    // Użytkownik kliknie "Zarejestruj" ponownie — tym razem bez aktywnej sesji
    const registerUrl = `${window.location.origin}/?register=1`;
    this.keycloak.logout({ redirectUri: registerUrl });
  }

  logout(): void {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }

  get isAuthenticated(): boolean {
    return !!this.keycloak.authenticated;
  }

  getToken(): Promise<string> {
    return this.keycloak
      .updateToken(30)
      .then(() => this.keycloak.token ?? '')
      .catch(() => '');
  }

  get username(): string {
    return this.keycloak.tokenParsed?.['preferred_username'] ?? 'Unknown';
  }

  get email(): string {
    return this.keycloak.tokenParsed?.['email'] ?? '';
  }

  /** Keycloak user UUID (sub claim) — used as keycloakId in user-profile-service */
  get userId(): string {
    return this.keycloak.tokenParsed?.['sub'] ?? '';
  }

  hasRole(role: string): boolean {
    return this.keycloak.hasRealmRole(role);
  }
}
