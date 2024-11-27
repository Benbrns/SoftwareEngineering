import { useRouter } from "next/router";
import { use, useEffect, useState } from "react";
import InputField from "../InputField";
import InputSubmit from "../InputSubmit";
import { makeRent, getAllRents, searchRents } from "@/services/rentService";
import { Rent, RentInput, SearchRentInput } from "@/types";
import { mutate } from "swr";
import { useTranslation } from "next-i18next";
import { useSession } from "next-auth/react";

type Props = {
  setRents: (rents: Rent[]) => void;
};

const SearchRentals: React.FC<Props> = ({ setRents }: Props) => {
  const { t } = useTranslation();
  const router = useRouter();
  const [ErrorNoValuesMessage, setErrorNoValuesMessage] = useState<string>("");
  const [ErrorNothingFoundMessage, setErrorNothingFoundMessage] =
    useState<string>("");

  const [email, setEmail] = useState<string>("");
  const { data: session } = useSession();

  const validate = () => {
    let result: boolean = false;

    if (email === "") {
      setErrorNoValuesMessage(t("error.noEmail"));
      return true;
    }
    return result;
  };

  const handleFormSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorNoValuesMessage("");
    setErrorNothingFoundMessage("");

    if (validate()) {
      return;
    }

    // Initialize an empty object
    let rent: Partial<SearchRentInput> = {};

    // Dynamically add fields if they are not empty
    if (email) rent.email = email;

    const response = await searchRents(
      rent as SearchRentInput,
      session?.user.token
    );

    if (response.ok) {
      const rents = await response.json();
      setRents(rents);
      return;
    }
    if (response.status === 400) {
      const rents = await response.json();
      console.log(
        "mapped error: " +
          rents.rent +
          " to translation: " +
          t("error.noResults")
      );
      setErrorNothingFoundMessage(t("error.noResults"));
    }
    return;
  };

  const handleReset = async () => {
    setEmail("");

    const response = await getAllRents(session?.user.token);

    if (response.ok) {
      const rentals = await response.json();
      setRents(rentals);

      return;
    }
    if (response.status === 400) {
      const rents = await response.json();
    }
    return;
  };

  return (
    <div className="max-w-md mx-auto mt-8">
      <form
        onSubmit={handleFormSubmit}
        className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <InputField
          label={t("rental.ownerEmail")}
          value={email}
          name="email"
          onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
            setEmail(event.target.value)
          }
          id="email"
        />
        {ErrorNoValuesMessage && (
          <p className="text-red-500 text-xs italic">{ErrorNoValuesMessage}</p>
        )}
        {ErrorNothingFoundMessage && (
          <p className="text-red-500 text-xs italic">
            {ErrorNothingFoundMessage}
          </p>
        )}

        <div className="mt-4">
          <InputSubmit
            onClick={handleFormSubmit}
            value={t("system.searchRental")}
          />
          <button
            type="button"
            onClick={handleReset}
            className="rounded-md mt-2 text-white px-2 py-1 focus:outline-none  bg-blue-600 hover:bg-blue-800 focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50">
            {t("system.resetFilter")}
          </button>
        </div>
      </form>
    </div>
  );
};

export default SearchRentals;
