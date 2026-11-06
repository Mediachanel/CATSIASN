import { Queue, QueueEvents } from "bullmq";
import IORedis from "ioredis";
const url = process.env.REDIS_URL || "redis://127.0.0.1:6379";
const producerConnection = new IORedis(url, { maxRetriesPerRequest: 1 });
const eventsConnection = new IORedis(url, { maxRetriesPerRequest: null });
const queue = new Queue("cat-maintenance", { connection: producerConnection });
const events = new QueueEvents("cat-maintenance", { connection: eventsConnection });
let job;
try {
  await events.waitUntilReady();
  job = await queue.add("health-check", {}, { removeOnComplete: false, removeOnFail: false });
  const result = await job.waitUntilFinished(events, 15000);
  if (result?.status !== "ok") throw new Error("Unexpected worker result");
  console.log("Worker processed the health-check job.");
} finally { if (job) await job.remove().catch(() => {}); await events.close(); await queue.close(); await eventsConnection.quit(); await producerConnection.quit(); }
