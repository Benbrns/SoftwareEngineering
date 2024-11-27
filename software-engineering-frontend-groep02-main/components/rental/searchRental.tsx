import { useRouter } from "next/navigation";
import { use, useEffect, useState } from "react";
import InputField from "../InputField";
import InputSubmit from "../InputSubmit";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  addRentalCar,
  getAllRentals,
  searchRental,
} from "@/services/rentalService";
import { Rental, RentalInput, SearchRentalInput } from "@/types";
import { mutate } from "swr";
import { useTranslation } from "next-i18next";
import { useSession } from "next-auth/react";
import { Label } from "@/components/ui/label";
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { Filter, Search, Send, Trash2 } from "lucide-react";
import { Switch } from "@/components/ui/switch";

type Props = {
  setRentals: (rentals: Rental[]) => void;
};

const SearchRentals: React.FC<Props> = ({ setRentals }: Props) => {
  const { t } = useTranslation();
  const router = useRouter();
  const [ErrorNoValuesMessage, setErrorNoValuesMessage] = useState<string>("");
  const [ErrorNothingFoundMessage, setErrorNothingFoundMessage] =
    useState<string>("");

  const [email, setEmail] = useState<string>("");
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [type, setType] = useState<string>("");
  const [towBar, setTowBar] = useState<boolean>(false);
  const [numberOfSeats, setNumberOfSeats] = useState<number>();
  const [numberOfChildSeats, setNumberOfChildSeats] = useState<number>();
  const [foldingRearSeats, setFoldingRearSeats] = useState<boolean>(false);
  const [model, setModel] = useState<string>("");
  const [brand, setBrand] = useState<string>("");
  const [city, setCity] = useState<string>("");

  const { data: session } = useSession();

  const validate = () => {
    let result: boolean = false;
    console.log("searching for rentals");

    if (
      email + city + brand + model === "" &&
      startDate === null &&
      endDate === null &&
      foldingRearSeats === null &&
      numberOfChildSeats === null &&
      numberOfSeats === null &&
      towBar === null &&
      type === null
    ) {
      setErrorNoValuesMessage(t("error.noValues"));
      return true;
    }

    return result;
  };

  const handleFormSubmit = async () => {
    console.log(
      "|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"
    );
    setErrorNoValuesMessage("");
    setErrorNothingFoundMessage("");

    if (validate()) {
      return;
    }

    // Initialize an empty object
    let searchRentalInput: Partial<SearchRentalInput> = {};

    // Dynamically add fields if they are not empty
    if (email) searchRentalInput.email = email;
    if (startDate) searchRentalInput.startDate = startDate;
    if (endDate) searchRentalInput.endDate = endDate;
    if (type) searchRentalInput.type = type;
    if (towBar) searchRentalInput.towBar = towBar;
    if (numberOfSeats) searchRentalInput.numberOfSeats = numberOfSeats;
    if (numberOfChildSeats)
      searchRentalInput.numberOfChildSeats = numberOfChildSeats;
    if (foldingRearSeats) searchRentalInput.foldingRearSeat = foldingRearSeats;
    if (brand) searchRentalInput.brand = brand;
    if (model) searchRentalInput.model = model;
    if (city) searchRentalInput.city = city;

    const response = await searchRental(
      searchRentalInput as SearchRentalInput,
      session?.user.token
    );

    if (response.ok) {
      const rentals = await response.json();
      setRentals(rentals);
      return;
    }
    if (response.status === 400) {
      const rentals = await response.json();
      console.log(
        "mapped error: " +
          rentals.rental +
          " to translation: " +
          t("error.noResults")
      );
      setErrorNothingFoundMessage(t("error.noResults"));
    }
    return;
  };

  const handleReset = async () => {
    setEmail("");
    setStartDate(null);
    setEndDate(null);
    setType("");
    setTowBar(false);
    setNumberOfSeats(undefined);
    setNumberOfChildSeats(undefined);
    setFoldingRearSeats(false);
    setModel("");
    setBrand("");
    setCity("");

    const response = await getAllRentals(session?.user.token);

    if (response.ok) {
      const rentals = await response.json();
      setRentals(rentals);

      return;
    }
    if (response.status === 400) {
      const rentals = await response.json();
    }
    return;
  };

  const makeDate = (value: Date) => {
    const day = String(value.getDate()).padStart(2, "0");
    const month = String(value.getMonth() + 1).padStart(2, "0"); // January is 0!
    const year = value.getFullYear();
    return day + "/" + month + "/" + year;
  };

  return (
    <form onSubmit={handleFormSubmit} className=" ">
      <Sheet>
        <div className="container flex justify-between">
          {/* make the same spacing as the header */}
          <div className="flex flex-row items-center"></div>

          <div className="flex">
            <SheetTrigger asChild>
              <Button variant="outline" className="">
                <Filter className=" h-[1.10rem] w-[1.10rem]" />
              </Button>
            </SheetTrigger>
          </div>
        </div>
        <SheetContent>
          <SheetHeader>
            <SheetTitle>{t("system.searchRental")}</SheetTitle>
            <SheetDescription>{t("system.searchRentalText")}</SheetDescription>
          </SheetHeader>
          <div className="grid gap-4 py-4 px-2 overflow-y-scroll max-h-[70vh]">
            <div>
              <Label htmlFor="email">{t("rental.ownerEmail")}</Label>
              <Input
                id="email"
                value={email}
                type="text"
                onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setEmail(event.target.value)
                }
                className="col-span-3"
              />
            </div>
            <div>
              <Label htmlFor="startDate">{t("rental.startDate")}</Label>
              <Input
                id="startDate"
                type="date"
                value={startDate ? startDate.toISOString().split("T")[0] : ""}
                onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setStartDate(
                    event.target.value ? new Date(event.target.value) : null
                  )
                }
                className="col-span-3"
              />
            </div>
            <div>
              <Label htmlFor="endDate">{t("rental.endDate")}</Label>
              <Input
                id="endDate"
                type="date"
                value={endDate ? endDate.toISOString().split("T")[0] : ""}
                onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setEndDate(
                    event.target.value ? new Date(event.target.value) : null
                  )
                }
                className="col-span-3"
              />
            </div>
            <div>
              <Label htmlFor="type">{t("car.type")}</Label>
              <Input
                id="type"
                type="text"
                value={type}
                onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setType(event.target.value)
                }
                className="col-span-3"
              />
            </div>

            <div>
              <Label htmlFor="numberOfSeats">{t("car.numberOfSeats")}</Label>
              <Input
                id="numberOfSeats"
                type="number"
                min={1}
                value={numberOfSeats}
                onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setNumberOfSeats(Number(event.target.value))
                }
                className="col-span-3"
              />
            </div>
            <div>
              <Label htmlFor="numberOfChildSeats">
                {t("car.numberOfChildSeats")}
              </Label>
              <Input
                id="numberOfChildSeats"
                type="number"
                min={0}
                value={numberOfChildSeats}
                onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setNumberOfChildSeats(Number(event.target.value))
                }
                className="col-span-3"
              />
            </div>

            <div>
              <Label htmlFor="brand">{t("car.brand")}</Label>
              <Input
                id="brand"
                type="text"
                value={brand}
                onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setBrand(event.target.value)
                }
                className="col-span-3"
              />
            </div>
            <div>
              <Label htmlFor="city">{t("rental.pickupCity")}</Label>
              <Input
                id="city"
                type="text"
                value={city}
                onInput={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setCity(event.target.value)
                }
                className="col-span-3"
              />
            </div>
            <div>
              <div className="flex items-center space-x-2 p-2 ">
                <Switch
                  checked={towBar}
                  onCheckedChange={(checked: boolean) => setTowBar(checked)}
                  id="towBar"
                />
                <Label htmlFor="towBar">{t("car.towBarOption")}</Label>
              </div>
            </div>
            <div>
              <div className="flex items-center space-x-2 p-2">
                <Switch
                  checked={foldingRearSeats}
                  onCheckedChange={(checked: boolean) =>
                    setFoldingRearSeats(checked)
                  }
                  id="foldingRearSeats"
                />

                <Label htmlFor="foldingRearSeats">
                  {t("car.foldingRearSeatOption")}
                </Label>
              </div>
            </div>
            {ErrorNoValuesMessage && (
              <p className="text-red-500 text-xs italic">
                {ErrorNoValuesMessage}
              </p>
            )}
            {ErrorNothingFoundMessage && (
              <p className="text-red-500 text-xs italic">
                {ErrorNothingFoundMessage}
              </p>
            )}
          </div>

          <SheetFooter>
            <div className="mt-4 gap-1 flex flex-col w-full">
              <Button variant="destructive" className=" " onClick={handleReset}>
                <Trash2 className="w-4 h-4 mr-2" />
                {t("system.resetFilter")}
              </Button>
              <Button type="submit" onClick={handleFormSubmit}>
                <Search className="w-4 h-4 mr-2" />
                {t("system.searchRental")}
              </Button>
            </div>
          </SheetFooter>
        </SheetContent>
      </Sheet>
    </form>
  );
};

export default SearchRentals;
