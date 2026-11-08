import { readFile } from 'node:fs/promises';
import { PrismaClient } from '@prisma/client';

// Explicit opt-in: this script never deletes or overwrites existing questions.
if (!process.argv.includes('--apply')) {
  console.log('No changes made. After migrating a development database, pass --apply to import visual draft questions.');
  process.exit(0);
}
const rows = JSON.parse(await readFile('data/bank_visual/soal_visual.json', 'utf8'));
const labels = ['A', 'B', 'C', 'D', 'E'];
if (!Array.isArray(rows) || rows.length !== 6 || new Set(rows.map(r => r.code)).size !== rows.length) throw new Error('Invalid visual question bank');
for (const row of rows) {
  if (row.category !== 'TIU' || !labels.includes(row.correct_answer) || !row.explanation || !row.question_image_alt || !/^\/soal-visual\/[a-z0-9-]+\.svg$/.test(row.question_image)) throw new Error(`Invalid question ${row.code}`);
  await readFile(`public${row.question_image}`);
  for (const label of labels) {
    const key = label.toLowerCase();
    if (!row[`option_${key}`] || Number(row[`score_${key}`]) !== (label === row.correct_answer ? 5 : 0)) throw new Error(`Invalid option ${row.code}/${label}`);
  }
}
const db = new PrismaClient();
try {
  await db.$transaction(async tx => {
    for (const row of rows) {
      if (await tx.question.findUnique({ where: { code: row.code } })) continue;
      await tx.question.create({ data: {
        code: row.code, category: row.category, topic: row.topic, difficulty: row.difficulty,
        sourceReference: row.source_reference, reviewNote: row.review_note,
        versions: { create: { version: 1, status: 'DRAFT', isDemo: true, prompt: row.question_text,
          explanation: row.explanation, imagePath: row.question_image, imageAlt: row.question_image_alt,
          options: { create: labels.map(label => ({ label, text: row[`option_${label.toLowerCase()}`], isCorrect: label === row.correct_answer, weight: null })) }
        } }
      } });
    }
  });
  console.log('Visual draft questions imported; existing codes were left unchanged.');
} finally { await db.$disconnect(); }
