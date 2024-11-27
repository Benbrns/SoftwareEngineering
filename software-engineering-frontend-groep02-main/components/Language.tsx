import React from "react";
import { useRouter } from "next/router";
import { useTranslation } from "next-i18next";

const Language: React.FC = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const { locale, pathname, asPath, query } = router;

  const handleLanguageChange = (event: { target: { value: string } }) => {
    const newLocale = event.target.value;
    const { pathname, asPath, query } = router;
    router.push({ pathname, query }, asPath, { locale: newLocale });
  };

  return (
    <div className="ml-6">
      <label htmlFor="language">{t("header.language")}</label>
      <select
        id="language"
        value={locale}
        onChange={handleLanguageChange}
        className="ml-2 p-1 text-black">
        <option value="en">English</option>
        <option value="fr">Français</option>
        <option value="nl">Nederlands</option>
      </select>
    </div>
  );
};

export default Language;
