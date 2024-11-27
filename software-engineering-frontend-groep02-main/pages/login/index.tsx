import Head from "next/head";
import UserLoginForm from "@/components/users/UserLoginForm";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

const Login: React.FC = () => {
  return (
    <>
      <Head>
        <title>Login</title>
      </Head>
      <main className="mt-20">
        <UserLoginForm />
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

export default Login;
