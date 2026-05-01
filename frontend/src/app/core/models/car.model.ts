export type CarCategory = 'SEDAN' | 'SUV' | 'HATCHBACK' | 'VAN' | 'ELECTRIC';
export type CarStatus = 'AVAILABLE' | 'UNAVAILABLE';

export interface Car {
  id: string;
  category: CarCategory;
  price: number;
  status: CarStatus;
}

export interface CreateCarRequest {
  id: { value: string };
  category: CarCategory;
  price: number;
}
