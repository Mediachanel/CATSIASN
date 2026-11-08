import { z } from "zod";
import { db } from "../../../../../../lib/db/client";
import { verifyPassword } from "../../../../../../lib/auth/password";
import { createMobileToken } from "../../../../../../lib/auth/mobile";

const loginSchema = z.object({
  email: z.string().email(),
  password: z.string().min(1),
});

export async function POST(request: Request) {
  try {
    const json = await request.json().catch(() => null);
    const parsed = loginSchema.safeParse(json);
    if (!parsed.success) {
      return Response.json(
        { success: false, error: "Format email atau password tidak valid." },
        { status: 400 }
      );
    }

    const { email, password } = parsed.data;
    const user = await db.user.findUnique({ where: { email } });
    if (!user) {
      return Response.json(
        { success: false, error: "Email atau kata sandi salah." },
        { status: 401 }
      );
    }

    const isValid = await verifyPassword(user.passwordHash, password);
    if (!isValid) {
      return Response.json(
        { success: false, error: "Email atau kata sandi salah." },
        { status: 401 }
      );
    }

    const token = await createMobileToken(user.id);

    return Response.json({
      success: true,
      token,
      user: {
        id: user.id,
        email: user.email,
        name: user.name,
        role: user.role,
      },
    });
  } catch (error) {
    return Response.json(
      { success: false, error: error instanceof Error ? error.message : "LOGIN_FAILED" },
      { status: 500 }
    );
  }
}
