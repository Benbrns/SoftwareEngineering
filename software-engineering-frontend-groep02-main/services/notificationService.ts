import { Role } from "@/types";

export const getAllNotifications = async (token: any) => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/notification", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: `Bearer ${token}`,
    },
  });
};

// set notification as viewed
export const setNotificationAsViewed = async (
  token: any,
  notificationId: number,
  role: String
) => {
  return await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/notification/viewed/${role}/${notificationId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        authorization: `Bearer ${token}`,
      },
    }
  );
};
