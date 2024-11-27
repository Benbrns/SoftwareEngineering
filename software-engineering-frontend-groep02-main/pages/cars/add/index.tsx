import Header from "@/components/Header/Header";
import Head from "next/head";
import React, { useState, useEffect } from "react";
import { GetServerSidePropsContext } from "next";
import AddCarForm from "@/components/addCarForm";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from "next-i18next";

const AddCar: React.FC = () => {
  const { t } = useTranslation();
  return (
    <>
      <Head>
        <title>{t("header.addCar")}</title>
      </Head>
      <main className=" mt-20 ">
        <h1 className=" mt-1 text-center text-6xl">{t("header.addCar")}</h1>
        <AddCarForm />
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

export default AddCar;
