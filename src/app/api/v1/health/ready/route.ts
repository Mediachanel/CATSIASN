import { db } from "../../../../../lib/db/client";
import { pingRedis } from "../../../../../lib/security/rate-limit";
export const dynamic = "force-dynamic";
export async function GET() {
  let timer: ReturnType<typeof setTimeout> | undefined;
  try {
    await Promise.race([
      Promise.all([db.$queryRaw`SELECT 1`, pingRedis()]),
      new Promise<never>((_, reject) => { timer = setTimeout(() => reject(new Error("timeout")), 3000); }),
    ]);
    return Response.json({ status: "ok" }, { headers: { "Cache-Control": "no-store" } });
  } catch {
    return Response.json({ status: "unavailable" }, { status: 503, headers: { "Cache-Control": "no-store" } });
  } finally { if (timer) clearTimeout(timer); }
}
