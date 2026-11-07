import NextAuth from "next-auth";
import Credentials from "next-auth/providers/credentials";
import { db } from "./lib/db/client";
import { hashPassword, verifyPassword } from "./lib/auth/password";
import { allowAttempt } from "./lib/security/rate-limit";
import { loginSchema } from "./lib/validation/auth";

// A dummy hash makes unknown users perform a password verification too.
let dummyHash: Promise<string> | undefined;
export const { handlers, auth, signIn, signOut } = NextAuth({
  basePath: "/api/v1/auth",
  // This self-hosted app derives its callback URL from the request host.
  // Deployments must restrict which hosts can reach the application.
  trustHost: true,
  pages: { signIn: "/login" },
  session: { strategy: "jwt", maxAge: 60 * 60 * 8 },
  providers: [Credentials({
    credentials: { email: {}, password: {} },
    async authorize(input) {
      const parsed = loginSchema.safeParse(input);
      if (!parsed.success) return null;
      const { email, password } = parsed.data;
      // No client-provided IP is trusted; this is an account-level limit, not an IP-level defense.
      try { if (!await allowAttempt("login", email, 10, 15 * 60)) return null; } catch { return null; }
      const user = await db.user.findUnique({ where: { email } });
      dummyHash ??= hashPassword("not-a-real-user-password");
      const valid = await verifyPassword(user?.passwordHash ?? await dummyHash, password);
      if (!user || !valid) return null;
      return { id: user.id, name: user.name, email: user.email };
    },
  })],
  callbacks: {
    jwt({ token, user }) { if (user?.id) token.sub = user.id; return token; },
    session({ session, token }) { if (session.user && token.sub) session.user.id = token.sub; return session; },
  },
});
