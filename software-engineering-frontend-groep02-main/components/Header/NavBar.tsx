import Link from "next/link";
import { cn } from "@/lib/utils";
import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
  navigationMenuTriggerStyle,
} from "@/components/ui/navigation-menu";
import { useState } from "react";
import React from "react";
import { Button } from "@/components/ui/button";
import { useTranslation } from "next-i18next";
import Image from "next/image";
import { useSession } from "next-auth/react";

export const NavBar: React.FC = () => {
  const { t } = useTranslation();

  const { data: session } = useSession();

  const isRenter = session?.user.roles?.includes("RENTER");
  const isOwner = session?.user.roles?.includes("OWNER");
  const [clickBlocked, setClickBlocked] = useState(false);

  // Handler for mouse enter event
  const handleMouseEnter = () => {
    // Block clicks
    setClickBlocked(true);

    // Start a timeout to unblock clicks after 1 second
    setTimeout(() => {
      setClickBlocked(false);
    }, 1000); // 1 second cooldown
  };

  // Handler for click event
  const handleClick = (event: {
    preventDefault: () => void;
    stopPropagation: () => void;
  }) => {
    if (clickBlocked) {
      // If clicks are blocked, prevent the click action
      event.preventDefault();
      event.stopPropagation();
    }
  };
  return (
    <NavigationMenu className="hidden md:block">
      <NavigationMenuList>
        {isOwner && (
          <NavigationMenuItem>
            <NavigationMenuTrigger
              onMouseEnter={handleMouseEnter}
              onClick={handleClick}>
              {t("header.cars")}
            </NavigationMenuTrigger>
            <NavigationMenuContent>
              <ul className="grid gap-3 p-6 md:w-[400px] lg:w-[500px] lg:grid-cols-[.75fr_1fr]">
                <li className="row-span-3">
                  <NavigationMenuLink asChild>
                    <Link
                      className="flex h-full w-full select-none flex-col justify-end rounded-md relative overflow-hidden no-underline outline-none focus:shadow-md transition duration-300"
                      href="/cars">
                      <Image
                        src="/img/car-front-min.jpg"
                        layout="fill"
                        objectPosition="right"
                        objectFit="cover"
                        alt="Car Overview"
                        priority // Ensures the image is loaded with higher priority
                      />
                      <div>
                        {/* Shadow overlay */}
                        <div className="absolute inset-0 bg-black opacity-50 transition duration-300"></div>

                        {/* Content */}
                        <div className="relative z-10 p-6">
                          <div className="mb-2 mt-4 text-lg font-medium text-white">
                            {t("header.carOverview")}
                          </div>
                          <p className="text-sm leading-tight text-white">
                            Get an overview of all your cars and their current
                            state.
                          </p>
                        </div>
                        {/* Lighten on hover */}
                      </div>
                    </Link>
                  </NavigationMenuLink>
                </li>
                <Link href="/cars/add">
                  <ListItem title={t("header.addCar")}>
                    {t("header.addCar")}
                  </ListItem>
                </Link>
                <ListItem title="Fast">Easy and fast car rentals.</ListItem>
                <ListItem title="Efficient">
                  Don't waste time on paperwork and waiting.
                </ListItem>
              </ul>
            </NavigationMenuContent>
          </NavigationMenuItem>
        )}

        <NavigationMenuItem>
          <Link href="/rentals" legacyBehavior passHref>
            <NavigationMenuLink className={navigationMenuTriggerStyle()}>
              {t("header.rentalOverview")}
            </NavigationMenuLink>
          </Link>
        </NavigationMenuItem>

        {/* Conditional Rent Overview Link for Renters */}
        {isRenter && (
          <NavigationMenuItem>
            <Link href="/rents" legacyBehavior passHref>
              <NavigationMenuLink className={navigationMenuTriggerStyle()}>
                {t("header.rentOverview")}
              </NavigationMenuLink>
            </Link>
          </NavigationMenuItem>
        )}

        <NavigationMenuItem>
          <Link href="/bills" legacyBehavior passHref>
            <NavigationMenuLink className={navigationMenuTriggerStyle()}>
              {t("header.bills")}
            </NavigationMenuLink>
          </Link>
        </NavigationMenuItem>
        <NavigationMenuItem>
          <Link href="/complaint" legacyBehavior passHref>
            <NavigationMenuLink className={navigationMenuTriggerStyle()}>
              {t("header.complaints")}
            </NavigationMenuLink>
          </Link>
        </NavigationMenuItem>
      </NavigationMenuList>
    </NavigationMenu>
  );
};

const ListItem = React.forwardRef<
  React.ElementRef<"a">,
  React.ComponentPropsWithoutRef<"a">
>(({ className, title, children, ...props }, ref) => {
  return (
    <li>
      <NavigationMenuLink asChild>
        <a
          ref={ref}
          className={cn(
            "block select-none space-y-1 rounded-md p-3 leading-none no-underline outline-none transition-colors hover:bg-accent hover:text-accent-foreground focus:bg-accent focus:text-accent-foreground",
            className
          )}
          {...props}>
          <div className="text-sm font-medium leading-none">{title}</div>
          <p className="line-clamp-2 text-sm leading-snug text-muted-foreground">
            {children}
          </p>
        </a>
      </NavigationMenuLink>
    </li>
  );
});
ListItem.displayName = "ListItem";
