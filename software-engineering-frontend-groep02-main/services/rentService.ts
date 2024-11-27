import { Rent, RentInput, RentStatus, Rental, SearchRentInput } from "@/types";

export const getAllRents = async (token: any) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/rent", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
  });
};

export const makeRent = async (rent: RentInput, rental: Rental, token: any) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/rent/add/${rental.id}`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(rent),
    }
  );
};

export const searchRents = async (searchRent: SearchRentInput, token: any) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/rent/email/?email=${searchRent.email}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
    }
  );
};

export const cancelRent = async (rentId: number, token: any) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/rent/delete/?rentId=${rentId}`,
    {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
    }
  );
};

export const setStatusRent = async (
  status: string,
  rentId: number,
  token: any
) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/rent/status/${status}/${rentId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
    }
  );
};

export const checkInRent = async (rentId: number, token: any) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/rent/checkIn/?rentId=${rentId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
    }
  );
};

export const checkOutRent = async (
  rentId: number,
  token: any,
  distance: number,
  fuelLevel: number
) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/rent/checkOut/?rentId=${rentId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ distance, fuelLevel }),
    }
  );
};
