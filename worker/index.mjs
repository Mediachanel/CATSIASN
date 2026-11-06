import { Worker } from "bullmq";
import IORedis from "ioredis";
const url = process.env.REDIS_URL || "redis://127.0.0.1:6379";
if (!["redis:", "rediss:"].includes(new URL(url).protocol)) throw new Error("Unsupported Redis URL");
const connection = new IORedis(url, { maxRetriesPerRequest: null });
connection.on("error", () => {});
const log = (level, event, extra = {}) => console.log(JSON.stringify({ time: new Date().toISOString(), service: "worker", level, event, ...extra }));
const worker = new Worker("cat-maintenance", async job => {
  if (job.name !== "health-check") throw new Error("Unsupported job type");
  return { status: "ok" };
}, { connection, concurrency: 1 });
worker.on("ready", () => log("info", "worker_ready"));
worker.on("completed", job => log("info", "job_completed", { jobId: job.id, name: job.name }));
worker.on("failed", (job) => log("error", "job_failed", { jobId: job?.id, name: job?.name }));
let lastErrorLog = 0;
worker.on("error", () => { if (Date.now() - lastErrorLog >= 10000) { lastErrorLog = Date.now(); log("error", "worker_error"); } });
let closing = false;
async function shutdown() { if (closing) return; closing = true; log("info", "worker_stopping"); await worker.close(); await connection.quit(); }
for (const signal of ["SIGTERM", "SIGINT"]) process.on(signal, () => { shutdown().then(() => process.exit(0), () => process.exit(1)); });
