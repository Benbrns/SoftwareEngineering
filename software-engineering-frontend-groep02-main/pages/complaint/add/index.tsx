import AddComplaint from "@/components/complaint/AddComplaint";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

const AddComplaintPage: React.FC = () => {
  return (
    <>
      <main className="mt-20 flex-grow px-20 pt-5 overflow-auto">
        <AddComplaint />
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

export default AddComplaintPage;
