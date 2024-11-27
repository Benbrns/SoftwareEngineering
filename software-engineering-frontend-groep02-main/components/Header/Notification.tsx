import {
  Cloud,
  CreditCard,
  Github,
  Keyboard,
  LifeBuoy,
  LogOut,
  Mail,
  MessageSquare,
  Plus,
  PlusCircle,
  Settings,
  Bell,
  BellOff,
  BellDot,
  User,
  UserPlus,
  Users,
  Check,
  Circle,
  CircleCheckBig,
} from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";

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
import { setStatusRent } from "@/services/rentService";
import useSWR, { mutate } from "swr"; // Note: Direct import of `mutate` is not used here
import {
  getAllNotifications,
  setNotificationAsViewed,
} from "@/services/notificationService";
import useInterval from "use-interval";
import { useSession } from "next-auth/react";
import { useState } from "react";
import type { Notification } from "@/types";
import { useTranslation, UseTranslation } from "next-i18next";
import { DialogClose } from "@radix-ui/react-dialog";
import { set } from "date-fns";

export function NotificationTab() {
  const [isNotificationDialogOpen, setIsNotificationDialogOpen] =
    useState(false);
  const [selectedNotification, setSelectedNotification] =
    useState<Notification | null>(null);
  const [ownerNotifications, setOwnerNotifications] = useState<Notification[]>(
    []
  );
  const [renterNotifications, setRenterNotifications] = useState<
    Notification[]
  >([]);

  const [pendingRenterNotifications, setPendingRenterNotifications] = useState<
    Notification[]
  >([]);
  const [oldOwnerNotifications, setOldOwnerNotifications] = useState<
    Notification[]
  >([]);

  const { data: session } = useSession();

  const isRenter = session?.user.roles?.includes("RENTER");
  const isOwner = session?.user.roles?.includes("OWNER");

  const { t } = useTranslation();

  const getNotifications = async () => {
    const response = await getAllNotifications(session?.user.token);
    const notifications = await response.json();

    // Filter owner notifications
    const tmpOwnerNotifications = notifications.filter(
      (notification: Notification) =>
        notification.rent.ownerEmail === session?.user.email
    );
    const ownerNotifications = tmpOwnerNotifications.filter(
      (notification: Notification) => notification.rent.status === "PENDING"
    );
    const oldOwnerNotifications = tmpOwnerNotifications.filter(
      (notification: Notification) => notification.rent.status !== "PENDING"
    );

    // Filter renter notifications
    const tmpRenterNotifications = notifications.filter(
      (notification: Notification) =>
        notification.rent.renterEmail === session?.user.email
    );
    const renterNotifications = tmpRenterNotifications.filter(
      (notification: Notification) => notification.rent.status !== "PENDING"
    );
    const pendingRenterNotifications = tmpRenterNotifications.filter(
      (notification: Notification) => notification.rent.status === "PENDING"
    );

    // Set the filtered notifications into state
    setOwnerNotifications(ownerNotifications);
    setOldOwnerNotifications(oldOwnerNotifications);
    setRenterNotifications(renterNotifications);
    setPendingRenterNotifications(pendingRenterNotifications);

    return { notifications };
  };

  const { data, error } = useSWR("/api/notifications", getNotifications);

  useInterval(() => {
    mutate(getNotifications());
  }, 3000);

  const handleSelectNotification = async (
    notification: Notification,
    role: String
  ) => {
    setSelectedNotification(notification);
    setIsNotificationDialogOpen(true);

    // Set notification as viewed if not already viewed
    if (
      (role === "owner" && !notification.ownerViewed) ||
      (role === "renter" && !notification.renterViewed)
    ) {
      try {
        const response = await setNotificationAsViewed(
          session?.user.token,
          notification.id,
          role
        );
        if (response.ok) {
          console.log("Notification marked as viewed successfully");
        } else {
          console.error("Failed to mark notification as viewed");
        }
      } catch (error) {
        console.error("Error in fetching data:", error);
      }
    }
  };

  const hasUnviewedOwnerNotifications = () => {
    return ownerNotifications.some((notification) => !notification.ownerViewed);
  };

  const hasUnviewedRenterNotifications = () => {
    return renterNotifications.some(
      (notification) => !notification.renterViewed
    );
  };

  const showNotificationIndicator = () => {
    return (
      (isOwner &&
        ownerNotifications &&
        ownerNotifications.length > 0 &&
        hasUnviewedOwnerNotifications()) ||
      (isRenter &&
        renterNotifications &&
        renterNotifications.length > 0 &&
        hasUnviewedRenterNotifications())
    );
  };

  const handleNotificationReject = async (
    selectedNotification: Notification
  ) => {
    if (selectedNotification !== null) {
      // Handle the confirmation of the rent
      setIsNotificationDialogOpen(false);
      setStatusRent(
        "REJECTED",
        selectedNotification.rent.id,
        session?.user.token
      );
    }
  };
  const handleNotificationConfirm = async (
    selectedNotification: Notification
  ) => {
    if (selectedNotification !== null) {
      // Handle the confirmation of the rent
      setIsNotificationDialogOpen(false);

      setStatusRent(
        "CONFIRMED",
        selectedNotification.rent.id,
        session?.user.token
      );
    }
  };

  if (error)
    return (
      <DropdownMenu modal={false}>
        <DropdownMenuTrigger asChild>
          <Button variant="ghost">
            <BellOff className="h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent className="w-56">
          <DropdownMenuLabel>Notifications</DropdownMenuLabel>
          <DropdownMenuSeparator />
          <DropdownMenuGroup>
            <DropdownMenuItem>
              <div className="mr-2 h-4 w-4" />
              <span>Failed to load notifications</span>
            </DropdownMenuItem>
          </DropdownMenuGroup>
        </DropdownMenuContent>
      </DropdownMenu>
    );

  if (!data)
    return (
      <DropdownMenu modal={false}>
        <DropdownMenuTrigger asChild>
          <Button variant="ghost">
            <BellDot className="h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent className="w-56">
          <DropdownMenuLabel>Notifications</DropdownMenuLabel>
          <DropdownMenuSeparator />
          <DropdownMenuGroup>
            <DropdownMenuItem>
              <div className="mr-2 h-4 w-4" />
              <span> Loading ...</span>
            </DropdownMenuItem>
          </DropdownMenuGroup>
          <DropdownMenuSeparator />
          <DropdownMenuLabel>Pending</DropdownMenuLabel>
          <DropdownMenuSeparator />
          <DropdownMenuGroup>
            <DropdownMenuItem>
              <User className="mr-2 h-4 w-4" />
              <span>Profile</span>
            </DropdownMenuItem>
          </DropdownMenuGroup>
        </DropdownMenuContent>
      </DropdownMenu>
    );

  return (
    <>
      <Dialog
        open={isNotificationDialogOpen}
        onOpenChange={setIsNotificationDialogOpen}>
        <DialogContent className="sm:max-w-[425px]">
          {selectedNotification ? (
            <>
              <DialogHeader>
                <DialogTitle>
                  {selectedNotification.rent.status === "PENDING" &&
                    t("notification.rental.title")}
                  {selectedNotification.rent.status === "CONFIRMED" &&
                    t("notification.rental.conf")}
                  {selectedNotification.rent.status === "REJECTED" &&
                    t("notification.rental.canc")}
                </DialogTitle>
                <DialogDescription>
                  {t("notification.rental.for")}{" "}
                  {String(selectedNotification.rent.rental.car.model)}{" "}
                  {String(selectedNotification.rent.rental.car.licensePlate)}{" "}
                  {selectedNotification.rent.ownerEmail ===
                    session?.user.email && (
                    <>
                      {t("notification.rental.by")}{" "}
                      {String(selectedNotification.rent.renterEmail)}
                    </>
                  )}
                  <br />
                  {t("system.rentFrom")}{" "}
                  {String(selectedNotification.rent.startDate)}{" "}
                  {t("system.rentUntil")}{" "}
                  {String(selectedNotification.rent.endDate)}{" "}
                  {selectedNotification.rent.status === "REJECTED" && (
                    <span>{t("notification.rentReject")}</span>
                  )}
                  {selectedNotification.rent.status === "CONFIRMED" && (
                    <span>{t("notification.rentAccept")}</span>
                  )}
                </DialogDescription>
              </DialogHeader>
              <DialogFooter>
                {selectedNotification.rent.status === "PENDING" &&
                selectedNotification.rent.ownerEmail === session?.user.email ? (
                  <>
                    <Button
                      variant="destructive"
                      onClick={() =>
                        handleNotificationReject(selectedNotification)
                      }>
                      Reject
                    </Button>
                    <Button
                      onClick={() =>
                        handleNotificationConfirm(selectedNotification)
                      }>
                      Accept
                    </Button>
                  </>
                ) : (
                  <Button onClick={() => setIsNotificationDialogOpen(false)}>
                    Close
                  </Button>
                )}
              </DialogFooter>
            </>
          ) : (
            <>
              <DialogHeader>
                <DialogTitle>No notification selected</DialogTitle>
                <DialogDescription>
                  An error occurred. You are not supposed to se this page if no
                  notification is selected.
                </DialogDescription>
              </DialogHeader>
              <DialogFooter>
                <DialogClose>
                  <Button>Close</Button>
                </DialogClose>
              </DialogFooter>
            </>
          )}
        </DialogContent>
      </Dialog>
      <DropdownMenu modal={false}>
        <DropdownMenuTrigger asChild>
          <Button variant="ghost">
            <div className="relative">
              <Bell className="h-4 w-4" />

              {showNotificationIndicator() ? (
                <span className="absolute top-0 right-0 transform translate-x-1.15/2 translate-y-0.5 block h-[0.45rem] w-[0.45rem] bg-red-500 rounded-full"></span>
              ) : null}
            </div>
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent className=" relative right-14 w-64">
          {/* Owner Notifications */}
          {isOwner && (
            <>
              <DropdownMenuGroup>
                <DropdownMenuLabel>Owner Notifications</DropdownMenuLabel>
                <DropdownMenuSeparator />
                {ownerNotifications && ownerNotifications.length > 0 ? (
                  ownerNotifications.map(
                    (notification: Notification, index: number) => (
                      <DropdownMenuItem
                        key={index}
                        onSelect={() =>
                          handleSelectNotification(notification, "owner")
                        }>
                        {notification.ownerViewed ? (
                          <CircleCheckBig className="mr-2 h-4 w-4" />
                        ) : (
                          <Circle className="mr-2 h-4 w-4" />
                        )}

                        <span>{t("notification.rentRequest")}</span>
                      </DropdownMenuItem>
                    )
                  )
                ) : (
                  <DropdownMenuItem>
                    <div className="mr-2 h-4 w-4" />
                    <span>{t("notification.noNotification")}</span>
                  </DropdownMenuItem>
                )}
              </DropdownMenuGroup>

              {/* Old Notifications Section */}
              <DropdownMenuGroup>
                <DropdownMenuSeparator />
                <DropdownMenuLabel>Old Notifications</DropdownMenuLabel>
                <DropdownMenuSeparator />
                {oldOwnerNotifications.length > 0 ? (
                  oldOwnerNotifications.map((notification, index) => (
                    <DropdownMenuItem
                      key={index}
                      onSelect={() =>
                        handleSelectNotification(notification, "renter")
                      }>
                      <div className="mr-2 h-4 w-4" />
                      {notification.rent.status === "CONFIRMED" && (
                        <span>{t("notification.rentAccept")}</span>
                      )}
                      {notification.rent.status === "REJECTED" && (
                        <span>{t("notification.rentReject")}</span>
                      )}
                    </DropdownMenuItem>
                  ))
                ) : (
                  <DropdownMenuItem>
                    <div className="mr-2 h-4 w-4" />
                    <span>{t("notification.noNotification")}</span>
                  </DropdownMenuItem>
                )}
              </DropdownMenuGroup>
            </>
          )}

          {/* Separator if the user has both roles */}
          {isOwner && isRenter && <DropdownMenuSeparator />}

          {/* Renter Notifications */}

          {isRenter && (
            <>
              {/* General Renter Notifications Section */}
              <DropdownMenuGroup>
                <DropdownMenuLabel>Renter Notifications</DropdownMenuLabel>
                <DropdownMenuSeparator />
                {renterNotifications.length > 0 ? (
                  renterNotifications.map((notification, index) => (
                    <DropdownMenuItem
                      key={index}
                      onSelect={() =>
                        handleSelectNotification(notification, "renter")
                      }>
                      {notification.renterViewed ? (
                        <CircleCheckBig className="mr-2 h-4 w-4" />
                      ) : (
                        <Circle className="mr-2 h-4 w-4" />
                      )}
                      <span>
                        {notification.rent.status === "CONFIRMED" &&
                          t("notification.rentAccept")}
                        {notification.rent.status === "REJECTED" &&
                          t("notification.rentReject")}
                      </span>
                    </DropdownMenuItem>
                  ))
                ) : (
                  <DropdownMenuItem>
                    <div className="mr-2 h-4 w-4" />
                    <span>{t("notification.noNotification")}</span>
                  </DropdownMenuItem>
                )}
              </DropdownMenuGroup>

              {/* Pending Notifications Section */}
              <DropdownMenuGroup>
                <DropdownMenuSeparator />
                <DropdownMenuLabel>Pending Notifications</DropdownMenuLabel>
                <DropdownMenuSeparator />
                {pendingRenterNotifications.length > 0 ? (
                  pendingRenterNotifications.map((notification, index) => (
                    <DropdownMenuItem
                      key={index}
                      onSelect={() =>
                        handleSelectNotification(notification, "renter")
                      }>
                      <div className="mr-2 h-4 w-4" />
                      <span>{t("notification.rentRequest")}</span>
                    </DropdownMenuItem>
                  ))
                ) : (
                  <DropdownMenuItem>
                    <div className="mr-2 h-4 w-4" />
                    <span>{t("notification.noNotification")}</span>
                  </DropdownMenuItem>
                )}
              </DropdownMenuGroup>
            </>
          )}
        </DropdownMenuContent>
      </DropdownMenu>
    </>
  );
}
