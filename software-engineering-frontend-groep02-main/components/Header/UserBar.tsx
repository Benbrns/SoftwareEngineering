import {
  Cloud,
  CreditCard,
  Github,
  Keyboard,
  Languages,
  LifeBuoy,
  LogOut,
  Mail,
  MessageSquare,
  Plus,
  PlusCircle,
  Settings,
  User,
  UserPlus,
  Users,
  Moon,
  Sun,
} from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuPortal,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuSub,
  DropdownMenuSubContent,
  DropdownMenuSubTrigger,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { signOut, useSession } from "next-auth/react";
import React from "react";
import { useRouter } from "next/router";
import { useTranslation } from "next-i18next";
import { useTheme } from "next-themes";

export function UserBar() {
  const { t } = useTranslation();
  const router = useRouter();
  const { locale, query } = router;

  const { data: session } = useSession();

  const handleLanguageChange = (newLocale: string | undefined) => {
    if (newLocale !== locale) {
      router.push({ pathname: router.pathname, query }, router.asPath, {
        locale: newLocale,
      });
    }
  };

  const { theme, setTheme } = useTheme();

  const isActive = (value: string | undefined, type: string) => {
    if (type === "theme") {
      return value === theme ? "bg-gray-200 dark:bg-gray-700" : "";
    }
    if (type === "locale") {
      return value === locale ? "bg-gray-200 dark:bg-gray-700" : "";
    }
    return "";
  };

  return (
    <DropdownMenu modal={false}>
      <DropdownMenuTrigger asChild>
        <Button variant="outline">
          {session ? <p>{session?.user?.firstName}</p> : <p>Settings</p>}
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="w-56">
        {session && (
          <>
            <DropdownMenuLabel>My Account</DropdownMenuLabel>
            <DropdownMenuSeparator />
          </>
        )}
        <DropdownMenuGroup>
          <DropdownMenuSub>
            <DropdownMenuSubTrigger>
              <Languages className="mr-2 h-4 w-4" />{" "}
              {/* Assuming Languages is an icon component */}
              <span>Language</span>
            </DropdownMenuSubTrigger>
            <DropdownMenuPortal>
              <DropdownMenuSubContent>
                <DropdownMenuItem
                  onClick={() => handleLanguageChange("en")}
                  className={isActive("en", "locale")}>
                  <span>English</span>
                </DropdownMenuItem>
                <DropdownMenuItem
                  onClick={() => handleLanguageChange("fr")}
                  className={isActive("fr", "locale")}>
                  <span>Français</span>
                </DropdownMenuItem>
                <DropdownMenuItem
                  onClick={() => handleLanguageChange("nl")}
                  className={isActive("nl", "locale")}>
                  <span>Nederlands</span>
                </DropdownMenuItem>
              </DropdownMenuSubContent>
            </DropdownMenuPortal>
          </DropdownMenuSub>
          <DropdownMenuSub>
            <DropdownMenuSubTrigger>
              <Sun className="mr-2 h-4 w-4 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
              <Moon className="absolute mr-2 h-4 w-4 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
              <span>Theme Selector</span>
            </DropdownMenuSubTrigger>
            <DropdownMenuPortal>
              <DropdownMenuSubContent>
                <DropdownMenuItem
                  onClick={() => setTheme("light")}
                  className={isActive("light", "theme")}>
                  Light
                </DropdownMenuItem>
                <DropdownMenuItem
                  onClick={() => setTheme("dark")}
                  className={isActive("dark", "theme")}>
                  Dark
                </DropdownMenuItem>
                <DropdownMenuItem
                  onClick={() => setTheme("system")}
                  className={isActive("system", "theme")}>
                  System
                </DropdownMenuItem>
              </DropdownMenuSubContent>
            </DropdownMenuPortal>
          </DropdownMenuSub>
          {session && (
            <>
              <DropdownMenuSeparator />
              <DropdownMenuItem
                onClick={() =>
                  signOut({ callbackUrl: process.env.NEXTAUTH_URL })
                }>
                <LogOut className="mr-2 h-4 w-4" />
                <span>Log out</span>
                <DropdownMenuShortcut>⇧⌘Q</DropdownMenuShortcut>
              </DropdownMenuItem>
            </>
          )}
        </DropdownMenuGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
