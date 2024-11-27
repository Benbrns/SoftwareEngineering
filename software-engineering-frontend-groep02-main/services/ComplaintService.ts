import { Complaint } from "@/types";

export const getAllComplaints = async (token: any) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/complaint", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
  });
};

export const addComplaint = async (complaint: Complaint, token: any) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/complaint/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(complaint),
  });
};

export const deleteComplaint = async (complaintId: number, token: any) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL +
      `/complaint/delete/?complaintId=${complaintId}`,
    {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
    }
  );
};
