import React, { useState } from "react";
import { deleteCar } from "@/services/carService";
import { Car } from "@/types";
import { mutate } from "swr";
import Modal from "../ui/modal";
import { useTranslation } from "next-i18next";
import { useSession } from "next-auth/react";

type Props = {
  renting: number;
  setRenting: (carId: number) => void;
  cars: Car[];
  isLoading: boolean;
};

const CarOverview: React.FC<Props> = ({
  cars,
  isLoading,
  setRenting,
  renting,
}: Props) => {
  const { t } = useTranslation();
  const [selectedCar, setSelectedCar] = useState<Car | null>(null);
  const [isModalOpen, setModalOpen] = useState(false);
  const { data: session, status } = useSession();

  const handleRental = (carId: number) => {
    setRenting(carId);
  };

  const handleDelete = async () => {
    if (selectedCar !== null) {
      await deleteCar(selectedCar.id, session?.user.token);
      closeModal();
    }
  };

  const openModal = (car: Car) => {
    setSelectedCar(car);
    setModalOpen(true);
  };

  const closeModal = () => setModalOpen(false);

  return (
    <section>
      {isLoading && <p>{t("system.loading")}</p>}
      {cars?.length < 1 && (
        <h1 className="text-center text-red-700 text-2xl p-10">
          {t("error.noCars")}
        </h1>
      )}
      <div className="flex justify-center mx-8 mt-4">
        <table className="w-90 text-sm text-left">
          <thead className="text-xs text-gray-500 uppercase bg-gray-200 dark:text-gray-400">
            <tr>
              <th className="px-6 py-3">{t("car.brand")}</th>
              <th className="px-6 py-3">{t("car.model")}</th>
              <th className="px-6 py-3">{t("car.licencePlate")}</th>
              <th className="px-6 py-3"></th>
              <th className="px-6 py-3"></th>
            </tr>
          </thead>
          <tbody>
            {cars?.map((car: Car, index: number) => (
              <tr key={index} className="border-b bg-gray-150 border-white">
                <td className="px-6 py-4 text-gray-500">{car.brand}</td>
                <td className="px-6 py-4">{car.model}</td>
                <td className="px-6 py-4">{car.licensePlate}</td>
                {car.rentals && car.rentals.length !== 0 ? (
                  <>
                    {/* 
                      This is a cancel button can be implemented in the future
                      <td>
                        <button
                          className="rounded-md bg-green-600 text-white px-2 py-1 hover:bg-green-800 focus:outline-none focus:ring-2 focus:ring-green-600 focus:ring-opacity-50"
                          onClick={() => setRenting(0)}>
                          Cancel
                        </button>
                      </td> 
                      
                      */}
                  </>
                ) : (
                  <>
                    <td>
                      <button
                        className="rounded-md bg-blue-600 text-white px-2 py-1 hover:bg-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50"
                        onClick={() => handleRental(car.id)}>
                        {t("system.addRental")}
                      </button>
                    </td>
                    <td>
                      <button
                        className="rounded-md bg-red-600 text-white px-2 py-1 hover:bg-red-800 focus:outline-none focus:ring-2 focus:ring-red-600 focus:ring-opacity-50"
                        onClick={() => openModal(car)}>
                        {t("system.deleteCar.button")}
                      </button>
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {cars.length < 1 && (
        <h1 className="text-center text-red-700 text-2xl p-10">
          {t("error.noRentals")}
        </h1>
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={closeModal}
        onConfirm={handleDelete} // Pass the delete handler as the confirmation action
        confirmColor="red" // Optional: Customize the confirmation button color
        confirmText={t("modal.yes")} // Optional: Customize the confirmation button text
        abortColor="blue" // Optional: Customize the abort button color
        abortText={t("modal.no")} // Optional: Customize the abort button text
      >
        <div className="">
          <h1 className="text-lg font-bold">{t("system.deleteCar.title")}</h1>
          <h2>{t("system.deleteCar.message")}</h2>

          <p className="mb-2">
            {selectedCar?.brand} {selectedCar?.model}{" "}
            {selectedCar?.licensePlate} ?
          </p>
        </div>
      </Modal>
    </section>
  );
};

export default CarOverview;
