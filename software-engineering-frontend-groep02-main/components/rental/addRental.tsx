import { useRouter } from "next/navigation";
import { use, useEffect, useState } from "react";
import InputField from "../InputField";
import InputSubmit from "../InputSubmit";
import { addRentalCar } from "@/services/rentalService";
import { RentalInput } from "@/types";
import { useTranslation, UseTranslation } from "next-i18next";
import { useSession } from "next-auth/react";

type Props = {
  carId: number;
};

const AddRental: React.FC<Props> = ({ carId }: Props) => {
  const { t } = useTranslation();
  const router = useRouter();
  const [ErrorCityMessage, setErrorCityMessage] = useState<string>("");
  const [ErrorEndDateMessage, setErrorEndDateMessage] = useState<string>("");
  const [ErrorStartDateMessage, setErrorStartDateMessage] =
    useState<string>("");
  const [ErrorPhoneNumberMessage, setErrorPhoneNumberMessage] =
    useState<string>("");
  const [ErrorEmailMessage, setErrorEmailMessage] = useState<string>("");
  const [ErrorBasePriceMessage, setErrorBasePriceMessage] =
    useState<string>("");
  const [ErrorPricePerKmMessage, setErrorPricePerKmMessage] =
    useState<string>("");
  const [ErrorFuelPenaltyPriceMessage, setErrorFuelPenaltyPriceMessage] =
    useState<string>("");
  const [ErrorPricePerDayMessage, setErrorPricePerDayMessage] =
    useState<string>("");

  const [startDate, setStartDate] = useState<Date>(new Date());
  const [endDate, setEndDate] = useState<Date>(new Date());
  const [city, setCity] = useState<string>("");
  const [phoneNumber, setPhoneNumber] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [basePrice, setBasePrice] = useState<number>();
  const [pricePerKm, setPricePerKm] = useState<number>();
  const [fuelPenaltyPrice, setFuelPenaltyPrice] = useState<number>();
  const [pricePerDay, setPricePerDay] = useState<number>();

  const { data: session } = useSession();

  const addRental = async () => {
    let rental: RentalInput = {
      startDate: startDate,
      endDate: endDate,
      city: city,
      phoneNumber: phoneNumber,
      email: email,
    };

    return await addRentalCar(rental, carId, session?.user.token);
  };

  const validate = () => {
    let result: boolean = false;
    const startDateString = makeDate(startDate);
    const endDateString = makeDate(endDate);
    if (startDate === null) {
      setErrorStartDateMessage(t("error.noStartDate"));
      result = true;
    }
    const pattern: RegExp =
      /^(0[1-9]|[1-2][0-9]|3[0-1])\/(0[1-9]|1[0-2])\/\d{4}$/;
    if (!pattern.test(startDateString)) {
      setErrorStartDateMessage(t("error.invalid.date"));
      result = true;
    }
    if (!pattern.test(endDateString)) {
      setErrorStartDateMessage(t("error.invalid.date"));
      result = true;
    }
    if (startDate <= new Date()) {
      setErrorStartDateMessage(t("error.invalid.startDateNoFuture"));
      result = true;
    }

    if (endDate <= startDate) {
      setErrorEndDateMessage(t("error.invalid.endDateBeforeStartDate"));
      result = true;
    }
    if (endDate === null) {
      setErrorEndDateMessage(t("error.noEndDate"));
      result = true;
    }
    if (endDate <= new Date()) {
      setErrorEndDateMessage(t("error.invalid.endDateNoFuture"));
      result = true;
    }
    if (city === "") {
      setErrorCityMessage(t("error.noCity"));
      result = true;
    }
    if (phoneNumber === "") {
      setErrorPhoneNumberMessage(t("error.noPhone"));
      result = true;
    }
    if (email === "") {
      setErrorEmailMessage(t("error.noEmail"));
      result = true;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setErrorEmailMessage(t("error.invalid.email"));
      result = true;
    }
    if (basePrice === undefined) {
      setErrorBasePriceMessage(t("error.noPrice.base"));
      result = true;
    }
    if (pricePerKm === undefined) {
      setErrorPricePerKmMessage(t("error.noPrice.perKm"));
      result = true;
    }
    if (fuelPenaltyPrice === undefined) {
      setErrorFuelPenaltyPriceMessage(t("error.noPrice.fuelPenalty"));
      result = true;
    }
    if (pricePerDay === undefined) {
      setErrorPricePerDayMessage(t("error.noPrice.perDay"));
      result = true;
    }

    return result;
  };

  const handleFormSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorCityMessage("");
    setErrorEmailMessage("");
    setErrorEndDateMessage("");
    setErrorPhoneNumberMessage("");
    setErrorStartDateMessage("");
    setErrorBasePriceMessage("");
    setErrorPricePerKmMessage("");
    setErrorFuelPenaltyPriceMessage("");
    setErrorPricePerDayMessage("");
    console.log(basePrice);

    if (validate()) {
      return;
    }

    const response = await addRental();
    if (response.ok) {
      router.push("/rentals");
    }
    if (response.status === 400) {
      const data = await response.json();
    }
  };

  const makeDate = (value: Date) => {
    const day = String(value.getDate()).padStart(2, "0");
    const month = String(value.getMonth() + 1).padStart(2, "0"); // January is 0!
    const year = value.getFullYear();
    return day + "/" + month + "/" + year;
  };

  return (
    <div className="max-w-md mx-auto mt-8">
      <form
        onSubmit={handleFormSubmit}
        className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <InputField
          type="date"
          label={t("rental.startDate")}
          value={startDate ? startDate.toISOString().split("T")[0] : ""} // Convert startDate to string
          name="startDate"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setStartDate(new Date(event.target.value))
          }
          id="startDate"
        />
        {ErrorStartDateMessage && (
          <p className="text-red-500 text-xs italic">{ErrorStartDateMessage}</p>
        )}
        <InputField
          type="date"
          label={t("rental.endDate")}
          value={endDate ? endDate.toISOString().split("T")[0] : ""} // Convert endDate to string
          name="endDate"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setEndDate(new Date(event.target.value))
          }
          id="endDate"
        />
        {ErrorEndDateMessage && (
          <p className="text-red-500 text-xs italic">{ErrorEndDateMessage}</p>
        )}
        <InputField
          label={t("rental.pickupCity")}
          value={city}
          name="type"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setCity(event.target.value)
          }
          id="city"
        />
        {ErrorCityMessage && (
          <p className="text-red-500 text-xs italic">{ErrorCityMessage}</p>
        )}
        <InputField
          label={t("rent.phoneNumber")}
          value={phoneNumber}
          name="phoneNumber"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setPhoneNumber(event.target.value)
          }
          id="phoneNumber"
        />
        {ErrorPhoneNumberMessage && (
          <p className="text-red-500 text-xs italic">
            {ErrorPhoneNumberMessage}
          </p>
        )}
        <InputField
          label={t("rent.email")}
          value={email}
          name="email"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setEmail(event.target.value)
          }
          id="email"
        />
        {ErrorEmailMessage && (
          <p className="text-red-500 text-xs italic">{ErrorEmailMessage}</p>
        )}
        <InputField
          type="number"
          label={t("rental.basePrice")}
          value={basePrice?.toString() || ""}
          name="basePrice"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setBasePrice(Number(event.target.value))
          }
          id="basePrice"
        />
        {ErrorBasePriceMessage && (
          <p className="text-red-500 text-xs italic">{ErrorBasePriceMessage}</p>
        )}
        <InputField
          type="number"
          label={t("rental.pricePerKm")}
          value={pricePerKm?.toString() || ""}
          name="pricePerKm"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setPricePerKm(Number(event.target.value))
          }
          id="pricePerKm"
        />
        {ErrorPricePerKmMessage && (
          <p className="text-red-500 text-xs italic">
            {ErrorPricePerKmMessage}
          </p>
        )}
        <InputField
          type="number"
          label={t("rental.fuelPenaltyPrice")}
          value={fuelPenaltyPrice?.toString() || ""}
          name="fuelPenaltyPrice"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setFuelPenaltyPrice(Number(event.target.value))
          }
          id="fuelPenaltyPrice"
        />
        {ErrorFuelPenaltyPriceMessage && (
          <p className="text-red-500 text-xs italic">
            {ErrorFuelPenaltyPriceMessage}
          </p>
        )}
        <InputField
          type="number"
          label={t("rental.pricePerDay")}
          value={pricePerDay?.toString() || ""}
          name="pricePerDay"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setPricePerDay(Number(event.target.value))
          }
          id="pricePerDay"
        />
        {ErrorPricePerDayMessage && (
          <p className="text-red-500 text-xs italic">
            {ErrorPricePerDayMessage}
          </p>
        )}
        <div className="mt-4">
          <InputSubmit
            onClick={handleFormSubmit}
            value={t("system.addRental")}
          />
        </div>
      </form>
    </div>
  );
};

export default AddRental;
