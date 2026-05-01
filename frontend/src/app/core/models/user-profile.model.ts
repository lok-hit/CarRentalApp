export interface UserProfile {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  activeReservations: number;
  vip: boolean;
  rentalHistory?: string[];
  preferences?: Record<string, string>;
}

export interface CreateUserProfileRequest {
  keycloakId: string;
  email: string;
  firstName: string;
  lastName: string;
  phone?: string;
}
