import React, {
  RefObject,
  forwardRef,
  useImperativeHandle,
  useRef,
  useState,
  useTransition,
} from "react";
import { useRouter } from "next/router"; // Note: Correct import for useRouter
import { makeRent } from "@/services/rentService";
import InputField from "../InputField";
import InputSubmit from "../InputSubmit";
import { Rental } from "@/types";
import { useTranslation, UseTranslation } from "next-i18next";
import { useSession } from "next-auth/react";

interface Props {
  rental: Rental;
}

interface MakeRentFormHandles {
  submitForm: () => void;
}

const MakeRentForm = forwardRef<MakeRentFormHandles, Props>((props, ref) => {
  const { t } = useTranslation();
  const formRef: RefObject<MakeRentFormHandles> = useRef(null);
  const { rental } = props;
  const router = useRouter();
  const [errorPhoneNumberMessage, setErrorPhoneNumberMessage] =
    useState<string>("");
  const [errorEmailMessage, setErrorEmailMessage] = useState<string>("");
  const [errorRegisterNumberMessage, setErrorRegisterNumberMessage] =
    useState<string>("");
  const [errorBirthDateMessage, setErrorBirthDateMessage] =
    useState<string>("");
  const [
    errorDrivingLicenceNumberMessage,
    setErrorDrivingLicenceNumberMessage,
  ] = useState<string>("");

  const [phoneNumber, setPhoneNumber] = useState("");
  const [email, setEmail] = useState("");
  const [registerNumber, setRegisterNumber] = useState("");
  const [birthDate, setBirthDate] = useState<Date | null>(null);
  const [drivingLicenceNumber, setDrivingLicenceNumber] = useState("");

  const { data: session } = useSession();

  const validate = () => {
    let result: boolean = false;

    if (phoneNumber === "") {
      setErrorPhoneNumberMessage(t("error.noPhone"));
      result = true;
    }
    if (email === "") {
      setErrorEmailMessage(t("error.noEmail"));
      result = true;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setErrorEmailMessage(t("error.invalid.email"));
      result = true;
    }
    if (registerNumber === "") {
      setErrorRegisterNumberMessage(t("error.noRegisterNumber"));
      result = true;
    }
    if (!/^\d{2}\.\d{2}\.\d{2}-\d{3}\.\d{2}$/.test(registerNumber)) {
      setErrorRegisterNumberMessage(t("error.invalid.registerNumber"));
      result = true;
    }
    if (!birthDate) {
      setErrorBirthDateMessage(t("error.noBithDate"));
      result = true;
    }
    if (drivingLicenceNumber === "") {
      setErrorDrivingLicenceNumberMessage(t("error.noDrivingLicenceNumber"));
      result = true;
    }
    if (!/^[0-9]{10}$/.test(drivingLicenceNumber)) {
      setErrorDrivingLicenceNumberMessage(
        t("error.invalid.drivingLicenceNumber")
      );
      result = true;
    }

    return result;
  };

  const handleFormSubmit = async (event?: React.FormEvent<HTMLFormElement>) => {
    event?.preventDefault();

    setErrorPhoneNumberMessage("");
    setErrorEmailMessage("");

    if (!validate() && birthDate) {
      const confirmationMessage = (
        <div>
          <h2 className="text-2xl font-bold mb-4">
            {t("notification.rental.title")}
          </h2>
          <p>
            {t("notification.rental.for")}{" "}
            {`${rental.car.brand} ${rental.car.model} ${rental.car.licensePlate}`}
          </p>
          <p className="mb-2">
            {t("system.rentFrom")} {rental.startDate.toString()}{" "}
            {t("system.rentUntil")} {rental.endDate.toString()}
          </p>
        </div>
      );

      const response = await makeRent(
        {
          startDate: rental.startDate,
          endDate: rental.endDate,
          phoneNumber,
          email,
          nationalRegisterNumber: registerNumber,
          birthDate,
          licenseNumber: drivingLicenceNumber,
        },
        rental,
        session?.user.token
      );

      if (response.ok) {
        router.push("/rents");
      } else if (response.status === 400) {
        const data = await response.json();
        console.log(data);
      }
    }
  };

  useImperativeHandle(ref, () => ({
    submitForm: () => {
      handleFormSubmit();
    },
  }));

  return (
    <form
      onSubmit={handleFormSubmit}
      className="bg-white  rounded px-8 pt-6 pb-8 mb-4">
      <InputField
        label={t("rent.phoneNumber")}
        value={phoneNumber}
        name="phoneNumber"
        onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
          setPhoneNumber(event.target.value)
        }
        id="phoneNumber"
      />
      {errorPhoneNumberMessage && (
        <p className="text-red-500 text-xs italic">{errorPhoneNumberMessage}</p>
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
      {errorEmailMessage && (
        <p className="text-red-500 text-xs italic">{errorEmailMessage}</p>
      )}
      <InputField
        label={t("rent.registerNumber")}
        value={registerNumber}
        name="registerNumber"
        onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
          setRegisterNumber(event.target.value)
        }
        id="registerNumber"
      />
      {errorRegisterNumberMessage && (
        <p className="text-red-500 text-xs italic">
          {errorRegisterNumberMessage}
        </p>
      )}
      <InputField
        type="date"
        label={t("rent.birthDate")}
        value={birthDate ? birthDate.toISOString().split("T")[0] : ""}
        name="birthDate"
        onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
          setBirthDate(event.target.value ? new Date(event.target.value) : null)
        }
        id="birthDate"
      />
      {errorBirthDateMessage && (
        <p className="text-red-500 text-xs italic">{errorBirthDateMessage}</p>
      )}
      <InputField
        label={t("rent.drivingNumber")}
        value={drivingLicenceNumber}
        name="drivingLicenceNumber"
        onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
          setDrivingLicenceNumber(event.target.value)
        }
        id="drivingLicenceNumber"
      />
      {errorDrivingLicenceNumberMessage && (
        <p className="text-red-500 text-xs italic">
          {errorDrivingLicenceNumberMessage}
        </p>
      )}
    </form>
  );
});

export default MakeRentForm;
