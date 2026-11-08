import { createHash } from "node:crypto";
import { describe, expect, it } from "vitest";
import { isMidtransPaid, verifyMidtransSignature } from "../../src/lib/payments/midtrans";

describe("Midtrans notification verification", () => {
  const key = "sandbox-test-key";
  const notification = {
    order_id: "order-123", status_code: "200", gross_amount: "45000.00",
    transaction_status: "settlement", signature_key: createHash("sha512").update("order-12320045000.00" + key).digest("hex"),
  };

  it("accepts a valid signature and rejects modified amounts or invalid signatures", () => {
    expect(verifyMidtransSignature(notification, key)).toBe(true);
    expect(verifyMidtransSignature({ ...notification, gross_amount: "100000.00" }, key)).toBe(false);
    expect(verifyMidtransSignature({ ...notification, signature_key: "invalid" }, key)).toBe(false);
    expect(verifyMidtransSignature(notification, "")).toBe(false);
  });

  it("only accepts successful, non-fraudulent payment statuses", () => {
    expect(isMidtransPaid(notification)).toBe(true);
    expect(isMidtransPaid({ ...notification, transaction_status: "pending" })).toBe(false);
    expect(isMidtransPaid({ ...notification, fraud_status: "challenge" })).toBe(false);
    expect(isMidtransPaid({ ...notification, status_code: "201" })).toBe(false);
  });
});
