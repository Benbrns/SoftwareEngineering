import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import Head from "next/head";
import Image from "next/image";
import Link from "next/link";

export default function Home() {
  return (
    <div className="relative w-full min-h-screen">
      <Head>
        <title>Car Rental Service</title>
        <meta name="description" content="Rent your dream car with us" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className="text-center">
        <div className="relative w-full h-screen">
          {/* Background Image */}
          <div className="absolute inset-0 z-0">
            <Image
              src="/img/hero-car-min.jpg" // Ensure your image path is correct
              layout="fill"
              objectFit="cover"
              alt="Car Rental"
              priority // Helps with prioritizing the loading of the image
            />
            {/* Dark overlay */}
            <div className="absolute inset-0 bg-black opacity-50 z-10"></div>
          </div>

          {/* Text Overlay */}
          <div className="flex flex-col justify-center items-center h-full z-20 relative">
            <h1 className="text-6xl text-white font-bold mb-2">
              Explore at Your Own Pace
            </h1>
            <p className="text-2xl text-gray-300 mb-4">
              The freedom to drive anywhere, anytime.
            </p>
            <Link
              href={"/rentals"}
              className="bg-blue-500 text-white px-10 py-2 rounded-full hover:bg-blue-700 transition duration-300">
              Book Now
            </Link>
          </div>
        </div>
      </main>
    </div>
  );
}

export const getServerSideProps = async (context: { locale: any }) => {
  const { locale } = context;
  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ["common"])),
    },
  };
};
