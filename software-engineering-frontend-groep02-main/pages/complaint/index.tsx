import ComplaintOverview from "@/components/complaint/ComplaintOverview";
import { getAllComplaints } from "@/services/ComplaintService";
import { Complaint } from "@/types";
import { useSession } from "next-auth/react";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import Link from "next/link";
import { use, useEffect, useState } from "react";
import useInterval from "use-interval";

const ComplaintPage: React.FC = () => {
  const { data: session, status } = useSession();
  const [isLoading, setIsLoading] = useState(true);
  const [complaints, setComplaints] = useState<Complaint[]>([]);

  const fetchComplaints = async () => {
    if (status === "authenticated") {
      setIsLoading(true);
      const response = await getAllComplaints(session.user.token);
      if (response.ok) {
        const data = await response.json();
        setComplaints(data);
        setIsLoading(false);
      } else {
        console.error("Failed to fetch complaints");
        setIsLoading(false);
      }
    } else {
      console.error("Failed to fetch complaints");
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (status === "authenticated") {
      fetchComplaints();
    }
  }, [status]);

  useInterval(() => {
    fetchComplaints();
  }, 6000);

  return (
    <>
      <main className="mt-20 flex-grow px-20 pt-5 overflow-auto">
        <Link
          href={"/complaint/add"}
          className="p-2 bg-blue-500 ml-20 rounded-md"
        >
          Make Complaint
        </Link>
        {isLoading && !complaints && <p>Loading...</p>}
        {!isLoading && complaints.length < 1 && (
          <h1 className="text-center text-red-700 text-2xl p-10">
            There are no complaints to show
          </h1>
        )}
        {complaints?.length > 0 && (
          <ComplaintOverview complaints={complaints} />
        )}
      </main>
    </>
  );
};

export const getServerSideProps = async (context: { locale: any }) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ["common"])),
    },
  };
};

export default ComplaintPage;
