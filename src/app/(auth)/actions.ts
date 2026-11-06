"use server";
import { AuthError } from "next-auth";
import { redirect } from "next/navigation";
import { Prisma } from "@prisma/client";
import { signIn, signOut } from "../../auth";
import { db } from "../../lib/db/client";
import { hashPassword } from "../../lib/auth/password";
import { allowAttempt } from "../../lib/security/rate-limit";
import { registrationSchema } from "../../lib/validation/auth";

export async function register(form: FormData) {
  const parsed = registrationSchema.safeParse(Object.fromEntries(form));
  if (!parsed.success) redirect("/register?error=invalid");
  const { name, email, password } = parsed.data;
  try {
    if (!await allowAttempt("registration", email, 5, 60 * 60)) redirect("/register?error=unavailable");
  } catch (error) {
    // Redirect errors are handled outside this block.
    if (error && typeof error === "object" && "digest" in error) throw error;
    redirect("/register?error=unavailable");
  }
  const passwordHash = await hashPassword(password);
  try { await db.user.create({ data: { name, email, passwordHash } }); }
  catch (error) {
    if (error instanceof Prisma.PrismaClientKnownRequestError && error.code === "P2002") redirect("/register?error=unavailable");
    throw error;
  }
  redirect("/login?registered=1");
}
export async function login(form: FormData) {
  try { await signIn("credentials", { email: form.get("email"), password: form.get("password"), redirectTo: "/dashboard" }); }
  catch (error) { if (error instanceof AuthError) redirect("/login?error=credentials"); throw error; }
}
export async function logout() { await signOut({ redirectTo: "/login" }); }
