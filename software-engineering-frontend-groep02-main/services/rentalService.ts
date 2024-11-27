import { Rental, RentalInput, SearchRentalInput } from "@/types";

export const getAllRentals = async (token: any) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/rental", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
  });
};

export const addRentalCar = async (
  rental: RentalInput,
  carId: number,
  token: any
) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/rental/add/?carId=${carId}`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(rental),
    }
  );
};

export const searchRental = async (
  searchRental: SearchRentalInput,
  token: any
) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/rental/search/", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(searchRental),
  });
};

export const cancelRental = async (rentalId: number, token: any) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/rental/delete/?rentalId=${rentalId}`,
    {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
    }
  );
};
