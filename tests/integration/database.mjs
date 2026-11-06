// Requires a migrated development database and seeded plans. Never points at production.
import { PrismaClient } from '@prisma/client';
import assert from 'node:assert/strict';
const prisma = new PrismaClient();
async function main() {
  const plans = await prisma.membershipPlan.findMany({ where: { code: { in: ['FREE', 'PREMIUM'] } } });
  assert.equal(plans.length, 2, 'run the seed before this check');
  let rejected = false;
  try {
    await prisma.$transaction(async tx => {
      await tx.membershipPlan.create({ data: { code: `INVALID_${Date.now()}`, name: 'invalid', priceIdr: -1, durationDays: 90, quota: 1 } });
      throw new Error('ROLLBACK: constraint did not reject invalid plan');
    });
  } catch (error) {
    if (!String(error.message).includes('membership_plans_valid_values')) throw error;
    rejected = true;
  }
  assert.ok(rejected, 'database must reject negative prices');
  const batches = await prisma.questionImportBatch.findMany({ orderBy: { code: 'asc' }, select: { code: true, rowCount: true } });
  assert.deepEqual(batches, [
    { code: 'BATCH01', rowCount: 3000 },
    { code: 'BATCH02', rowCount: 3000 },
    { code: 'BATCH03', rowCount: 3000 },
  ]);
  assert.equal(await prisma.question.count(), 9000);
  assert.equal(await prisma.questionVersion.count({ where: { status: 'DRAFT', isDemo: true } }), 9000);
  assert.equal(await prisma.questionOption.count(), 45000);
  const categoryCounts = await prisma.question.groupBy({ by: ['category'], _count: true });
  assert.deepEqual(Object.fromEntries(categoryCounts.map(row => [row.category, row._count])), { TWK: 2250, TIU: 3150, TKP: 3600 });
  assert.equal(await prisma.questionOption.count({ where: { weight: { not: null } } }), 18000);
  console.log('Database integration checks passed: seeded plans and check constraint.');
}
main().catch(error => { console.error(error); process.exitCode = 1; }).finally(() => prisma.$disconnect());
