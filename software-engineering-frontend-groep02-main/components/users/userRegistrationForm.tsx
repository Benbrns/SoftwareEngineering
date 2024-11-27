import { userRegistration } from "@/services/UserService";
import { StatusMessage } from "@/types";
import classNames from "classnames";
import { useRouter } from "next/router";
import { useState } from "react";
import { useTranslation } from "next-i18next";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import Link from "next/link";
import { Switch } from "@/components/ui/switch";

const UserRegistrationForm: React.FC = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [firstName, setFirstName] = useState<string>("");
  const [lastName, setLastName] = useState<string>("");
  const [phoneNumber, setPhoneNumber] = useState<string>("");
  const [birthDate, setBirthDate] = useState<string>("");
  const [nationalRegisterNumber, setNationalRegisterNumber] =
    useState<string>("");
  const [licenseNumber, setLicenseNumber] = useState<string>("");
  const [emailError, setEmailError] = useState<string | null>(null);
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [firstNameError, setFirstNameError] = useState<string | null>(null);
  const [lastNameError, setLastNameError] = useState<string | null>(null);
  const [phoneNumberError, setPhoneNumberError] = useState<string | null>(null);
  const [birthDateError, setBirthDateError] = useState<string | null>(null);
  const [nationalRegisterNumberError, setNationalRegisterNumberError] =
    useState<string | null>(null);
  const [licenseNumberError, setLicenseNumberError] = useState<string | null>(
    null
  );
  const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
  const { t } = useTranslation();

  // roles
  const [isAdmin, setIsAdmin] = useState<boolean>(false);
  const [isRenter, setIsRenter] = useState<boolean>(true);
  const [isOwner, setIsOwner] = useState<boolean>(false);
  const [isAccountant, setIsAccountant] = useState<boolean>(false);

  const handleAdminChange = () => {
    setIsAdmin(!isAdmin); // Toggle the boolean value
  };

  const handleRenterChange = () => {
    setIsRenter(!isRenter); // Toggle the boolean value
  };

  const handleOwnerChange = () => {
    setIsOwner(!isOwner); // Toggle the boolean value
  };

  const handleAccountantChange = () => {
    setIsAccountant(!isAccountant); // Toggle the boolean value
  };

  const router = useRouter();

  const clearErrors = () => {
    setEmailError(null);
    setPasswordError(null);
    setFirstNameError(null);
    setLastNameError(null);
    setPhoneNumberError(null);
    setBirthDateError(null);
    setNationalRegisterNumberError(null);
    setLicenseNumberError(null);
    setStatusMessages([]);
  };

  const validate = (): boolean => {
    let result = false;
    const expression: RegExp = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;

    if (!expression.test(email)) {
      setEmailError(t("error.invalid.email"));
      result = true;
    }

    if (!password || !password.trim()) {
      setPasswordError(t("error.user.noPass"));
      result = true;
    }

    if (!firstName || !firstName.trim()) {
      setFirstNameError(t("error.user.noFirstN"));
      result = true;
    }

    if (!lastName || !lastName.trim()) {
      console.log(lastName, !lastName, lastName.trim());
      setLastNameError(t("error.user.noLastN"));
      result = true;
    }

    if (!phoneNumber || !phoneNumber.trim()) {
      setPhoneNumberError(t("error.noPhone"));
      result = true;
    }

    if (!birthDate || !birthDate.trim()) {
      setBirthDateError(t("error.noBirthDate"));
      result = true;
    }

    if (!nationalRegisterNumber || !nationalRegisterNumber.trim()) {
      setNationalRegisterNumberError(t("error.noRegisterNumber"));
      result = true;
    }

    if (!licenseNumber || !licenseNumber.trim()) {
      setLicenseNumberError(t("error.noDrivigLicenseNumber"));
      result = true;
    }

    return result;
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    clearErrors();

    if (validate()) {
      return;
    }

    const user = {
      email,
      password,
      firstName,
      lastName,
      phoneNumber,
      birthDate,
      nationalRegisterNumber,
      licenseNumber,
      isAdmin,
      isRenter,
      isOwner,
      isAccountant,
    };
    const response = await userRegistration(user);

    if (response.status === 200) {
      setStatusMessages([
        { message: t("error.user.signupSucc"), type: "success" },
      ]);

      setTimeout(() => {
        router.push("/login");
      }, 2000);
    } else {
      setStatusMessages([
        { message: t("error.user.invalCred"), type: "error" },
      ]);
    }
  };
  return (
    <>
      <form onSubmit={handleSubmit}>
        <Card className=" container mx-auto mt-8 p-8 max-w-md rounded-md shadow-md">
          <CardHeader>
            <CardTitle>{t("system.register.title")}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid w-full items-center gap-4">
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="emailInput">{t("user.mail")}</Label>
                <Input
                  id="emailInput"
                  type="text"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  placeholder="john.doe@example.com"
                />
                {emailError && <div className="text-red-500">{emailError}</div>}
              </div>
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="passwordInput"> {t("user.password")}</Label>
                <Input
                  id="passwordInput"
                  type="password"
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                  placeholder="password"
                />
                {passwordError && (
                  <div className="text-red-500 text-xs italic">
                    {passwordError}
                  </div>
                )}
              </div>
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="firstNameInput">{t("user.firstName")}</Label>
                <Input
                  id="firstNameInput"
                  type="text"
                  value={firstName}
                  onChange={(event) => setFirstName(event.target.value)}
                  placeholder="John"
                />
                {firstNameError && (
                  <div className="text-red-500 text-xs italic">
                    {firstNameError}
                  </div>
                )}
              </div>
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="lastNameInput"> {t("user.lastName")}</Label>
                <Input
                  id="lastNameInput"
                  type="text"
                  value={lastName}
                  onChange={(event) => setLastName(event.target.value)}
                  placeholder="Doe"
                />
                {lastNameError && (
                  <div className="text-red-500 text-xs italic">
                    {lastNameError}
                  </div>
                )}
              </div>
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="phoneNum">{t("user.phoneNum")}</Label>
                <Input
                  id="phoneNum"
                  type="text"
                  value={phoneNumber}
                  onChange={(event) => setPhoneNumber(event.target.value)}
                  placeholder="0412345678"
                />
                {phoneNumberError && (
                  <div className="text-red-500 text-xs italic">
                    {phoneNumberError}
                  </div>
                )}
              </div>
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="birthDateInput">{t("user.bDate")}</Label>
                <Input
                  id="birthDateInput"
                  type="date"
                  value={birthDate}
                  onChange={(event) => setBirthDate(event.target.value)}
                />
                {passwordError && (
                  <div className="text-red-500 text-xs italic">
                    {passwordError}
                  </div>
                )}
              </div>
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="nationalRegistrationNumberInput">
                  {t("user.nRegNum")}
                </Label>
                <Input
                  id="nationalRegistrationNumberInput"
                  type="text"
                  value={nationalRegisterNumber}
                  onChange={(event) =>
                    setNationalRegisterNumber(event.target.value)
                  }
                  placeholder="23.03.07-123.45"
                />
                {nationalRegisterNumberError && (
                  <div className="text-red-500 text-xs italic">
                    {nationalRegisterNumberError}
                  </div>
                )}
              </div>
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="licenseNumberInput">{t("user.lNum")}</Label>
                <Input
                  id="licenseNumberInput"
                  type="text"
                  value={licenseNumber}
                  onChange={(event) => setLicenseNumber(event.target.value)}
                  placeholder="0123456789"
                />
                {licenseNumberError && (
                  <div className="text-red-500 text-xs italic">
                    {licenseNumberError}
                  </div>
                )}
              </div>

              <div>
                <label className="block mb-2 text-sm font-medium">
                  {t("system.register.roleSelect")}
                </label>
                <div className="flex flex-wrap">
                  <div className="flex items-center mr-4 mb-2">
                    <input
                      id="renterRole"
                      type="checkbox"
                      checked={isRenter}
                      onChange={() => handleRenterChange()}
                      className="mr-1"
                    />
                    <label htmlFor="renterRole">{t("user.role.renter")}</label>
                  </div>
                  <div className="flex items-center mr-4 mb-2">
                    <input
                      id="ownerRole"
                      type="checkbox"
                      checked={isOwner}
                      onChange={() => handleOwnerChange()}
                      className="mr-1"
                    />
                    <label htmlFor="ownerRole">{t("user.role.owner")}</label>
                  </div>
                  {/* <div className="flex items-center mr-4 mb-2">
                <input
                  id="accountantRole"
                  type="checkbox"
                  checked={isAccountant}
                  onChange={() => handleAccountantChange()}
                  className="mr-1"
                />
                <label htmlFor="accountantRole">Accountant</label>
              </div>
              <div className="flex items-center mr-4 mb-2">
                <input
                  id="adminRole"
                  type="checkbox"
                  checked={isAdmin}
                  onChange={() => handleAdminChange()}
                  className="mr-1"
                />
                <label htmlFor="adminRole">Admin</label>
              </div> */}
                </div>
              </div>
              {statusMessages && (
                <div>
                  <ul>
                    {statusMessages.map(({ message, type }, index) => (
                      <li
                        key={index}
                        className={classNames({
                          "text-red-800": type === "error",
                          "text-green-800": type === "success",
                        })}>
                        {message}
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          </CardContent>
          <CardFooter className="flex flex-col justify-between">
            <Link href={"/login"} className="text-blue-500 pb-5 underline">
              {t("system.login.loginText")}
            </Link>
            <Button
              className="w-full bg-blue-400 hover:bg-blue-500"
              type="submit">
              {t("system.register.button")}
            </Button>
          </CardFooter>
        </Card>
      </form>
    </>
  );
};

export default UserRegistrationForm;
