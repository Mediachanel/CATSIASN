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
  await prisma.tryoutEvent.upsert({
    where: { code: "DEMO-SKD-110" },
    create: {
      code: "DEMO-SKD-110",
      title: "Simulasi SKD Internal — 110 Soal",
      description: "Lingkungan latihan internal. Soal masih DRAFT dan memerlukan review ahli sebelum publikasi.",
      status: "PUBLISHED",
      isInternalDemo: true,
      durationMinutes: 100,
      twkCount: 30,
      tiuCount: 35,
      tkpCount: 45,
    },
    update: {},
  });
}
main().catch(error => { console.error(error); process.exitCode = 1; }).finally(() => prisma.$disconnect());
