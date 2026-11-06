import { describe, expect, it } from "vitest";
import { registrationSchema } from "../../src/lib/validation/auth";
describe("registration validation", () => {
 it("normalizes email and trims name", () => { const result = registrationSchema.parse({ name: "  Peserta  ", email: " USER@Example.com ", password: "long-enough-password" }); expect(result.name).toBe("Peserta"); expect(result.email).toBe("user@example.com"); });
 it("rejects invalid email and short password", () => { expect(registrationSchema.safeParse({ name: "A", email: "invalid", password: "short" }).success).toBe(false); });
});
