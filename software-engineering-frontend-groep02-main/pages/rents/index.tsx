import React, { useState, useEffect, useRef } from "react";
import { Rent } from "@/types";
import {
  cancelRent,
  checkInRent,
  getAllRents,
  checkOutRent,
} from "@/services/rentService";
import SearchRent from "@/components/rent/SearchRent";
import Modal from "@/components/ui/modal";
import MakeRentForm from "@/components/rent/MakeRentForm";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation, UseTranslation } from "next-i18next";
import { useSession } from "next-auth/react";
import useInterval from "use-interval";

const Rents: React.FC = () => {
  const { t } = useTranslation();
  const formRef = useRef<{ submitForm: () => void }>(null);
  const [rents, setRents] = useState<Rent[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const [selectedRent, setSelectedRent] = useState<Rent | null>(null);
  const [isCancelRentModalOpen, setIsCancelRentModalOpen] = useState(false);
  const [isMakeRentModalOpen, setIsMakeRentModalOpen] = useState(false);
  const { data: session, status } = useSession();

  const [isCheckOutRentModalOpen, setIsCheckOutRentModalOpen] = useState(false);
  const [selectedCheckOutRent, setSelectedCheckOutRent] = useState<Rent | null>(
    null
  );
  const [distance, setDistance] = useState<number>(0);
  const [fuelLevel, setFuelLevel] = useState<number>(0);

  const fetchData = async () => {
    setIsLoading(true);
    if (status === "authenticated") {
      const rentsResponse = await getAllRents(session?.user.token);
      if (rentsResponse.ok) {
        const rents = await rentsResponse.json();
        setIsLoading(false);
        setRents(rents);
      } else {
        setIsLoading(false);
        console.error("Failed to fetch rents");
      }
    }
  };

  const checkIn = async (id: number) => {
    await checkInRent(id, session?.user.token);
    fetchData();
  };

  useEffect(() => {
    if (status === "authenticated") {
      fetchData();
    }
  }, [status]);

  useInterval(() => {
    fetchData();
  }, 2000);

  const openCancelRentModal = (rent: Rent) => {
    setSelectedRent(rent);
    setIsCancelRentModalOpen(true);
  };

  const closeCancelRentModal = () => {
    setIsCancelRentModalOpen(false);
  };

  const handleCancelRent = async () => {
    if (selectedRent !== null) {
      await cancelRent(selectedRent.id, session?.user.token);
      fetchData();

      closeCancelRentModal();
    }
  };

  //checkout
  const openCheckOutRentModal = (rent: Rent) => {
    setSelectedCheckOutRent(rent);
    setIsCheckOutRentModalOpen(true);
  };

  const closeCheckOutRentModal = () => {
    setIsCheckOutRentModalOpen(false);
  };

  const handelCheckOut = async () => {
    if (selectedCheckOutRent !== null) {
      await checkOutRent(
        selectedCheckOutRent?.id,
        session?.user.token,
        distance,
        fuelLevel
      );
      fetchData();
      closeCheckOutRentModal();
    }
  };

  const openMakeRentModal = (rent: Rent) => {
    setSelectedRent(rent);
    setIsMakeRentModalOpen(true);
  };

  const closeMakeRentModal = () => {
    setIsMakeRentModalOpen(false);
  };

  const handleMakeRent = async () => {
    if (formRef.current) {
      formRef.current.submitForm(); // You need to implement submitForm method in MakeRentForm

      closeCancelRentModal();
    }
  };

  return (
    <>
      <main className="mt-20">
        <SearchRent setRents={setRents} />

        <div className="flex justify-center mx-8 mt-4">
          {rents?.length > 0 && (
            <table id="table" className="w-90 text-sm text-left">
              <thead className="text-xs text-gray-500 uppercase bg-gray-200 dark:text-gray-400">
                {" "}
                <tr>
                  <th className="px-6 py-3">{t("rental.car")}</th>
                  <th className="px-6 py-3">{t("rental.startDate")}</th>
                  <th className="px-6 py-3">{t("rental.endDate")}</th>
                  <th className="px-6 py-3">{t("rental.ownerEmail")}</th>
                  <th className="px-6 py-3">{t("rent.emailReceiver")}</th>
                  <th className="px-6 py-3"></th>
                </tr>
              </thead>

              <tbody>
                {rents?.map((rent: Rent, index: number) => (
                  <tr
                    key={index}
                    className=" border-b bg-gray-150 border-white"
                  >
                    <td className="px-6 py-4">
                      {rent?.rental?.car?.brand} {rent?.rental?.car?.model}{" "}
                      {rent?.rental?.car?.licensePlate}
                    </td>
                    <td className="px-6 py-4">
                      {new Date(rent.startDate).toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4">
                      {new Date(rent.endDate).toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4">{rent.ownerEmail}</td>
                    <td className="px-6 py-4">{rent.renterEmail}</td>

                    <td className="px-6 py-4">
                      <button
                        className="rounded-md bg-red-600 text-white px-2 py-1 hover:bg-red-800 focus:outline-none focus:ring-2 focus:ring-red-600 focus:ring-opacity-50"
                        onClick={() => openCancelRentModal(rent)}
                      >
                        {t("system.cancelRental.button")}
                      </button>
                    </td>
                    {/* ------------------------------------------------------------------------------------------ */}
                    {/* ------------------------------------------------------------------------------------------ */}
                    {/* ------------------------------------------------------------------------------------------ */}
                    {session?.user.roles.includes("RENTER") &&
                      !rent.checkInStatus && (
                        <td>
                          <button
                            className="rounded-md bg-blue-600 text-white px-2 py-1 hover:bg-red-800 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50"
                            onClick={() => checkIn(rent.id)}
                          >
                            CheckIn
                          </button>
                        </td>
                      )}
                    {session?.user.roles.includes("RENTER") &&
                      rent.checkInStatus &&
                      !rent.checkOutDate && (
                        <td>
                          <button
                            className="rounded-md bg-blue-600 text-white px-2 py-1 hover:bg-red-800 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50"
                            onClick={() => openCheckOutRentModal(rent)}
                          >
                            CheckOut
                          </button>
                        </td>
                      )}
                  </tr>
                ))}
              </tbody>
              {isLoading && !rents && <p>{t("system.loading")}</p>}
              {!isLoading && rents.length < 1 && (
                <h1 className="text-center text-red-700 text-2xl p-10">
                  {t("error.noRents")}
                </h1>
              )}
            </table>
          )}
        </div>
        <Modal
          isOpen={isCancelRentModalOpen}
          onClose={closeCancelRentModal}
          onConfirm={handleCancelRent} // Pass the delete handler as the confirmation action
          confirmColor="red" // Optional: Customize the confirmation button color
          confirmText={t("modal.yes")} // Optional: Customize the confirmation button text
          abortColor="blue" // Optional: Customize the abort button color
          abortText={t("modal.no")} // Optional: Customize the abort button text
        >
          <div className="">
            <h1 className="text-lg font-bold">
              {t("system.cancelRent.title")}
            </h1>
            <h2>{t("system.cancelRental.message")}</h2>
            <p>
              {t("system.cancelRent.rentOf")}{" "}
              {String(selectedRent?.rental.car.brand)}{" "}
              {String(selectedRent?.rental.car.model)}{" "}
              {String(selectedRent?.rental.car.licensePlate)}
            </p>
            <p className="mb-2">
              {t("system.cancelRent.on")} {String(selectedRent?.startDate)} -
              {String(selectedRent?.endDate)} ?
            </p>
          </div>
        </Modal>

        {/* checkoutmodel */}
        <Modal
          isOpen={isCheckOutRentModalOpen}
          onClose={closeCheckOutRentModal}
          onConfirm={handelCheckOut}
          confirmColor="blue"
          confirmText="Confirm"
          abortColor="red"
          abortText="Cancel"
        >
          <div className="">
            <h1 className="text-lg font-bold">CheckOut Rent</h1>
            <form>
              <label htmlFor="distanceInput">Distance</label>
              <input
                id="distanceInput"
                type="number"
                value={distance}
                onChange={(event) => setDistance(Number(event.target.value))}
                className="w-full border border-gray-300 p-2 rounded-md"
                min={0} // Set the minimum value to 1
              />
            </form>
            <form>
              <label htmlFor="fuelLevelInput">Fuel Level</label>
              <input
                id="fuelLevelInput"
                type="number"
                value={fuelLevel}
                onChange={(event) => setFuelLevel(Number(event.target.value))}
                className="w-full border border-gray-300 p-2 rounded-md"
                min={0} // Set the minimum value to 1
              />
            </form>
          </div>
        </Modal>
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

export default Rents;
