import React, { useState } from "react";
import { userLogin } from "@/services/UserService";
import { StatusMessage } from "@/types";
import classNames from "classnames";
import Link from "next/link";
import { useRouter } from "next/router";
import { signIn, signOut, useSession } from "next-auth/react";
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

const UserLoginForm: React.FC = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [emailError, setEmailError] = useState<string | null>(null);
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
  const router = useRouter();
  const callbackUrl = (router.query.callbackUrl as string) || "/";
  const { t } = useTranslation();

  const clearErrors = () => {
    setEmailError(null);
    setPasswordError(null);
    setStatusMessages([]);
  };

  const validate = (): boolean => {
    let result = false;
    const expression: RegExp = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;

    if (!expression.test(email)) {
      setEmailError(t("error.invalid.email"));
      result = false;
    }
    if (!email || email.trim()) {
      setPasswordError(t("error.noEmail"));
      result = false;
    }
    if (!password || password.trim()) {
      setPasswordError(t("error.user.noPass"));
      result = false;
    }

    return result;
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    clearErrors();

    if (!validate) {
      return;
    }

    const result = await signIn("credentials", {
      redirect: false,
      email,
      password,
      callbackUrl,
    });

    if (result?.error) {
      if (result.status === 401) {
        setStatusMessages([
          { message: t("error.user.invalCred"), type: "error" },
        ]);
      }
    } else {
      setStatusMessages([
        { message: t("error.user.loginSucc"), type: "success" },
      ]);
      router.push(callbackUrl);
    }
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <Card className="container mx-auto mt-8 p-8 max-w-md rounded-md shadow-md">
          <CardHeader>
            <CardTitle>{t("system.login.title")}</CardTitle>
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
            <Link href={"/register"} className="text-blue-500 pb-5 underline">
              {t("system.login.signupText")}
            </Link>
            <Button
              className="w-full bg-blue-400 hover:bg-blue-500"
              type="submit">
              {t("system.login.button")}
            </Button>
          </CardFooter>
        </Card>
      </form>
    </>
  );
};

export default UserLoginForm;
