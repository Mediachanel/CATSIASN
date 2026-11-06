import { PrismaClient } from '@prisma/client';
const prisma = new PrismaClient();
async function main() {
  // Create examples once; never overwrite administrator changes on later runs.
  for (const plan of [
    { code: 'FREE', name: 'Free', priceIdr: 0, durationDays: 90, quota: 1 },
    { code: 'PREMIUM', name: 'Premium', priceIdr: 49000, durationDays: 90, quota: 20 },
  ]) {
    await prisma.membershipPlan.upsert({ where: { code: plan.code }, create: plan, update: {} });
  }
}
main().catch(error => { console.error(error); process.exitCode = 1; }).finally(() => prisma.$disconnect());
