import { z } from "zod";
import { db } from "../../../../../../lib/db/client";
import { hashPassword } from "../../../../../../lib/auth/password";
import { createMobileToken } from "../../../../../../lib/auth/mobile";

const registerSchema = z.object({
  name: z.string().trim().min(2, "Nama minimal 2 karakter"),
  email: z.string().email("Format email tidak valid"),
  password: z.string().min(8, "Password minimal 8 karakter"),
});

export async function POST(request: Request) {
  try {
    const json = await request.json().catch(() => null);
    const parsed = registerSchema.safeParse(json);
    if (!parsed.success) {
      const msg = parsed.error.issues[0]?.message ?? "Data pendaftaran tidak valid.";
      return Response.json({ success: false, error: msg }, { status: 400 });
    }

    const { name, email, password } = parsed.data;
    const existing = await db.user.findUnique({ where: { email } });
    if (existing) {
      return Response.json(
        { success: false, error: "Alamat email sudah terdaftar. Silakan masuk." },
        { status: 409 }
      );
    }

    const passwordHash = await hashPassword(password);
    const user = await db.user.create({
      data: {
        name,
        email,
        passwordHash,
        role: "PARTICIPANT",
      },
    });

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
      { success: false, error: error instanceof Error ? error.message : "REGISTER_FAILED" },
      { status: 500 }
    );
  }
}
