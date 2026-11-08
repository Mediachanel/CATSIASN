import { describe, expect, it } from "vitest";
import { hashToken } from "../../src/lib/auth/mobile";

describe("mobile auth tokens", () => {
  it("hashes tokens deterministically with sha256", () => {
    const raw = "test-token-123456";
    const hash1 = hashToken(raw);
    const hash2 = hashToken(raw);
    expect(hash1).toBe(hash2);
    expect(hash1).toHaveLength(64);
  });

  it("produces different hashes for different tokens", () => {
    const hashA = hashToken("token-a");
    const hashB = hashToken("token-b");
    expect(hashA).not.toBe(hashB);
  });
});
