import { Car } from './car.model';
import { UserProfile } from './user-profile.model';

export interface Dashboard {
  availableCars: Car[];
  userProfile: UserProfile;
}
