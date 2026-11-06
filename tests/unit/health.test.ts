import { beforeEach, describe, expect, it, vi } from "vitest";
const dependencies = vi.hoisted(() => ({ query: vi.fn(), ping: vi.fn() }));
vi.mock("../../src/lib/db/client", () => ({ db: { $queryRaw: dependencies.query } }));
vi.mock("../../src/lib/security/rate-limit", () => ({ pingRedis: dependencies.ping }));
import { GET } from "../../src/app/api/v1/health/ready/route";
describe("readiness", () => {
 beforeEach(() => { dependencies.query.mockReset(); dependencies.ping.mockReset(); });
 it("returns ok when both dependencies respond", async () => { dependencies.query.mockResolvedValue([1]); dependencies.ping.mockResolvedValue("PONG"); const result = await GET(); expect(result.status).toBe(200); expect(await result.json()).toEqual({ status: "ok" }); });
 it("returns unavailable without exposing errors when Redis fails", async () => { dependencies.query.mockResolvedValue([1]); dependencies.ping.mockRejectedValue(new Error("sensitive detail")); const result = await GET(); expect(result.status).toBe(503); expect(await result.json()).toEqual({ status: "unavailable" }); });
 it("returns unavailable when database fails", async () => { dependencies.query.mockRejectedValue(new Error("connection failure")); dependencies.ping.mockResolvedValue("PONG"); expect((await GET()).status).toBe(503); });
});
