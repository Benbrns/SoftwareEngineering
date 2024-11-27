import { User } from "@/types";

export const userLogin = (user: User) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + `/authenticate`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
  });
};

export const userRegistration = (user: User) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + `/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
  });
};

export const refreshUserData = (token: any) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + `/user`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
  });
};
