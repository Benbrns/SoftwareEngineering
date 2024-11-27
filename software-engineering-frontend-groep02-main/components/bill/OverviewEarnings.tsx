import { setBillPaid } from "@/services/billService";
import { Bill } from "@/types";
import { useSession } from "next-auth/react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

type Props = {
  bills: Bill[];
};

const OverviewEarnings: React.FC<Props> = ({ bills }: Props) => {
  const { data: session, status } = useSession();
  const { t } = useTranslation();

  const [totalPaid, setTotalPaid] = useState<number>(0);
  const [totalUnpaid, setTotalUnpaid] = useState<number>(0);

  const getTotals = () => {
    let paid = 0;
    let unpaid = 0;
    bills.forEach((bill) => {
      if (bill.paid) {
        paid += bill.total;
      } else {
        unpaid += bill.total;
      }
    });
    setTotalPaid(paid);
    setTotalUnpaid(unpaid);
  };

  useEffect(() => {
    getTotals();
  }, [bills]);

  return (
    <>
      <div className="flex justify-center mx-8 mt-4">
        paid: € {totalPaid} | unpaid: € {totalUnpaid}
      </div>
    </>
  );
};

export default OverviewEarnings;
