import { z } from "zod";
export const emailSchema = z.string().trim().email().max(254).transform(value => value.toLowerCase());
export const passwordSchema = z.string().min(12, "Gunakan setidaknya 12 karakter.").max(128, "Kata sandi terlalu panjang.");
export const registrationSchema = z.object({ name: z.string().trim().min(2).max(100), email: emailSchema, password: passwordSchema });
export const loginSchema = z.object({ email: emailSchema, password: z.string().min(1).max(128) });
