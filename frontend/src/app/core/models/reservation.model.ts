export type ReservationStatus = 'CREATED' | 'CONFIRMED' | 'CANCELLED' | 'PENDING';

export interface Reservation {
  reservationId: string;
  carId: string;
  customerId: string;
  startDate: string;
  endDate: string;
  priceAmount: number;
  priceCurrency: string;
  status: ReservationStatus;
}

export interface CreateReservationRequest {
  carId: string;
  customerId: string;
  startDate: string;
  endDate: string;
  priceAmount: number;
  priceCurrency: string;
}
