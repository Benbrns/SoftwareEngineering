import BillOverview from "@/components/bill/BillOverview";
import OverviewEarnings from "@/components/bill/OverviewEarnings";
import { getAllBills } from "@/services/billService";
import { Bill } from "@/types";
import { useSession } from "next-auth/react";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import React, { use, useEffect, useState } from "react";
import useInterval from "use-interval";

const BillPage: React.FC = () => {
  const { data: session, status } = useSession();
  const [isLoading, setIsLoading] = useState(true);
  const [bills, setBills] = useState<Bill[]>([]);

  const fetchData = async () => {
    if (status === "authenticated") {
      setIsLoading(true);
      const response = await getAllBills(session.user.token);
      if (response.ok) {
        console.log("response", response);
        const data = await response.json();
        setBills(data);
        setIsLoading(false);
      } else {
        console.error("Failed to fetch bills");
        setIsLoading(false);
      }
    } else {
      console.error("Failed to fetch bills");
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (status === "authenticated") {
      fetchData();
    }
  }, [status]);

  useInterval(() => {
    fetchData();
  }, 6000);

  return (
    <>
      <main className="mt-20">
        {isLoading && !bills && <p>Loading...</p>}
        {!isLoading && bills.length < 1 && (
          <h1 className="text-center text-red-700 text-2xl p-10">
            There are no bills to show
          </h1>
        )}
        {bills?.length > 0 &&
          (session?.user.roles.includes("ADMIN") ||
            session?.user.roles.includes("OWNER") ||
            session?.user.roles.includes("ACCOUNTANT")) && (
            <OverviewEarnings bills={bills} />
          )}
        {bills?.length > 0 && <BillOverview bills={bills} />}
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

export default BillPage;
