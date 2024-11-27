import Link from "next/link";
import React, { useTransition } from "react";
import Dropdown from "../ui/dropdown";
import { NotificationTab } from "./Notification";
import Language from "../Language";
import { useTranslation } from "next-i18next";
import { useSession, signIn, signOut } from "next-auth/react";
import { NavBar } from "./NavBar";
import { UserBar } from "./UserBar";
import { Button } from "../ui/button";

const Header: React.FC = () => {
  const { t } = useTranslation();
  const { data: session } = useSession();

  const isRenter = session?.user.roles?.includes("RENTER");
  const isOwner = session?.user.roles?.includes("OWNER");

  return (
    <header className="w-full h-16 flex  fixed bg-background z-50 items-center">
      <div className="container flex flex-row justify-between">
        {/* Group the logo and navbar together */}
        <div className="flex flex-row items-center">
          {session ? (
            <>
              <Link href="/" className="text-2xl mr-4">
                {t("app.title")}
              </Link>
              <NavBar />
            </>
          ) : (
            <nav className="flex justify-center items-center mx-auto gap-2">
              {/* Buttons for Login and Signup */}
              <Link href={"/login"}>
                <Button>{t("header.login")}</Button>
              </Link>

              <Link href={"/register"}>
                <Button variant="secondary">{t("header.signUp")}</Button>
              </Link>
            </nav>
          )}
        </div>

        {/* Group the login form and theme selector together and align them to the right */}
        <div className="flex gap-2">
          {session && <NotificationTab />}
          <UserBar />
        </div>
      </div>
    </header>
  );
};

export default Header;
