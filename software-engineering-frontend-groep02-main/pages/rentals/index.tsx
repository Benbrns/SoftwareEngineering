import React, { useState, useEffect, useRef, use } from "react";
import { Rental } from "@/types";
import { cancelRental, getAllRentals } from "@/services/rentalService";
import SearchRental from "@/components/rental/searchRental";
import dynamic from 'next/dynamic';
const Map = dynamic(() => import('@/components/Map'), { ssr: false });
import Modal from "@/components/ui/modal";
import MakeRentForm from "@/components/rent/MakeRentForm";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from "next-i18next";
import { useSession } from "next-auth/react";
import useInterval from "use-interval";
import { Label } from "@/components/ui/label";
import { Separator } from "@/components/ui/separator";

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogClose,
} from "@/components/ui/dialog";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { Send } from "lucide-react";
import { DateRange } from "react-day-picker";
import { DatePickerWithRange } from "@/components/rental/DatePickerWithRange";
import { differenceInCalendarDays, set } from "date-fns";
import { difference } from "next/dist/build/utils";
import { Slider } from "@/components/ui/slider";
import { Input } from "@/components/ui/input";

const Rentals: React.FC = () => {
  const { t } = useTranslation();
  const formRef = useRef<{ submitForm: () => void }>(null);
  const [rentals, setRentals] = useState<Rental[]>([]);
  const [otherRentals, setOtherRentals] = useState<Rental[]>([]);
  const [ownerRentals, setOwnerRentals] = useState<Rental[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { data: session, status } = useSession();
  const [date, setDate] = React.useState<DateRange | undefined>();
  const [days, setDays] = useState<number>(0);
  const [totalPrice, setTotalPrice] = useState<number | undefined>();
  const [remainingFuel, setRemainingFuel] = useState<number>(100);
  const [drivenKm, setDrivenKm] = useState<number>(0);

  const isRenter = session?.user.roles?.includes("RENTER");
  const isOwner = session?.user.roles?.includes("OWNER");

  const [selectedRental, setSelectedRental] = useState<Rental | null>(null);
  const [isCancelRentalModalOpen, setIsCancelRentalModalOpen] = useState(false);
  const [isMakeRentModalOpen, setIsMakeRentModalOpen] = useState(false);

  const fetchData = async () => {
    setIsLoading(true);
    if (status === "authenticated") {
      const rentalsResponse = await getAllRentals(session?.user.token);
      if (rentalsResponse.ok) {
        const rentals = await rentalsResponse.json();
        setIsLoading(false);
        setRentals(rentals);
        console.log(rentals);
        setOwnerRentals(
          rentals.filter(
            (rental: Rental) => rental.ownerEmail === session?.user.email
          )
        );
        setOtherRentals(
          rentals.filter(
            (rental: Rental) => rental.ownerEmail !== session?.user.email
          )
        );
      } else {
        setIsLoading(false);
        console.error("Failed to fetch rentals");
      }
    }
  };

  useEffect(() => {
    if (selectedRental) {
      if (date && date.from && date.to) {
        const dayCount = differenceInCalendarDays(date.to, date.from);
        setDays(dayCount);
      }
      if (selectedRental?.basePrice) {
        setTotalPrice(
          selectedRental.basePrice +
            selectedRental.pricePerDay * days +
            selectedRental.fuelPenaltyPrice * (1 - remainingFuel / 100) +
            selectedRental.pricePerKm * drivenKm
        );
      }
    }
  }, [
    days,
    date,
    selectedRental?.basePrice,
    selectedRental?.pricePerKm,
    selectedRental?.fuelPenaltyPrice,
    selectedRental?.pricePerDay,
    drivenKm,
    remainingFuel,
  ]);

  useEffect(() => {
    if (status === "authenticated") {
      fetchData();
    }
  }, [status]);

  useInterval(() => {
    fetchData();
  }, 2000);

  const openCancelRentalModal = (rental: Rental) => {
    setSelectedRental(rental);
    setIsCancelRentalModalOpen(true);
  };

  const closeCancelRentalModal = () => {
    setIsCancelRentalModalOpen(false);
  };

  const handleCancelRental = async () => {
    if (selectedRental !== null) {
      await cancelRental(selectedRental.id, session?.user.token);
      fetchData();

      closeCancelRentalModal();
    }
  };

  const openMakeRentModal = (rental: Rental) => {
    setSelectedRental(rental);
    setIsMakeRentModalOpen(true);
  };

  const closeMakeRentModal = () => {
    setIsMakeRentModalOpen(false);
  };

  const handleMakeRent = async () => {
    if (formRef.current) {
      formRef.current.submitForm(); // You need to implement submitForm method in MakeRentForm

      closeCancelRentalModal();
    }
  };

  return (
    <>
      <main className="mt-20">
        <SearchRental setRentals={setRentals} />
        {isOwner && (
          <>
            {/* Table for owner's rentals */}
            <h2 className="text-center text-2xl p-10">
              {t("rental.ownerRentals")}
            </h2>
            <div className="flex justify-center mx-8 mt-4">
              <table className="w-90 text-sm text-left">
                <thead className="text-xs text-gray-500 uppercase bg-gray-200 dark:text-gray-400">
                  <tr>
                    <th className="px-6 py-3">{t("rental.car")}</th>
                    <th className="px-6 py-3">{t("rental.startDate")}</th>
                    <th className="px-6 py-3">{t("rental.endDate")}</th>
                    <th className="px-6 py-3">{t("rental.pickupCity")}</th>
                    <th className="px-6 py-3">{t("rental.ownerEmail")}</th>
                    <th className="px-6 py-3">{t("rental.basePrice")}</th>
                    <th className="px-6 py-3">{t("rental.pricePerKm")}</th>
                    <th className="px-6 py-3">
                      {t("rental.fuelPenaltyPrice")}
                    </th>
                    <th className="px-6 py-3">{t("rental.pricePerDay")}</th>
                    <th className="px-6 py-3"></th>
                  </tr>
                </thead>
                <tbody>
                  {ownerRentals.map((rental: Rental, index: number) => (
                    <tr
                      key={index}
                      className=" border-b bg-gray-150 border-white">
                      <td className="px-6 py-4">
                        {rental?.car?.brand} {rental?.car?.model}{" "}
                        {rental?.car?.licensePlate}
                      </td>
                      <td className="px-6 py-4">
                        {new Date(rental.startDate).toLocaleDateString()}
                      </td>
                      <td className="px-6 py-4">
                        {new Date(rental.endDate).toLocaleDateString()}
                      </td>
                      <td className="px-6 py-4">{rental.city}</td>
                      <td className="px-6 py-4">{rental.ownerEmail}</td>
                      <td className="px-6 py-4">
                        €{rental?.basePrice.toFixed(2)}
                      </td>
                      <td className="px-6 py-4">
                        €{rental?.pricePerKm.toFixed(2)}
                      </td>
                      <td className="px-6 py-4">
                        €{rental?.fuelPenaltyPrice.toFixed(2)}
                      </td>
                      <td className="px-6 py-4">
                        €{rental?.pricePerDay.toFixed(2)}
                      </td>

                      <td className="px-6 py-4">
                        {session?.user.email == rental.ownerEmail &&
                          rental.hasRents == 0 && (
                            <button
                              className="rounded-md bg-red-600 text-white px-2 py-1 hover:bg-red-800 focus:outline-none focus:ring-2 focus:ring-red-600 focus:ring-opacity-50"
                              onClick={() => openCancelRentalModal(rental)}>
                              {t("system.cancelRental.button")}
                            </button>
                          )}
                      </td>
                    </tr>
                  ))}
                </tbody>
                {isLoading && !ownerRentals && <p>{t("system.loading")}</p>}

                {!isLoading && ownerRentals.length < 1 && (
                  <h1 className="text-center text-red-700 text-2xl p-10">
                    {t("error.noRentals")}
                  </h1>
                )}
              </table>
            </div>
          </>
        )}

        {/* Table for other rentals */}
        <h2 className="text-center text-2xl p-10">
          {t("rental.otherRentals")}
        </h2>
        <div className="flex justify-center mx-8 mt-4">
          <table className="w-90 text-sm text-left">
            <thead className="text-xs text-gray-500 uppercase bg-gray-200 dark:text-gray-400">
              <tr>
                <th className="px-6 py-3">{t("rental.car")}</th>
                <th className="px-6 py-3">{t("rental.startDate")}</th>
                <th className="px-6 py-3">{t("rental.endDate")}</th>
                <th className="px-6 py-3">{t("rental.pickupCity")}</th>
                <th className="px-6 py-3">{t("rental.ownerEmail")}</th>
                <th className="px-6 py-3">{t("rental.basePrice")}</th>
                <th className="px-6 py-3">{t("rental.pricePerKm")}</th>
                <th className="px-6 py-3">{t("rental.fuelPenaltyPrice")}</th>
                <th className="px-6 py-3">{t("rental.pricePerDay")}</th>
                <th className="px-6 py-3"></th>
                <th className="px-6 py-3"></th>
              </tr>
            </thead>
            <tbody>
              {otherRentals.map((rental, index) => (
                <tr key={index} className=" border-b bg-gray-150 border-white">
                  <td className="px-6 py-4">
                    {rental?.car?.brand} {rental?.car?.model}{" "}
                    {rental?.car?.licensePlate}
                  </td>
                  <td className="px-6 py-4">
                    {new Date(rental.startDate).toLocaleDateString()}
                  </td>
                  <td className="px-6 py-4">
                    {new Date(rental.endDate).toLocaleDateString()}
                  </td>
                  <td className="px-6 py-4">{rental.city}</td>
                  <td className="px-6 py-4">{rental.ownerEmail}</td>
                  <td className="px-6 py-4">€{rental?.basePrice.toFixed(2)}</td>
                  <td className="px-6 py-4">
                    €{rental?.pricePerKm.toFixed(2)}
                  </td>
                  <td className="px-6 py-4">
                    €{rental?.fuelPenaltyPrice.toFixed(2)}
                  </td>
                  <td className="px-6 py-4">
                    €{rental?.pricePerDay.toFixed(2)}
                  </td>
                  <td className="px-6 py-4">
                    <Dialog>
                      <DialogTrigger>
                        <Button
                          onClick={() => setSelectedRental(rental)}
                          variant="secondary">
                          {t("system.makeSimulation.button")}
                        </Button>
                      </DialogTrigger>
                      <DialogContent className="sm:max-w-[75%] xl:max-w-fit">
                        <DialogHeader>
                          <DialogTitle>
                            {t("system.makeSimulation.title")}
                          </DialogTitle>
                          <DialogDescription>
                            {t("system.makeSimulation.description")}
                          </DialogDescription>
                        </DialogHeader>
                        <div className="py-4">
                          <Table>
                            <TableHeader>
                              <TableRow>
                                <TableHead className="w-[103px]">
                                  {t("system.makeSimulation.price.base")}
                                </TableHead>
                                <TableHead className="w-[125px]">
                                  {t("system.makeSimulation.price.km")}
                                </TableHead>
                                <TableHead className="w-[100px]">
                                  {t("system.makeSimulation.price.fuel")}
                                </TableHead>
                                <TableHead className="w-[100px]">
                                  {t("system.makeSimulation.price.day")}
                                </TableHead>
                              </TableRow>
                            </TableHeader>
                            <TableBody>
                              <TableRow>
                                <TableCell>
                                  €
                                  {String(selectedRental?.basePrice.toFixed(2))}
                                </TableCell>
                                <TableCell>
                                  €
                                  {String(
                                    selectedRental?.pricePerKm.toFixed(2)
                                  )}
                                </TableCell>
                                <TableCell>
                                  €
                                  {String(
                                    selectedRental?.fuelPenaltyPrice.toFixed(2)
                                  )}
                                </TableCell>
                                <TableCell>
                                  €
                                  {String(
                                    selectedRental?.pricePerDay.toFixed(2)
                                  )}
                                </TableCell>
                              </TableRow>
                            </TableBody>
                          </Table>

                          <Separator />

                          <div className="mt-6 grid grid-flow-col grid-rows-2 gap-x-11">
                            <DatePickerWithRange
                              date={date}
                              setDate={setDate}
                            />
                            <p className="mt-1">
                              {t("system.makeSimulation.days")} {days}
                            </p>

                            <div className="col-end-3">
                              <Label>
                                {t("system.makeSimulation.remainingFuel")}
                              </Label>
                              <div className="flex gap-3 w-[100%]">
                                <Slider
                                  onValueChange={(value) =>
                                    setRemainingFuel(value[0])
                                  }
                                  defaultValue={[remainingFuel]}
                                  max={100}
                                  step={1}
                                />
                                <p>{remainingFuel}%</p>
                              </div>
                            </div>

                            <div className="grid w-full max-w-[100%] items-center gap-1.5 col-end-3">
                              <Label>
                                {t("system.makeSimulation.drivenKm")}
                              </Label>
                              <Input
                                value={drivenKm}
                                onChange={(e) => setDrivenKm(+e.target.value)}
                                type="number"
                                min={0}
                                id="km"
                                placeholder=""
                              />
                            </div>
                          </div>
                          <div className="bg-gray-200  dark:text-primary-foreground rounded-md mt-6 p-2 flex">
                            <p>{t("system.makeSimulation.totalPrice")} </p>
                            <p>€{totalPrice?.toFixed(2)}</p>
                          </div>
                        </div>
                        <DialogFooter>
                          <DialogClose>
                            <Button variant="outline">
                              {t("system.makeSimulation.close")}
                            </Button>
                          </DialogClose>
                          <DialogClose>
                            <Button
                              onClick={() => openMakeRentModal(rental)}
                              type="submit">
                              <Send className="mr-2 h-4 w-4" />
                              {t("system.makeSimulation.makeRent")}
                            </Button>
                          </DialogClose>
                        </DialogFooter>
                      </DialogContent>
                    </Dialog>
                  </td>
                  <td className="px-6 py-4">
                    {session?.user.email !== rental.ownerEmail && (
                      <button
                        className="rounded-md bg-blue-600 text-white px-2 py-1 hover:bg-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50"
                        onClick={() => openMakeRentModal(rental)}>
                        {t("system.makeRent.button")}
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
            {isLoading && !otherRentals && <p>{t("system.loading")}</p>}

            {!isLoading && otherRentals.length < 1 && (
              <h1 className="text-center text-red-700 text-2xl p-10">
                {t("error.noRentals")}
              </h1>
            )}
          </table>
        </div>

        <Modal
          isOpen={isCancelRentalModalOpen}
          onClose={closeCancelRentalModal}
          onConfirm={handleCancelRental} // Pass the delete handler as the confirmation action
          confirmColor="red" // Optional: Customize the confirmation button color
          confirmText={t("modal.yes")} // Optional: Customize the confirmation button text
          abortColor="blue" // Optional: Customize the abort button color
          abortText={t("modal.no")} // Optional: Customize the abort button text
        >
          <h2>{t("system.cancelRental.message")}</h2>
          <p>{t("system.cancelRental.for")}</p>
          <p>
            {String(selectedRental?.car.brand)}{" "}
            {String(selectedRental?.car.model)}{" "}
            {String(selectedRental?.car.licensePlate)}
          </p>
          <p>
            {t("system.rentFrom")} {String(selectedRental?.startDate)}{" "}
            {t("system.rentUntil")} {String(selectedRental?.endDate)}{" "}
          </p>
        </Modal>
        <Modal
          isOpen={isMakeRentModalOpen}
          onClose={closeMakeRentModal}
          onConfirm={handleMakeRent} // Pass the delete handler as the confirmation action
          confirmColor="blue" // Optional: Customize the confirmation button color
          confirmText={t("system.makeRent.button")} // Optional: Customize the confirmation button text
          abortColor="red" // Optional: Customize the abort button color
          abortText={t("modal.abort")} // Optional: Customize the abort button text
        >
          <h2 className="text-2xl font-bold mb-4">
            {t("system.makeRent.title")}
          </h2>
          <h3 className="text-xl font-semibold mb-2">{t("rental.car")}</h3>
          <p className="mb-4">
            {String(selectedRental?.car.brand)}{" "}
            {String(selectedRental?.car.model)}{" "}
            {String(selectedRental?.car.licensePlate)}
          </p>
          <h3 className="text-xl font-semibold mb-2">
            {t("system.makeRent.period")}
          </h3>
          <p className="mb-4">
            {t("system.rentFrom")} {String(selectedRental?.startDate)}{" "}
            {t("system.rentUntil")} {String(selectedRental?.endDate)}
          </p>
          {selectedRental && (
            <MakeRentForm ref={formRef} rental={selectedRental} />
          )}
        </Modal>
        
      </main>
      <Map  rentals={rentals}/>
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

export default Rentals;
