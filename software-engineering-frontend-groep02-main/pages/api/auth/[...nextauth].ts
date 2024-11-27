import { refreshUserData, userLogin } from "@/services/UserService";
import { User } from "@/types";
import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import { i18n } from "next-i18next";
import { jwtDecode } from "jwt-decode";
import { signOut } from "next-auth/react";

export default NextAuth({
  providers: [
    CredentialsProvider({
      // The name to display on the sign in form (e.g. 'Sign in with...')
      name: "Credentials",
      // The credentials is used to generate a suitable form on the sign in page.
      // You can specify whatever fields you are expecting to be submitted.
      // e.g. domain, username, password, 2FA token, etc.
      // You can pass any HTML attribute to the <input> tag through the object.
      credentials: {
        email: {
          label: "Email",
          type: "email",
          placeholder: "jsmith@example.com",
        },
        password: { label: "Password", type: "password" },
      },
      async authorize(credentials, req) {
        // You need to provide your own logic here that takes the credentials
        // submitted and returns either a object representing a user or value
        // that is false/null if the credentials are invalid.
        // e.g. return { id: 1, name: 'J Smith', email: 'jsmith@example.com' }
        // You can also use the `req` object to obtain additional parameters
        // (i.e., the request IP address)
        const res = await userLogin(credentials as User);

        const user = await res.json();

        // If no error and we have user data, return it
        if (res.ok && user) {
          return user;
        }
        // Return null if user data could not be retrieved
        return null;
      },
    }),
  ],

  pages: {
    signIn: `/${i18n?.language || "en"}/login`, // Use detected or default language
    error: `/auth/error`,
  },

  callbacks: {
    async jwt({ token, user }) {
      return { ...token, ...user };
    },

    async session({ session, token }) {
      const userData = await refreshUserData(token.token);
      const user = await userData.json();
      session.user = user;
      session.user.token = token.token as string;

      return session;
    },
  },
  secret: process.env.NEXTAUTH_SECRET,
});
