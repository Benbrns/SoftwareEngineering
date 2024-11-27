import Head from "next/head";
import React, { useState, useEffect } from "react";
import useSWR, { mutate } from "swr";
import { Car } from "@/types";
import { getAllCars } from "@/services/carService";
import CarOverview from "@/components/car/CarOverview";
import AddRental from "@/components/rental/addRental";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useSession } from "next-auth/react";
import useInterval from "use-interval";

const Cars: React.FC = () => {
  const [renting, setRenting] = useState<number>(0);
  const { data: session } = useSession();

  const getCars = async () => {
    if (session?.user.token) {
      const response = await getAllCars(session.user.token);
      const cars = await response.json();
      return { cars };
    }
    return { cars: [] }; // Return empty or default data if session isn't available
  };

  const { data, error } = useSWR("api/cars", getCars);

  useEffect(() => {
    mutate("api/cars", getCars());
  }, [session]);

  useInterval(() => {
    mutate("api/cars", getCars());
  }, 2000);

  // session?.user.token ? ["/api/cars", session.user.token] : null,
  // getCars,
  // {
  //   onErrorRetry: (error, key, option, revalidate, { retryCount }) => {
  //     if (retryCount >= 10) return; // Do not retry on failure more than 10 times.
  //     if (error.status === 404) return; // Do not retry if it is a 404.
  //     // Retry after 5 seconds.
  //     setTimeout(() => revalidate({ retryCount }), 5000);
  //   },
  // }

  return (
    <>
      <main className="mt-20">
        {renting === 0 && (
          <CarOverview
            isLoading={!data && !error}
            cars={data?.cars || []}
            renting={renting}
            setRenting={setRenting}
          />
        )}
        {renting > 0 && <AddRental carId={renting} />}
      </main>
    </>
  );
};

export const getServerSideProps = async (context: { locale: any }) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ["common"])),
    },
  };
};

export default Cars;
