import Header from "@/components/Header/Header";
import "@/styles/globals.css";
import { appWithTranslation } from "next-i18next";
import type { AppProps } from "next/app";
import SessionProvider from "@/components/SessionProvider";
import { ThemeProvider } from "@/components/ThemeProvider";
import localFont from "next/font/local";

const inter = localFont({
  src: "../public/fonts/inter/Inter-VariableFont_slnt,wght.ttf",
  variable: "--font-inter",
});

const App = ({ Component, pageProps }: AppProps) => {
  return (
    <SessionProvider>
      <ThemeProvider
        attribute="class"
        defaultTheme="system"
        enableSystem
        disableTransitionOnChange>
        <Header />
        <div
          className={`${inter.className} grid grid-rows-[auto_1fr_auto] h-screen`}>
          <Component {...pageProps} />
        </div>
      </ThemeProvider>
    </SessionProvider>
  );
};

export default appWithTranslation(App);
