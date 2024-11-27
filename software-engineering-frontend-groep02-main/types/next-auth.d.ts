import NextAuth from "next-auth";

// Enum definitions
enum Role {
  USER = "USER",
  ADMIN = "ADMIN",
  ACCOUNTANT = "ACCOUNTANT",
  OWNER = "OWNER",
  RENTER = "RENTER",
}

enum RentStatus {
  CONFIRMED = "CONFIRMED",
  PENDING = "PENDING",
  REJECTED = "REJECTED",
}

// Interface definitions
interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber: string;
  birthDate: Date;
  nationalRegisterNumber: string;
  licenseNumber: string;
  roles: Set[Role];
  cars: Set<Car>;
  rents: Set<Rent>;
  token: string;
}

interface Car {
  id: number;
  user: User;
  rentals: Set<Rental>;
  model: string;
  brand: string;
  type: string;
  licensePlate: string;
  numberOfSeats: number;
  numberOfChildSeats: number;
  foldingRearSeat: boolean;
  towBar: boolean;
}

interface Rental {
  id: number;
  car: Car;
  rents: Set<Rent>;
  startDate: Date;
  endDate: Date;
  street: string;
  streetNumber: number;
  postal: number;
  city: string;
}

interface Rent {
  id: number;
  user: User;
  rental: Rental;
  startDate: Date;
  endDate: Date;
  status: RentStatus;
}

// Augmenting NextAuth types
declare module "next-auth" {
  interface Session {
    user: User;
  }
}

import { JWT } from "next-auth/jwt";

declare module "next-auth/jwt" {
  interface JWT {
    user: User;
  }
}
