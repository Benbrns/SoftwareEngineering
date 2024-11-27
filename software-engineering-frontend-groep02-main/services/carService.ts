export const addCarService = async (car: any, token: any) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/car/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      brand: car.brand,
      model: car.model,
      type: car.type,
      licensePlate: car.licensePlate,
      numberOfSeats: car.numberOfSeats,
      numberOfChildSeats: car.numberOfChildSeats,
      foldingRearSeat: car.foldingRearSeat,
      towBar: car.towBar,
    }),
  });
};

export const getAllCars = async (token: any) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/car", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
  });
};

export const getCarByRentalId = async (id: number, token: any) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + "/rental/get/?rentalId=" + id,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
    }
  );
};

export const deleteCar = async (id: number, token: any) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + `/car/delete/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
  });
};
