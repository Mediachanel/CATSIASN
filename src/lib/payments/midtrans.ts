import { createHash, timingSafeEqual } from "node:crypto";

export type MidtransNotification = {
  order_id: string;
  status_code: string;
  gross_amount: string;
  signature_key: string;
  transaction_status: string;
  fraud_status?: string;
};

/** A verified signature only authenticates the notification; callers must also
 * compare the order and amount with their own records before granting access. */
export function verifyMidtransSignature(notification: MidtransNotification, serverKey: string): boolean {
  if (!serverKey || !/^[a-f0-9]{128}$/i.test(notification.signature_key)) return false;
  const expected = createHash("sha512")
    .update(notification.order_id + notification.status_code + notification.gross_amount + serverKey)
    .digest();
  const supplied = Buffer.from(notification.signature_key, "hex");
  return supplied.length === expected.length && timingSafeEqual(expected, supplied);
}

export function isMidtransPaid(notification: MidtransNotification): boolean {
  return notification.status_code === "200" &&
    (notification.transaction_status === "settlement" || notification.transaction_status === "capture") &&
    (!notification.fraud_status || notification.fraud_status === "accept");
}
