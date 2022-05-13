export interface Offender {
  id: number;
  firstName: string;
  lastName: string;
  position: { latitude: number; longitude: number };
  picture: File | string | Blob;
  birthDate?: string;
}
