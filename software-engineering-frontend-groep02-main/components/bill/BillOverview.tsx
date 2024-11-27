import { setBillPaid } from "@/services/billService";
import { Bill } from "@/types";
import { useSession } from "next-auth/react";
import { useEffect } from "react";
import { useTranslation } from "react-i18next";

type Props = {
  bills: Bill[];
};

const BillOverview: React.FC<Props> = ({ bills }: Props) => {
  const { data: session, status } = useSession();
  const { t } = useTranslation();

  const setPaid = async (id: number) => {
    if (session) {
      await setBillPaid(session.user.token, id);
    } else {
      console.error("Failed to set bill as paid");
    }
  };

  return (
    <>
      <div className="flex justify-center mx-8 mt-4">
        <table className="w-90 text-sm text-left">
          <thead className="text-xs text-gray-500 uppercase bg-gray-200 dark:text-gray-400">
            <tr>
              <th className="px-6 py-3">renterEmail</th>
              <th className="px-6 py-3">ownerEmail</th>
              <th className="px-6 py-3">carBrand</th>
              <th className="px-6 py-3">carModel</th>
              <th className="px-6 py-3">carLicensePlate</th>
              <th className="px-6 py-3">distance</th>
              <th className="px-6 py-3">days</th>
              <th className="px-6 py-3">fuelLevel</th>
              <th className="px-6 py-3">total</th>
              <th className="px-6 py-3">paid</th>
              {(session?.user.roles.includes("ADMIN") ||
                session?.user.roles.includes("ACCOUNTANT")) && (
                <th className="px-6 py-3">Actions</th>
              )}
            </tr>
          </thead>
          <tbody>
            {bills?.map((bill: Bill, index: number) => (
              <tr key={index} className="border-b bg-gray-150 border-white">
                <td className="px-6 py-4">{bill.renterEmail}</td>
                <td className="px-6 py-4">{bill.ownerEmail}</td>
                <td className="px-6 py-4">{bill.carBrand}</td>
                <td className="px-6 py-4">{bill.carModel}</td>
                <td className="px-6 py-4">{bill.carLicensePlate}</td>
                <td className="px-6 py-4">{bill.distance}</td>
                <td className="px-6 py-4">{bill.days}</td>
                <td className="px-6 py-4">{bill.fuelLevel}</td>
                <td className="px-6 py-4">€ {bill.total}</td>
                <td
                  className="px-6 py-4"
                  style={{ color: bill.paid ? "green" : "red" }}
                >
                  {bill.paid ? "Paid" : "Not Paid"}
                </td>
                {(session?.user.roles.includes("ADMIN") ||
                  session?.user.roles.includes("ACCOUNTANT")) && (
                  <td className="flex justify-center items-center my-4">
                    <button
                      className="rounded-md bg-blue-600 text-white px-2 py-1 hover:bg-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50"
                      onClick={() => setPaid(bill.id)}
                    >
                      SetPaid
                    </button>
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
};

export default BillOverview;
