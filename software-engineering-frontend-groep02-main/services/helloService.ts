export const getHello = async () => {
  return await fetch(process.env.NEXT_PUBLIC_API_URL + "/hello", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
};
