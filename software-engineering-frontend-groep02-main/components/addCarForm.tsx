import { useRouter } from "next/navigation";
import { addCarService } from "@/services/carService";
import { useState } from "react";
import InputField from "./InputField";
import InputSubmit from "./InputSubmit";
import { useTranslation } from "next-i18next";
import { useSession } from "next-auth/react";

const AddCarForm: React.FC = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const [errorBrandMessage, setErrorBrandMessage] = useState<string>("");
  const [ErrorTypeMessage, setErrorTypeMessage] = useState<string>("");
  const [errorLicensePlateMessage, setErrorLicensePlateMessage] =
    useState<string>("");
  const [errorNumberOfSeatsMessage, setErrorNumberOfSeatsMessage] =
    useState<string>("");

  const [brand, setBrand] = useState("");
  const [model, setmodel] = useState("");
  const [type, settype] = useState("");
  const [licensePlate, setlicensePlate] = useState("");
  const [numberOfSeats, setnumberOfSeats] = useState(0);
  const [numberOfChildSeats, setnumberOfChildSeats] = useState(0);
  const [foldingRearSeat, setfoldingRearSeat] = useState(false);
  const [towBar, settowBar] = useState(false);
  const { data: session } = useSession();

  const addCar = async () => {
    return await addCarService(
      {
        brand,
        model,
        type,
        licensePlate,
        numberOfSeats,
        numberOfChildSeats,
        foldingRearSeat,
        towBar,
      },
      session?.user.token
    );
  };

  const validate = () => {
    let result: boolean = false;

    if (brand === "") {
      setErrorBrandMessage(t("error.noBrand"));
      result = true;
    }
    if (type === "") {
      setErrorTypeMessage(t("error.noType"));
      result = true;
    }
    if (licensePlate === "") {
      setErrorLicensePlateMessage(t("error.noLicencePlate"));
      result = true;
    }
    if (numberOfSeats === 0) {
      setErrorNumberOfSeatsMessage(t("error.noNumberOfSeats"));
      result = true;
    }

    return result;
  };

  const handleFormSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorBrandMessage("");
    setErrorTypeMessage("");
    setErrorLicensePlateMessage("");
    setErrorNumberOfSeatsMessage("");

    if (validate()) {
      return;
    }

    const response = await addCar();
    if (response.ok) {
      router.push("/cars");
    }
    if (response.status === 400) {
      const data = await response.json();
      console.log(data);
    }
  };
  return (
    <div className="max-w-md mx-auto mt-8">
      <form
        onSubmit={handleFormSubmit}
        className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <InputField
          label={t("car.brand")}
          value={brand}
          name="brand"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setBrand(event.target.value)
          }
          id="brand"
        />
        {errorBrandMessage && (
          <p className="text-red-500 text-xs italic">{errorBrandMessage}</p>
        )}
        <InputField
          label={t("car.model")}
          value={model}
          name="model"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setmodel(event.target.value)
          }
          id="model"
        />
        <InputField
          label={t("car.type")}
          value={type}
          name="type"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            settype(event.target.value)
          }
          id="type"
        />
        {ErrorTypeMessage && (
          <p className="text-red-500 text-xs italic">{ErrorTypeMessage}</p>
        )}
        <InputField
          label={t("car.licencePlate")}
          value={licensePlate}
          name="licensePlate"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setlicensePlate(event.target.value)
          }
          id="licensePlate"
        />
        {errorLicensePlateMessage && (
          <p className="text-red-500 text-xs italic">
            {errorLicensePlateMessage}
          </p>
        )}
        <InputField
          label={t("car.numberOfSeats")}
          value={numberOfSeats}
          name="numberOfSeats"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setnumberOfSeats(parseInt(event.target.value))
          }
          id="numberOfSeats"
          type="number"
        />
        {errorNumberOfSeatsMessage && (
          <p className="text-red-500 text-xs italic">
            {errorNumberOfSeatsMessage}
          </p>
        )}
        <InputField
          label={t("car.numberOfChildSeats")}
          value={numberOfChildSeats}
          name="numberOfChildSeats"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setnumberOfChildSeats(parseInt(event.target.value))
          }
          id="numberOfChildSeats"
          type="number"
        />
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">
            {t("car.foldingRearSeatOption")}:
          </label>
          <input
            type="checkbox"
            checked={foldingRearSeat}
            onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
              setfoldingRearSeat(event.target.checked)
            }
            id="foldingRearSeat"
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">
            {t("car.towBarOption")}:
          </label>
          <input
            type="checkbox"
            checked={towBar}
            onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
              settowBar(event.target.checked)
            }
            id="towBar"
          />
        </div>

        <div className="mb-4">
          <InputSubmit
            onClick={handleFormSubmit}
            value={t("system.addCarButton")}
          />
        </div>
      </form>
    </div>
  );
};

export default AddCarForm;
