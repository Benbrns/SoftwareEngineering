export const getAllBills = async (token: any) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/bill", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
  });
};

export const setBillPaid = async (token: any, id: number) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + `/bill/paid/?id=${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
  });
};
