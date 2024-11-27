export type Car = {
  id: number;
  rentals: Rental[];
  brand: string;
  model: string;
  type: string;
  licensePlate: string;
  numberOfSeats: number;
  numberOfChildSeats: number;
  foldingRearSeat: boolean;
  towBar: boolean;
};

export type Rental = {
  id: number;
  car: Car;
  startDate: Date;
  endDate: Date;
  street: string;
  streetNumber: number;
  postal: number;
  city: string;
  phoneNumber: string;
  ownerEmail: string;
  basePrice: number;
  pricePerKm: number;
  fuelPenaltyPrice: number;
  pricePerDay: number;
  hasRents: number;
};

export type RentalInput = {
  startDate: Date;
  endDate: Date;
  city: string;
  phoneNumber: string;
  email: string;
};

export type SearchRentalInput = {
  email?: string;
  startDate?: Date;
  endDate?: Date;
  brand?: string;
  city?: string;
  type?: string;
  model?: string;
  numberOfSeats?: number;
  numberOfChildSeats?: number;
  foldingRearSeat?: boolean;
  towBar?: boolean;
};

export type RentStatus = "PENDING" | "CONFIRMED" | "REJECTED";

export enum Role {
  USER = "USER",
  ADMIN = "ADMIN",
  ACCOUNTANT = "ACCOUNTANT",
  OWNER = "OWNER",
  RENTER = "RENTER",
}

export type Rent = {
  id: number;
  rental: Rental;
  startDate: Date;
  endDate: Date;
  phoneNumber: string;
  ownerEmail: string;
  renterEmail: string;
  nationalRegisterNumber: string;
  birthDate: Date;
  licenseNumber: string;
  status: RentStatus;
  checkInDate: Date;
  checkOutDate: Date;
  checkInStatus: boolean;
};

export type RentInput = {
  startDate: Date;
  endDate: Date;
  phoneNumber: string;
  email: string;
  nationalRegisterNumber: string;
  birthDate: Date;
  licenseNumber: string;
};

export type SearchRentInput = {
  startDate?: Date;
  endDate?: Date;
  phoneNumber?: string;
  email?: string;
  nationalRegisterNumber?: string;
  birthDate?: Date;
  licenseNumber?: string;
};

export type Notification = {
  id: number;
  rent: Rent;
  ownerViewed: boolean;
  renterViewed: boolean;
};

export type User = {
  name?: string;
  familyName?: string;
  email?: string;
  password?: string;
  roles?: string;
  id?: number;
};

export type Bill = {
  id: number;
  renterEmail: string;
  ownerEmail: string;
  carBrand: string;
  carModel: string;
  carLicensePlate: string;
  distance: number;
  days: number;
  fuelLevel: number;
  total: number;
  paid: boolean;
};

export type StatusMessage = {
  message: string;
  type: "error" | "success";
};

export type Complaint = {
  id?: number;
  senderEmail: string;
  receiverEmail: string;
  title: string;
  description: string;
};
