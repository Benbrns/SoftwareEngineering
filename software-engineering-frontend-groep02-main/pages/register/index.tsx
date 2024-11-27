import UserRegistrationForm from "@/components/users/userRegistrationForm";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import Head from "next/head";

const Registration: React.FC = () => {
  return (
    <>
      <Head>
        <title>Login</title>
      </Head>
      <main className="mt-20">
        <UserRegistrationForm />
      </main>
    </>
  );
};

export default Registration;

export const getServerSideProps = async (context: { locale: any }) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ["common"])),
    },
  };
};
