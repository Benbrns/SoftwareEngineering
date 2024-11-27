import { withAuth } from "next-auth/middleware";
import { NextResponse } from "next/server";
import { jwtDecode } from "jwt-decode";

import { Role } from "./types/index";
import { i18n } from "next-i18next";

export default withAuth(
  function middleware(req) {
    const { pathname } = req.nextUrl;
    const userRoles = (req.nextauth.token?.roles as string[]) || [];
    const token = req.nextauth.token;

    // Extract locale from URL or default to the default locale
    const locale = req.nextUrl.locale || i18n?.language || "en";

    const redirectTo = () => {
      const callbackUrl = encodeURIComponent(req.nextUrl.href);
      return NextResponse.redirect(
        new URL(
          `/${locale}/api/auth/signin?callbackUrl=${callbackUrl}`,
          req.url
        )
      );
    };

    // Check token expiration
    if (token) {
      const decodedToken = jwtDecode(token.token as string);
      const currentTime = Math.floor(Date.now() / 1000);

      if (decodedToken.exp && decodedToken.exp < currentTime) {
        // Token is expired, clear cookies and redirect to login
        const response = redirectTo();
        response.cookies.set("next-auth.session-token", "", { maxAge: -1 });
        response.cookies.set("__Secure-next-auth.session-token", "", {
          maxAge: -1,
        });
        return response;
      }
    }

    if (pathname.startsWith("/cars")) {
      if (!userRoles.includes("OWNER") && !userRoles.includes("ADMIN")) {
        console.log("Redirecting due to insufficient roles");
        return redirectTo();
      }
    }

    if (pathname.startsWith("/rents")) {
      if (
        !userRoles.includes("RENTER") &&
        !userRoles.includes("OWNER") &&
        !userRoles.includes("ADMIN")
      ) {
        console.log("Redirecting due to insufficient roles");
        return redirectTo();
      }
    }

    if (pathname.startsWith("/rentals")) {
      if (
        !userRoles.includes("RENTER") &&
        !userRoles.includes("OWNER") &&
        !userRoles.includes("ADMIN")
      ) {
        console.log("Redirecting due to insufficient roles");
        return redirectTo();
      }
    }

    return NextResponse.next();
  },
  {
    callbacks: {
      authorized: ({ token }) => !!token,
    },
  }
);

export const config = {
  matcher: ["/cars/:path*", "/rents/:path*", "/rentals/:path*"],
};
