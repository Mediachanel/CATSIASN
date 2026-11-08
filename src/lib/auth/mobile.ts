import crypto from "node:crypto";
import { db } from "../db/client";

export function hashToken(token: string): string {
  return crypto.createHash("sha256").update(token).digest("hex");
}

export async function createMobileToken(userId: string, durationDays = 30): Promise<string> {
  const rawToken = crypto.randomBytes(32).toString("hex");
  const tokenHash = hashToken(rawToken);
  const expiresAt = new Date(Date.now() + durationDays * 24 * 60 * 60 * 1000);

  await db.authSession.create({
    data: {
      userId,
      tokenHash,
      expiresAt,
    },
  });

  return rawToken;
}

export async function verifyMobileToken(rawToken: string) {
  const tokenHash = hashToken(rawToken);
  const now = new Date();

  const session = await db.authSession.findFirst({
    where: {
      tokenHash,
      expiresAt: { gt: now },
    },
    include: {
      user: {
        select: {
          id: true,
          email: true,
          name: true,
          role: true,
        },
      },
    },
  });

  return session?.user ?? null;
}

export async function getAuthenticatedUser(request: Request) {
  const authHeader = request.headers.get("Authorization");
  if (authHeader?.startsWith("Bearer ")) {
    const token = authHeader.slice(7).trim();
    if (token) {
      const user = await verifyMobileToken(token);
      if (user) return user;
    }
  }

  // Fallback to NextAuth session cookie (for web / hybrid testing)
  try {
    const { auth } = await import("../../auth");
    const session = await auth();
    if (session?.user?.id) {
      const user = await db.user.findUnique({
        where: { id: session.user.id },
        select: { id: true, email: true, name: true, role: true },
      });
      if (user) return user;
    }
  } catch {
    // Non-web or unconfigured session context
  }

  return null;
}
