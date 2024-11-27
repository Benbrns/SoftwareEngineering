import { Complaint } from "@/types";
import { useSession } from "next-auth/react";
import { useTranslation } from "react-i18next";

type Props = {
  complaints: Complaint[];
};

const ComplaintOverview: React.FC<Props> = ({ complaints }: Props) => {
  const { data: session, status } = useSession();
  const { t } = useTranslation();

  return (
    <>
      <div className="pt-4 flex justify-center mx-8 mt-4">
        <table className="w-full text-sm text-left">
          <thead className="text-xs text-gray-500 uppercase bg-gray-200 dark:text-gray-400">
            <tr>
              <th className="px-6 py-3">senderEmail</th>
              <th className="px-6 py-3">receiverEmail</th>
              <th className="px-6 py-3">title</th>
              <th className="px-6 py-3">description</th>
            </tr>
          </thead>
          <tbody>
            {complaints?.map((complaint: Complaint, index: number) => (
              <tr key={index} className="border-b bg-gray-150 border-white">
                <td className="px-6 py-4">{complaint.senderEmail}</td>
                <td className="px-6 py-4">{complaint.receiverEmail}</td>
                <td className="px-6 py-4">{complaint.title}</td>
                <td className="px-6 py-4">{complaint.description}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
};

export default ComplaintOverview;
