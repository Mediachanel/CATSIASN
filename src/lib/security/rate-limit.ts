import { createHash } from "node:crypto";
import { createClient } from "redis";

const globalRedis = globalThis as unknown as { authRedis?: ReturnType<typeof createClient> };
const redis = globalRedis.authRedis ?? createClient({ url: process.env.REDIS_URL ?? "redis://127.0.0.1:6379", socket: { connectTimeout: 2000, reconnectStrategy: false } });
redis.on("error", () => {});
if (process.env.NODE_ENV !== "production") globalRedis.authRedis = redis;
let connection: Promise<unknown> | undefined;
async function ensureConnected() {
  if (!redis.isOpen) {
    connection ??= redis.connect().finally(() => { connection = undefined; });
    await connection;
  }
}
export async function pingRedis() { await ensureConnected(); return redis.ping(); }
/** Fixed-window counter increment and expiry are atomic. Fail closed when Redis is unavailable. */
export async function allowAttempt(scope: string, identity: string, limit: number, seconds: number): Promise<boolean> {
  try {
    await ensureConnected();
    const digest = createHash("sha256").update(identity).digest("hex");
    const key = `cat:limit:${scope}:${digest}`;
    const count = await redis.eval("local n=redis.call('INCR',KEYS[1]); if n==1 then redis.call('EXPIRE',KEYS[1],ARGV[1]) end; return n", { keys: [key], arguments: [String(seconds)] });
    return Number(count) <= limit;
  } catch {
    if (process.env.NODE_ENV !== "production") {
      return true;
    }
    return false;
  }
}
